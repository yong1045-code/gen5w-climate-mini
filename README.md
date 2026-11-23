# Gen5W Climate Mini — v0.1 diagnostic

A minimal Android 4.4.2-compatible floating climate **UI prototype** for Hyundai/Kia Gen5W infotainment.

> **IMPORTANT:** This release has **no access to vehicle HVAC state** and **does not send HVAC commands**. All four indicators intentionally display `?`. It is **NOT** a working climate controller.

## Target environment

- Korean-market 2022 Hyundai IONIQ 5, Gen5W V039 (`NEEV.KOR.0000.V039.001.260902`), **not yet validated on vehicle**.
- NEMESIS installed. JMK may be present. Compatibility with JMK / Android Auto overlays is not validated.
- Minimum Android SDK 19 (Android 4.4). Java 8 sources.

## Build with GitHub Actions

1. Create a new GitHub repository (e.g., `gen5w-climate-mini`).
2. Upload **all contents** of this project, keeping `.github/workflows/build-apk.yml` and folders intact.
3. Open **Actions → Build diagnostic APK → Run workflow** (or push to `main`).
4. After successful build, download artifact `Gen5W-ClimateMini-v0.1-diagnostic-APK` and extract `app-debug.apk`.

## Local build

Requires Android SDK, Gradle 8.7+, Java 17: `gradle assembleDebug`.

## Test only while parked

1. Install the debug APK through NEMESIS's supported Android APK installation interface. This project is **not** an installable `NEMESIS.data` package.
2. Open **Gen5W Climate Mini**, grant overlay permission if required, tap **Open mini window**.
3. Drag the header to move; tap the header without moving to close.
4. Indicators remain unknown (`?`) and buttons display **vehicle control not connected**.
5. Stop the overlay and uninstall if it covers safety warnings or causes focus/input issues.

## Roadmap / prerequisites before live control

- Determine read-only HVAC API state names for DRIVER ONLY, A/C, HEAT, SYNC on **this exact firmware**.
- Verify reliable state changes initiated in stock climate UI; never infer from previous button taps.
- Identify and verify safe actuator API calls separately with explicit confirmation and parked testing.
- Add app-level permission checks, rollback, error state, and tests for reverse camera and warning overlays.
- Add NEMESIS installation packaging only after confirming package schema and device compatibility.

No root/service commands are included. This avoids speculative vehicle control calls and preserves the existing JMK wireless Android Auto / HUD setup.
