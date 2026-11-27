# Cloudflare ingestion implementation

**Branch:** feature/cloudflare-provider-ingestion-2026-11  
**Status:** Source implementation added; deployment and live provider verification pending.

## Runtime topology
- Cloudflare Workers executes provider adapters, API handlers and scheduled collection.
- Cloudflare D1 stores canonical event JSON, query fields and provider-run history.
- Cron is configured for 15-minute collection.
- Manual ingestion is protected by a server-side bearer secret.
- NASA FIRMS is conditional on a server-side map key.

## Event contract
The Worker emits schema version 1.0.0 and preserves canonical event dimensions. Existing root schemas are unchanged. Full JSON Schema runtime validation and fixture-based adapter contract tests remain production gates.

## Provider implementation boundary
See the provider register at ../../cloudflare/worker/providers.json and the Worker README. USGS, NASA EONET, NOAA/NWS and GDACS have first-pass adapters. NASA FIRMS is optional. NASA GIBS is imagery, MET Norway is location-query forecast context, and Copernicus requires selection of a specific dataset; these are not completed event feeds.

## Storage limitation
D1 is not PostGIS. The current schema stores point coordinates and canonical JSON. Polygon centroids are display approximations only; they are not impact polygons and must not drive geofencing, route safety or alert-radius decisions. A separate spatial database decision is required for production spatial queries.

## Required deployment gates
1. Create the D1 database and replace the placeholder database ID in wrangler.toml.
2. Apply the migration and configure INGEST_TOKEN.
3. Optionally configure NASA_FIRMS_MAP_KEY.
4. Run typecheck, adapter fixtures, schema validation and D1 migration tests.
5. Verify provider terms, rate limits, attribution, freshness and failure handling.
6. Verify API authentication, CORS policy, retention and monitoring.
7. Perform controlled live fetches and record exact timestamps and outcomes.

No credentials, database identifiers, production endpoints or secrets are committed by this implementation.
