# BASTION-KERN — Firebase Test Lab Phase 1 (Android)

## Verified build under test

GitHub Actions run: https://github.com/alfredobernal704-boop/bastion-kern/actions/runs/41069944385
Latest successful APK ZIP artifact from run attempt 2: ID `12984308506` (created 2026-10-30T14:20:34Z; expires 2026-11-06).

This APK **compiled and passed JVM tests**, but has NOT yet been run on Android devices. The current first milestone only contains Mapbox Standard/Satellite, annotated USGS M4.5+ events and an event details sheet.

## Preferred immediate manual smoke test (no CI configuration)

1. Create/select a **dedicated Firebase project** at https://console.firebase.google.com/.
2. Open **DevOps & Engagement > Test Lab**, select **Run a Robo test**, and upload `app-debug.apk` from the GitHub build artifact ZIP.
3. Choose **one physical Android phone**, portrait, with a compatible Android version. Avoid selecting a matrix of multiple physical devices on the initial run.
4. Run for **300 seconds (5 minutes)**, capture screenshots, videos, crashes and performance metrics.
5. Inspect every screenshot/video for real Mapbox map tiles, globe rendering, labels, event pins and the Standard/Satellite switch. Inspect USGS data and the official event link.
6. Compare results with the acceptance matrix below. Robo can miss custom OpenGL/Mapbox content and cannot prove actual tile rendering automatically.

## GitHub Actions CI integration (OIDC, no downloaded long-lived keys)

Workflow: `.github/workflows/firebase-testlab-android.yml`. It requires a one-time Google Cloud/Firebase setup and can be triggered manually. It deliberately does **not** run Firebase tests on every push, to avoid charging testing quota. On feature branches, GitHub may not expose the manual **Run workflow** button until the workflow is present in the repository default branch.

Required GitHub repository **Actions variables**:

- `FIREBASE_PROJECT_ID`: exact Google Cloud/Firebase project ID
- `GCP_WIF_PROVIDER`: Workload Identity Federation provider full resource path
- `GCP_SERVICE_ACCOUNT`: service-account email authorised to use Firebase Test Lab
- `FIREBASE_RESULTS_BUCKET`: optional, for an owned Cloud Storage results bucket

Configure Workload Identity Federation in Google Cloud and restrict GitHub subjects to `alfredobernal704-boop/bastion-kern`, ideally the dedicated workflow/ref. Grant `roles/iam.workloadIdentityUser` on the service account to the intended GitHub principal.

**IAM details matter:** Firebase documents that gcloud with the default Firebase-managed results bucket requires `roles/editor` on the selected Firebase project. Use a **dedicated test project** if choosing this broad default. For least-privilege operation with a dedicated, owned results bucket (which requires billing enabled and may incur storage costs), configure `roles/cloudtestservice.testAdmin` and `roles/firebase.analyticsViewer`, plus the necessary bucket-scoped Cloud Storage object permissions. Do not assign global organisation-wide Owner roles.

Enable the Firebase Test Lab/Cloud Testing APIs as documented and check quota/billing before execution. Neither selecting an existing Firebase account nor GitHub source access alone grants Test Lab permissions.

Once your Firebase project and OIDC federation are configured, merge/release the workflow onto the repository default branch for a visible manual dispatch button. Trigger the single physical-device smoke test with a **currently valid** Android build artifact ID and its originating run ID. Expired artifacts must be rebuilt.

## Physical-device acceptance matrix

| Case | Verification | Threshold |
| --- | --- | --- |
| Install and startup | No install errors, crash, ANR or immediate exit | Pass |
| 3D globe | Mapbox Standard globe and initial camera visually load in recording | Visual review |
| Standard/Satellite | Both controls visibly switch base styles | Visual review |
| USGS events | Recent USGS M4.5+ markers and list appear with source status | API/visual review |
| Event details | Tapping record shows magnitude, depth, UTC and trusted source link | Manual/recording review |
| Robustness | No critical logs or repeated process death | Pass |
| Performance | Review CPU, memory, network and response times | Baseline only |
| Battery/thermal | Collect via specialised profiling later; Robo metrics do not reliably measure battery drain | Not established |

### Interpretation of results

A green Robo outcome means the automated crawl completed its checks without a reported test failure. It **does not** verify Mapbox GPU raster output or the reliability/freshness of USGS feed data; screenshots, logs and targeted instrumentation are required.

Quotas: Firebase Spark includes up to **5 physical-device test runs/day** (and up to 10 virtual runs/day). Blaze may charge for usage exceeding its free testing allowance; prices and quota vary by account. Keep one test device per first run and do not schedule repeated execution without reviewing cost.

Official guides:
- https://firebase.google.com/docs/test-lab/android/firebase-console
- https://firebase.google.com/docs/test-lab/android/robo-ux-test
- https://firebase.google.com/docs/test-lab/usage-quotas-pricing
- https://firebase.google.com/docs/test-lab/android/iam-permissions-reference
- https://github.com/google-github-actions/auth
