#!/usr/bin/env python3
"""Verify a Firebase config belongs to precisely bastion-sdk/app.bastionkern."""
import json
import sys
from pathlib import Path

PROJECT_ID = "bastion-sdk"
PACKAGE_NAME = "app.bastionkern"


def verify(data):
    if not isinstance(data, dict):
        raise ValueError("Firebase config must be a JSON object")
    if (data.get("project_info") or {}).get("project_id") != PROJECT_ID:
        raise ValueError("Firebase project ID differs from bastion-sdk")
    candidates = [
        client for client in data.get("client", [])
        if (client.get("client_info") or {}).get("android_client_info", {})
           .get("package_name") == PACKAGE_NAME
    ]
    if len(candidates) != 1:
        raise ValueError("Expected exactly one Firebase Android client for app.bastionkern")
    if not candidates[0].get("client_info", {}).get("mobilesdk_app_id"):
        raise ValueError("Firebase app ID missing from registered Android client")
    if not data.get("project_info", {}).get("project_number"):
        raise ValueError("Firebase project number missing")
    return True


def main():
    if len(sys.argv) != 2:
        raise SystemExit("usage: verify_firebase_config.py path/to/google-services.json")
    config = json.loads(Path(sys.argv[1]).read_text(encoding="utf-8"))
    verify(config)
    print("Firebase config validated: project and package match.")


if __name__ == "__main__":
    main()
