#!/usr/bin/env python3
"""Validate the Firebase iOS app identity; this does not build an iOS target."""
import plistlib
import sys
from pathlib import Path

PROJECT_ID = "bastion-sdk"
PROJECT_NUMBER = "158994318096"
BUNDLE_ID = "Bastionkern.offentlogspot.com"


def verify(config):
    if config.get("PROJECT_ID") != PROJECT_ID:
        raise ValueError("iOS Firebase project ID differs from bastion-sdk")
    if str(config.get("GCM_SENDER_ID")) != PROJECT_NUMBER:
        raise ValueError("iOS Firebase sender ID does not match the Firebase project")
    if config.get("BUNDLE_ID") != BUNDLE_ID:
        raise ValueError(
            f"iOS Firebase bundle ID mismatch: expected {BUNDLE_ID!r}, "
            f"received {config.get('BUNDLE_ID')!r}"
        )
    if not str(config.get("GOOGLE_APP_ID", "")).startswith(f"1:{PROJECT_NUMBER}:ios:"):
        raise ValueError("iOS Firebase app ID does not match the project")
    return True


def main():
    if len(sys.argv) != 2:
        raise SystemExit("usage: verify_firebase_ios_config.py path/to/GoogleService-Info.plist")
    config = plistlib.loads(Path(sys.argv[1]).read_bytes())
    verify(config)
    print("Firebase iOS config validated for the existing registered bundle ID")


if __name__ == "__main__":
    main()
