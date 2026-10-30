#!/usr/bin/env python3
"""Select exactly one currently advertised physical Android PHONE for Firebase Robo.

Input: gcloud firebase test android models list --format=json
Output: GitHub Actions output file and a human-readable device summary.
No device identifiers are invented: every selection comes from the live catalogue.
"""
import argparse
import json
import os
from pathlib import Path

VERSIONS = ("35", "34", "33", "32", "31", "30", "29", "28", "27", "26")


def choose_device(models, requested_model="", requested_version=""):
    if bool(requested_model) != bool(requested_version):
        raise ValueError("Both requested device model and OS version must be supplied together.")

    candidates = []
    for model in models:
        if not isinstance(model, dict):
            continue
        if str(model.get("form", "")).upper() != "PHYSICAL":
            continue
        if str(model.get("formFactor", "")).upper() != "PHONE":
            continue
        model_id = str(model.get("id", ""))
        supported = {str(v) for v in model.get("supportedVersionIds", [])}
        available = [v for v in VERSIONS if v in supported]
        if not model_id or not available:
            continue

        if requested_model:
            if model_id == requested_model and requested_version in supported:
                return model_id, requested_version, str(model.get("name", model_id))
            continue

        if str(model.get("tags", "")).lower().find("deprecated") >= 0:
            continue
        version = available[0]
        name = str(model.get("name", model_id))
        maker = str(model.get("manufacturer", "")).lower()
        score = (
            1 if maker in ("google", "samsung") else 0,
            int(version),
            1 if maker == "google" else 0,
            model_id,
        )
        candidates.append((score, model_id, version, name))

    if requested_model:
        raise ValueError("Requested physical model/version is unavailable in the current catalogue.")
    if not candidates:
        raise ValueError("No compatible physical Android PHONE found in the current catalogue.")
    _, model_id, version, name = max(candidates)
    return model_id, version, name


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--catalogue", required=True)
    parser.add_argument("--requested-model", default="")
    parser.add_argument("--requested-version", default="")
    parser.add_argument("--output", required=True)
    parser.add_argument("--summary", required=True)
    args = parser.parse_args()

    models = json.loads(Path(args.catalogue).read_text(encoding="utf-8"))
    if not isinstance(models, list):
        raise ValueError("Expected a list from gcloud Test Lab device catalogue")
    model_id, version, name = choose_device(
        models, args.requested_model.strip(), args.requested_version.strip()
    )
    with open(args.output, "a", encoding="utf-8") as out:
        out.write(f"model={model_id}\nversion={version}\n")
    Path(args.summary).write_text(
        f"Physical Android device: {name} (model={model_id}, API={version})\n",
        encoding="utf-8",
    )
    print(f"Selected 1 physical Android phone: {name} (ID={model_id}, API={version})")


if __name__ == "__main__":
    main()
