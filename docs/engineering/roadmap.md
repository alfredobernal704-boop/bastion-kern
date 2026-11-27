# Engineering maturity roadmap

**Status:** Proposed sequence; dates and completion claims are intentionally omitted until scope, ownership, and evidence are agreed.

## Stage 0 — Repository foundation

- [x] Replace placeholder README with project overview and status boundaries.
- [x] Establish documentation navigation.
- [x] Preserve existing proprietary evaluation notice and legal records.
- [x] Add initial event/provider schema contracts.
- [x] Add lightweight repository validation workflow.
- [ ] Confirm repository contents and application source root.
- [ ] Confirm branch protection, required reviews, and release permissions.

## Stage 1 — Architecture and contracts

- [ ] Record current implementation architecture from source inspection.
- [ ] Establish versioned API and event schema policy.
- [ ] Define event identity, deduplication, lifecycle, and revision semantics.
- [ ] Define source adapter interface and quarantine contract.
- [ ] Add architecture decision records for material stack choices.
- [ ] Add reproducible unit and integration test strategy.

## Stage 2 — Backend and data governance

- [ ] Identify the existing Supabase project and owner.
- [ ] Audit schema, extensions, migrations, RLS, storage, and Edge Functions.
- [ ] Establish local/development, staging, and production separation.
- [ ] Add migration workflow and schema validation.
- [ ] Approve provider terms, attribution, retention, and commercial-use status.
- [ ] Implement provider health and freshness monitoring.

## Stage 3 — Ingestion and event quality

- [ ] Implement one provider adapter at a time.
- [ ] Validate payloads and timestamps before normalisation.
- [ ] Preserve raw source lineage and transformation version.
- [ ] Quarantine malformed or ambiguous records.
- [ ] Test coordinate transformations and GeoJSON geometry.
- [ ] Implement idempotent ingestion and documented deduplication.

## Stage 4 — Geospatial client and user experience

- [ ] Verify the current Mapbox integration and account configuration.
- [ ] Confirm a single shared map surface and module boundaries.
- [ ] Implement viewport-aware event queries and clustering.
- [ ] Verify accessible Global, Local, and Navigation states.
- [ ] Test low-bandwidth, delayed-provider, and offline/fallback behaviour.
- [ ] Measure mobile performance, memory, battery, and thermal behaviour.

## Stage 5 — Alerts and operational readiness

- [ ] Define alert eligibility, severity thresholds, and confidence gates.
- [ ] Add user-controlled watch areas and notification preferences.
- [ ] Implement idempotent notification dispatch and audit history.
- [ ] Conduct privacy and security reviews.
- [ ] Exercise incident response and provider outage scenarios.
- [ ] Define SLOs, support ownership, and release rollback procedures.

## Stage 6 — External evaluation

- [ ] Publish a verified architecture diagram and system boundaries.
- [ ] Publish a provider and third-party rights register.
- [ ] Provide reproducible tests and a release/change history.
- [ ] Complete legal-entity, trademark, privacy, and commercial licence reviews.
- [ ] Commission independent security and reliability testing before operational claims.

## Release gates

A release is not production-ready until its code, configuration, tests, data rights, privacy controls, security controls, and rollback path have been reviewed. A passing documentation workflow is only a repository-quality signal.
