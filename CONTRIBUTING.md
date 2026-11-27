# Contributing to BASTION-KERN

Thank you for considering a contribution. This repository is proprietary evaluation material unless a file states otherwise; submitting a pull request does not itself grant a general licence to use the project or third-party assets.

## Before starting

1. Read [README.md](README.md), [SECURITY.md](SECURITY.md), and the relevant architecture and data-governance documents.
2. Open or locate an issue describing the problem or proposed change.
3. For substantial work, agree the scope and acceptance criteria before implementation.
4. Check provider terms and third-party licences before adding code, data, screenshots, logos, fonts, or other assets.

## Contribution rules

- Keep changes focused and explain the reason for each material change.
- Do not commit API keys, passwords, access tokens, private user data, production exports, or local environment files.
- Do not add credentials to screenshots, logs, fixtures, test output, or documentation.
- Do not present mock, fixture, historical, or delayed data as live.
- Preserve source attribution, timestamps, licence references, and provenance.
- Preserve the distinction between severity, confidence, verification, and freshness.
- Follow the coordinate and GeoJSON rules in [the CRS contract](docs/geospatial/coordinate-reference-systems.md).
- Do not add a provider integration until its API terms, rate limits, attribution, and data rights have been reviewed.
- Avoid unrelated formatting or generated-file churn.

## Branches and commits

Use a descriptive branch name, for example:

- `docs/provider-provenance`
- `fix/geojson-axis-validation`
- `feat/event-deduplication`

Use concise conventional commit messages where practical:

- `docs: document provider attribution`
- `fix: reject invalid coordinate positions`
- `feat: add event lifecycle metadata`
- `test: cover event schema validation`

## Pull request requirements

A pull request should include:

- Problem statement and rationale.
- Scope and implementation summary.
- Relevant issue or design decision.
- Tests and validation performed, with exact commands and outcomes.
- Schema or migration impact, if any.
- Security, privacy, data-rights, and provider-terms impact.
- Screenshots for user-interface changes, with all sensitive values removed.
- Known limitations and follow-up work.

Do not claim checks passed unless they were run and the result is available. Do not mark a feature production-ready solely because it works in a local preview.

## Review and acceptance

All changes require maintainer review. A maintainer may request revisions, reject a change, or defer it until legal, data, security, or product questions are resolved. Do not merge your own changes unless the repository's maintainers have explicitly authorised that process.

## Vulnerability reporting

Do not create a public issue for an exploitable security vulnerability. Follow the private reporting instructions in [SECURITY.md](SECURITY.md).
