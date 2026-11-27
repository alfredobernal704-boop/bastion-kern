# Backend integration boundary

**Status:** Integration contract; connection and production configuration are unverified.

## Intended responsibility split

The client application is an untrusted environment. It may contain only public client configuration and publishable keys. Provider secrets, database service credentials, signing secrets, and privileged operations must remain server-side.

Supabase may provide PostgreSQL/PostGIS, authentication, storage, and server-side functions where explicitly configured. The repository must not assume that a Supabase project exists merely because the product architecture references Supabase.

## Configuration policy

- Use environment-specific configuration for local, preview, staging, and production.
- Commit a variable *name* only when the application actually consumes it; never commit a value.
- Do not expose `service_role`, secret API keys, database passwords, JWT signing secrets, or provider tokens in client bundles.
- A publishable/legacy anon key is not an authorisation boundary. Database access must be controlled by grants and RLS.
- Rotate any credential that has entered Git history or a public build; removing it from the latest commit is insufficient.
- Keep schema changes in reviewed, ordered migration files once the target project and migration workflow are established.

## Supabase security requirements

1. Enable RLS on every table in an exposed schema.
2. Define policies from the actual ownership and access model; `TO authenticated` alone does not enforce row ownership.
3. For UPDATE policies, define both `USING` and `WITH CHECK`.
4. Do not authorise access from user-editable `user_metadata`.
5. Prefer invoker-security views and functions. Review every privileged function for privilege escalation.
6. Restrict storage buckets and object policies to the intended users and operations.
7. Run Supabase security and performance advisors after schema changes.
8. Keep development, staging, and production environments separate.

## Connection procedure

1. Confirm the correct Supabase organisation and project with the project owner.
2. Confirm the target environment and region.
3. Connect the existing project through the Lovable project's Integrations panel or the approved Supabase workflow.
4. Configure secrets in the platform's secure environment-variable interface.
5. Inspect existing tables, policies, extensions, migrations, and Edge Functions before changing anything.
6. Apply additive migrations in a development environment first.
7. Verify access with authenticated, unauthenticated, and cross-user test cases.
8. Promote through reviewed migrations; never copy production secrets or personal data into fixtures.

## Current audit note

At the time this documentation was prepared, the connected Supabase account returned no accessible projects. Therefore no project ID, schema, migration, RLS policy, database URL, or production connection is asserted here. The Lovable workspace lists the existing BASTION-KERN project, but the Lovable agent could not be run because the workspace reported that it was out of credits. GitHub-to-Lovable synchronisation has not been verified.

## Change control

Do not create a new Supabase project, incur compute costs, enable billing, run destructive SQL, or change production access policies as part of a documentation-only repository update.
