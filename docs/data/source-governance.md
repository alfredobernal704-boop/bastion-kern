# Data-source governance and provenance

**Status:** Governance contract. The presence of a provider in this document is not proof of a live integration or a permission to redistribute its content.

## 1. Provider onboarding record

Every provider adapter must have a reviewed record containing:

| Field | Requirement |
|---|---|
| Provider ID | Stable internal identifier |
| Provider name | Official name and operator |
| Dataset/API | Exact endpoint, product, and version |
| Authority class | Official authority, scientific sensor, news, community, or unknown |
| Data classes | Hazard, weather, seismic, wildfire, hydrology, infrastructure, etc. |
| Terms | API terms and licence URL |
| Attribution | Exact required credit and link |
| Rate limits | Published limits, quotas, and retry guidance |
| Freshness | Expected update interval and source timestamp semantics |
| Geometry | Geometry type, CRS, axis order, spatial accuracy |
| Retention | Caching, archival, and deletion restrictions |
| Commercial use | Explicitly reviewed status |
| Review | Reviewer, date, and evidence |

## 2. Initial candidate catalogue

These are candidate sources aligned with the product scope. Each endpoint and dataset requires a separate rights and technical review before production use.

| Provider / service | Candidate domain | Review requirements |
|---|---|---|
| USGS Earthquake Hazards | Seismic events | Feed semantics, revisions, attribution, update cadence |
| NASA EONET | Environmental events | Originating asset rights and metadata terms |
| NASA FIRMS | Active fire observations | API access, latency, attribution, redistribution |
| NASA GIBS / Earthdata | Earth-observation imagery | Product-specific terms, authentication, tile usage |
| NOAA / NWS | Weather and warnings | Endpoint-specific policy, geography, attribution, freshness |
| GDACS | Global disaster alerts | Terms of use, source lineage, interpretation limits |
| MET Norway | Weather data | API terms, CC BY/NLOD applicability, attribution |
| ESA / Copernicus | Earth observation | Dataset-specific licence and processing constraints |
| GDELT | News context | Geolocation quality, publisher rights, freshness, context-only policy |

## 3. Canonical provenance fields

At minimum, preserve:

- `provider_id`
- `source_record_id`
- `source_url`
- `source_published_at` (when supplied)
- `source_observed_at` (when supplied)
- `ingested_at` (system UTC timestamp)
- `source_updated_at` (when supplied)
- `source_version` or revision identifier (when supplied)
- `source_crs` and source axis order for spatial data
- `attribution` and `terms_url`
- `transformation_version`
- `raw_payload_hash` where lawful and useful
- `normalisation_status` and quarantine reason

Never substitute ingestion time for observation time. Preserve unknown values as unknown; do not manufacture timestamps or confidence.

## 4. Validation and quarantine

Reject or quarantine records with invalid schema, invalid coordinates, impossible timestamps, unsupported geometry, missing required identifiers, or ambiguous CRS/axis order. Keep a machine-readable reason and provider reference. Do not silently repair data where the intended value cannot be established.

Deduplication must use provider identifiers and documented matching logic. Similar location and category alone are not sufficient evidence that two records describe the same event.

## 5. Trust, severity, and classification

Keep these dimensions separate:

- Source authority/type.
- Verification status.
- Confidence score and method.
- Severity and its classification basis.
- Freshness and data availability.
- Event lifecycle.

News coverage is contextual evidence, not automatically a geospatial hazard. A report should become a mapped event only when it is current, geolocatable, relevant, and sufficiently corroborated under documented rules.

## 6. Operational monitoring

For every adapter, monitor successful fetch time, response status, latency, rate-limit responses, parse failures, records accepted/rejected, and data age. Logs must not contain credentials or unnecessary personal location data.

Use bounded retries and backoff. A provider outage should produce a delayed or unavailable state, not fabricated or stale-live output.

## 7. Review cadence

Review provider terms before initial integration, after material API or licence changes, and at least once per release cycle. Update the root `docs/rights-register.json` when a rights determination changes.
