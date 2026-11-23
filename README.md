# Gen5W Climate Mini — v0.2 persistent-overlay diagnostic

Small experimental floating HVAC **UI only** for Hyundai/Kia Gen5W (Android 4.4.2 compatible). The launcher icon depicts a climate/air-conditioning fan rather than the default Android robot.

**Warning:** This is still **not** a working vehicle climate controller. The four tiles show `?`, since no verified HVAC read or actuation API is integrated. Touching a tile only displays a message; it never issues vehicle commands.

## What changed since v0.1

- Tries `TYPE_SYSTEM_ALERT` for API 19–25 and falls back to the v0.1 `TYPE_PHONE` mode if rejected; keeps `TYPE_APPLICATION_OVERLAY` for API 26+. This may allow the window to stay on top of other apps on Gen5W.
- Keeps the floating service in the foreground and uses `START_STICKY`; requests restoration after system process recycling. Explicitly closing the popup disables restoration.
- Uses `stopWithTask=false` so leaving/closing the launcher Activity does not intentionally stop the overlay.
- Close button is separate from the draggable title; **−** collapses to a compact title strip.
- Remembers the manually dragged position.
- Climate-themed launcher and notification icons.

Neither foreground services nor alert-window types can override proprietary head-unit window manager restrictions. Behavior over JMK, built-in navigation, Android Auto, reversing-camera and safety-warning UIs **is not yet validated**. Do not force windows over safety-critical interfaces.

## Build APK in GitHub

Upload the unpacked project folder's **contents** to the same GitHub repository, retaining directory structure and `.github/workflows/build-apk.yml`. Open the **Actions** tab → **Build diagnostic APK** → run workflow (or push to `main`). Download the newest artifact `Gen5W-ClimateMini-APK`, unzip, install `app-debug.apk` via NEMESIS's supported APK installation interface.

Local: Java 17, Android SDK, Gradle 8.7+, `gradle assembleDebug`.

## Parked test (Korean IONIQ 5 Gen5W V039)

1. Install v0.2 over v0.1 if signatures match. If Android reports signature mismatch, uninstall old v0.1 before reinstalling.
2. Open app → **미니창 열기**. Grant overlay permission if the operating system asks (Android 6+).
3. While parked, tap vehicle Home, stock navigation, JMK launcher, or Android Auto. Check whether the mini window stays visible.
4. Drag the title to move; press **−** to minimize and **+** to expand; press **×** to stop.
5. While parked, ensure the reverse-camera, critical vehicle warnings, map controls and touch input remain unobstructed. Close the popup if any conflict appears.
6. If it still disappears, report **whether it disappears after Home, after JMK, or only on wireless Android Auto**, and whether opening the app again restores the original overlay or creates a new one. That helps distinguish window layering from service lifecycle.
7. This build does **not** open automatically when the vehicle boots.

Vehicle API validation and read-only state discovery are future prerequisites, not claims of current support. No changes to NEMESIS/JMK, no system partitions modified, no root access used.
