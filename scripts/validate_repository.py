#!/usr/bin/env python3
"""Dependency-free validation for the repository's documentation contracts."""

from __future__ import annotations

import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
REQUIRED_FILES = (
    "README.md",
    "CONTRIBUTING.md",
    "SECURITY.md",
    "CHANGELOG.md",
    "docs/README.md",
    "docs/architecture/system-overview.md",
    "docs/architecture/backend-integration.md",
    "docs/data/source-governance.md",
    "docs/security/threat-model.md",
    "docs/governance/licensing-and-ip.md",
    "docs/engineering/roadmap.md",
    "schemas/event.schema.json",
    "schemas/provider.schema.json",
)


def main() -> int:
    errors: list[str] = []

    for relative in REQUIRED_FILES:
        if not (ROOT / relative).is_file():
            errors.append(f"Missing required file: {relative}")

    for path in sorted((ROOT / "schemas").glob("*.json")):
        try:
            data = json.loads(path.read_text(encoding="utf-8"))
        except (OSError, json.JSONDecodeError) as exc:
            errors.append(f"Invalid JSON in {path.relative_to(ROOT)}: {exc}")
            continue

        if data.get("$schema") != "https://json-schema.org/draft/2020-12/schema":
            errors.append(f"Unexpected or missing JSON Schema draft in {path.relative_to(ROOT)}")
        if not isinstance(data.get("title"), str) or not data["title"].strip():
            errors.append(f"Missing schema title in {path.relative_to(ROOT)}")

    if errors:
        print("Repository validation failed:")
        for error in errors:
            print(f" - {error}")
        return 1

    print(f"Repository validation passed: {len(REQUIRED_FILES)} required files; JSON contracts parsed.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
