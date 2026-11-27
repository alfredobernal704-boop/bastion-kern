# Engineering documentation

This index separates product intent, engineering contracts, and verified operational status.

## Architecture
- [System overview](architecture/system-overview.md) — logical components, trust boundaries, data flow, and failure behaviour.
- [Backend integration](architecture/backend-integration.md) — Cloudflare Workers/D1 ingestion boundary and future spatial-service decision.
- [Coordinate reference systems](geospatial/coordinate-reference-systems.md) — WGS 84, EPSG:4326 axis order, and RFC 7946 GeoJSON rules.

## Data governance
- [Source governance](data/source-governance.md) — provider governance, attribution, licensing, freshness, and ingestion controls.
- [Event contract](../schemas/event.schema.json) — normalised event record.
- [Provider contract](../schemas/provider.schema.json) — provider metadata and governance fields.
- [Cloudflare provider register](../cloudflare/worker/providers.json) — implementation and review state of provider candidates.

## Security and governance
- [Threat model](security/threat-model.md) — assets, trust boundaries, threats, and baseline controls.
- [Licensing and IP](governance/licensing-and-ip.md) — project notice, third-party rights, and decision gates.
- [Existing legal notices](legal-notices.txt) and [rights register](rights-register.json) — existing project legal records.

## Engineering operations
- [Cloudflare ingestion implementation](engineering/cloudflare-ingestion.md) — Worker runtime, provider coverage, storage limits and deployment gates.
- [Roadmap](engineering/roadmap.md) — staged engineering maturity plan.
- [Contribution guide](../CONTRIBUTING.md) — pull request and review expectations.
- [Security reporting](../SECURITY.md) — private vulnerability reporting.

## Status vocabulary
Use these terms consistently:
- **Implemented:** present in inspected code and covered by a reproducible check.
- **Configured:** credentials/infrastructure are set, but this does not imply healthy operation.
- **Verified:** a named test or operational check passed at a stated time.
- **Planned:** intended, not implemented.
- **Unknown:** evidence is insufficient to make a claim.
- **Blocked:** a stated prerequisite prevents progress.

A design document, roadmap item, mock-up, or provider listed here is not proof of a running capability.
