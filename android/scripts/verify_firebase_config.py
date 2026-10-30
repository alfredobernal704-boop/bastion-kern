#!/usr/bin/env python3
"""Validate a real Firebase Android config for the BASTION-KERN application.

No API key or credential material is printed. app.bastionkern is the Android
applicationId in the GitHub project, and its Firebase config must match exactly.
"""
import json
import sys
from pathlib import Path

PROJECT_ID = "bastion-sdk"
PROJECT_NUMBER = "158994318096"
PACKAGE_NAME = "app.bastionkern"


def verify(data):
    if not isinstance(data, dict):
        raise ValueError("Firebase config must be a JSON object")
    project = data.get("project_info") or {}
    if project.get("project_id") != PROJECT_ID:
        raise ValueError("Firebase project ID differs from bastion-sdk")
    if str(project.get("project_number")) != PROJECT_NUMBER:
        raise ValueError("Firebase project number differs from the existing bastion-sdk project")
    clients = data.get("client", [])
    if not isinstance(clients, list):
        raise ValueError("Expected a list of Firebase Android clients")
    candidates = [
        client for client in clients
        if (client.get("client_info") or {}).get("android_client_info", {})
           .get("package_name") == PACKAGE_NAME
    ]
    if len(candidates) != 1:
        found = sorted({
            str((c.get("client_info") or {}).get("android_client_info", {}).get("package_name"))
            for c in clients
            if isinstance(c, dict) and (c.get("client_info") or {}).get("android_client_info", {}).get("package_name")
        })
        raise ValueError(
            f"Android package mismatch: expected {PACKAGE_NAME!r}, registered in JSON: {found!r}. "
            "Register app.bastionkern as an Android app in bastion-sdk; "
            "do not modify applicationId or copy the config from a different app."
        )
    app_id = candidates[0].get("client_info", {}).get("mobilesdk_app_id", "")
    if not str(app_id).startswith(f"1:{PROJECT_NUMBER}:android:"):
        raise ValueError("Firebase Android app ID does not match the selected project")
    if not candidates[0].get("api_key"):
        raise ValueError("Registered Firebase client has no API key configuration")
    return True


def main():
    if len(sys.argv) != 2:
        raise SystemExit("usage: verify_firebase_config.py android/app/google-services.json")
    config = json.loads(Path(sys.argv[1]).read_text(encoding="utf-8"))
    verify(config)
    print("Firebase Android config validated: bastion-sdk/app.bastionkern")


if __name__ == "__main__":
    main()
