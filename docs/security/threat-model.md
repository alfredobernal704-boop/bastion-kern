# Security threat model

**Status:** Initial qualitative model; not a penetration test, audit certification, or proof of secure operation.

## 1. Scope and assumptions

The model covers a public repository and a planned geospatial application with external provider ingestion, a web/mobile client, backend services, geospatial storage, and optional notifications.

Assume:
- Public source and browser-delivered assets can be inspected.
- Provider responses and user-submitted content are untrusted.
- Client-side checks can be bypassed.
- A public/publishable key is not a secret and does not replace server-side authorisation.
- Infrastructure, database, deployment, and production access controls remain to be verified.

## 2. Assets

- Provider API credentials and backend secrets.
- User accounts, sessions, and access tokens.
- Precise location and saved/watch areas.
- Event records, provenance, and source payloads.
- Alert rules, device tokens, and notification history.
- Database backups, logs, and deployment configuration.
- Repository write permissions and release credentials.

## 3. Trust boundaries

1. External provider → ingestion adapter.
2. Public client → application API.
3. Authenticated user → database and storage.
4. Backend worker → notification provider.
5. CI workflow → repository and deployment credentials.
6. Maintainer workstation → GitHub and cloud accounts.

Validate data at every boundary. Do not rely on a client to enforce access control.

## 4. Threat scenarios and baseline mitigations

| Threat | Example impact | Baseline mitigation |
|---|---|---|
| Secret exposure | Provider or database compromise | Secret manager, no secrets in Git/client, rotation |
| Broken object-level authorisation | Cross-user access to saved locations or reports | Server-side ownership checks and RLS |
| Privilege escalation | User gains administrative access | Server-controlled roles; never trust editable user metadata |
| Injection / unsafe rendering | Provider text executes in client context | Schema validation, output encoding, safe URL handling |
| Malicious provider payload | Parser crash or corrupt geometry | Size limits, strict parsing, quarantine |
| Replay / duplicate alert | Repeated or misleading notifications | Idempotency keys, event lifecycle, dispatch audit |
| Stale event | User sees outdated information as live | Freshness timestamps and delayed/unavailable state |
| Supply-chain compromise | Malicious dependency or CI action | Pin versions, review updates, lockfiles, minimal actions |
| CI secret misuse | Build token exfiltration | Least privilege, protected environments, no untrusted PR secrets |
| Location data over-collection | Unnecessary exposure of sensitive movement data | Purpose limitation, minimisation, retention and deletion controls |
| Denial of service / quota exhaustion | Provider or API availability loss | Rate limits, timeouts, bounded retries, caching policy |
| False source attribution | Misleading authority or endorsement | Plain-text attribution, verified links, rights register |

## 5. Supabase-specific controls

- Enable RLS on all exposed tables.
- Pair authenticated role checks with row ownership or another explicit authorisation predicate.
- Use both `USING` and `WITH CHECK` for UPDATE policies.
- Never use user-editable `user_metadata` for access decisions.
- Prefer `SECURITY INVOKER`; isolate and review any necessary privileged function.
- Review views for RLS bypass behaviour.
- Keep service-role and secret keys exclusively on trusted server infrastructure.
- Restrict Storage buckets and object policies.
- Run security and performance advisors after schema changes.

## 6. Repository and CI controls

- Keep the repository public-safe: no credentials, private dumps, personal data, or confidential vendor material.
- Require pull-request review for protected branches.
- Limit workflow permissions to the minimum required.
- Pin third-party GitHub Actions to reviewed commit SHAs.
- Avoid executing untrusted pull-request code with write-scoped secrets.
- Protect release and deployment credentials with environments and approvals.

## 7. Incident response minimum

1. Triage and classify the incident.
2. Contain access or disable the affected integration.
3. Revoke and rotate exposed credentials; review access logs.
4. Preserve relevant evidence without copying unnecessary personal data.
5. Assess affected users, providers, and data.
6. Restore from a known-good state and verify controls.
7. Document cause, impact, remediation, and follow-up owners.

## 8. Open verification items

- Production authentication and authorisation configuration.
- Supabase schema, RLS policies, storage rules, and function privileges.
- Provider credential storage and rotation process.
- CI workflow permissions and branch protection.
- Dependency inventory and lockfile coverage.
- Privacy retention/deletion controls for location and notification data.
- Independent security testing and incident-response exercise.

Do not describe these controls as implemented until evidence is collected.
