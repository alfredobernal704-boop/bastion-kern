# BASTION-KERN Cloudflare ingestion service

**Status:** Initial implementation branch; not deployed or operationally verified.

## Components
- Cloudflare Worker: HTTP API, scheduled ingestion, authenticated manual ingestion.
- Cloudflare D1: event and provider-run persistence.
- Adapters: USGS, NASA EONET, NOAA/NWS, GDACS RSS, optional NASA FIRMS.
- Canonical payloads preserve the existing event schema's version, severity, verification, lifecycle, timestamps, geometry and provenance.

## Provider scope and limits
| Provider | State | Boundary |
|---|---|---|
| USGS Earthquakes | Adapter implemented | All-day GeoJSON feed |
| NASA EONET | Adapter implemented | Open events; point geometries only in this first iteration |
| NOAA/NWS | Adapter implemented | Active alerts; polygon alerts reduced to centroid for point storage |
| GDACS | RSS adapter implemented | No geometry inferred from text |
| NASA FIRMS | Optional adapter | Requires server-side NASA_FIRMS_MAP_KEY; detections are not confirmed perimeters |
| NASA GIBS / Earthdata / LANCE | Dataset and map-layer candidates | Imagery and near-real-time dataset access, not one canonical event feed |
| MET Norway | Context candidate | Forecast API is location-query based, not a global hazard-event feed |
| NASA GPM IMERG | Dataset-specific | Precipitation estimates; requires product-level processing and validation |\n| ESA/Copernicus | Dataset-specific | Select exact product and review its terms before implementation |\n| GDELT | Candidate only | Geolocated news context; never treat news as authoritative hazard detection |

The provider catalogue is not endorsement, rights clearance or proof of a live connection. No provider logos are included. Review rights and attribution before production use.

## Configure
1. Run npm install.
2. Create D1: npm run db:create.
3. Replace database_id in wrangler.toml with the returned ID.
4. Apply: npm run db:migrate:remote.
5. Set secret: npx wrangler secret put INGEST_TOKEN.
6. Optionally set secret NASA_FIRMS_MAP_KEY.
7. Run npm run typecheck and npm run dev.
8. Deploy only after provider terms and operational tests are approved.

## API
- GET /health
- GET /v1/providers
- GET /v1/events?limit=100&category=seismic
- POST /internal/ingest with Authorization: Bearer token

Scheduled collection is configured every 15 minutes. Never expose INGEST_TOKEN to a client.

## Limitations
D1 is not PostGIS. This version stores point coordinates and canonical JSON, not spatial polygons, geofencing or routing. Polygon centroids are approximations and must not be treated as impact areas. Full JSON Schema runtime validation, adapter contract tests, D1 provisioning, secrets, monitoring, retention controls and live-provider verification remain required. The root LICENSE, SECURITY.md, legal notices, rights register and canonical schemas are intentionally untouched.
