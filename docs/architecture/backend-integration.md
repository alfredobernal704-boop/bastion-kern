# Backend integration boundary

**Status:** Integration contract; connection and production configuration are unverified.

## Selected edge runtime

The engineering branch now contains a first-pass Cloudflare Workers ingestion service under cloudflare/worker. Cloudflare Workers is the selected edge execution layer for provider collection and the read API. Cloudflare D1 is the initial persistence target for canonical event JSON and provider-run metadata.

This does not replace the existing event/provider JSON Schema contracts or the repository's legal and rights records. The Worker is not deployed. The D1 database has not been created, the database ID remains a placeholder, and no Cloudflare credentials or secrets have been configured.

## Intended responsibility split

The client application is an untrusted environment. It may contain only public client configuration and publishable keys. Provider secrets, database service credentials, signing secrets, and privileged operations must remain server-side.

D1 is not PostGIS. The initial schema stores point coordinates and canonical JSON; it does not provide spatial predicates, impact polygon analysis, routing or geofencing. Do not use centroid approximations for safety decisions. A production spatial query requirement needs a separately reviewed spatial database/service decision.

## Cloudflare configuration and deployment

1. Create the D1 database for the target Cloudflare account.
2. Replace the placeholder database ID in cloudflare/worker/wrangler.toml.
3. Apply the checked-in migration in a development environment.
4. Configure INGEST_TOKEN and, if approved, NASA_FIRMS_MAP_KEY as Worker secrets.
5. Run the Worker typecheck and adapter contract tests.
6. Verify provider terms, attribution, rate limits, freshness and error handling.
7. Review API authentication, CORS, retention, monitoring and deployment environment before promotion.

See [Cloudflare ingestion implementation](../engineering/cloudflare-ingestion.md).

## Supabase integration boundary retained for future spatial needs

Supabase may still be evaluated for PostgreSQL/PostGIS, authentication, storage, and server-side functions if the project owner selects it for a specific requirement. No Supabase project is assumed or created by this branch.

### Security requirements if Supabase is later connected

1. Enable RLS on every table in an exposed schema.
2. Define policies from the actual ownership and access model; TO authenticated alone does not enforce row ownership.
3. For UPDATE policies, define both USING and WITH CHECK.
4. Do not authorise access from user-editable user_metadata.
5. Prefer invoker-security views and functions. Review every privileged function for privilege escalation.
6. Restrict storage buckets and object policies to intended users and operations.
7. Run security and performance advisors after schema changes.
8. Keep development, staging, and production environments separate.

## Existing audit note

The earlier audit found no accessible Supabase projects in the connected account and did not verify GitHub-to-Lovable synchronisation. That historical finding is preserved; this Cloudflare branch does not claim to have resolved either item.

## Change control

Do not create a production database, incur compute costs, enable billing, change production access policies, or deploy this Worker without explicit environment selection and review.
