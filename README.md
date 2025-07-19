# GPS Mover Xposed Module

**GPS Mover** is an advanced Android application and Xposed/LSPosed module that allows you to spoof your device's GPS location at the system level without requiring mock location permissions. It hooks into Android's system and Google Play Services location APIs to provide a realistic and customizable fake location experience, affecting all apps on the device.

------

## Features

### XposedHook Module

- **GPS Location Spoofing**
  - Spoof device GPS coordinates with realistic random drift for natural movement.
  - Spoof altitude, bearing, speed, timestamp, and configurable location accuracy.
  - Periodically updates location with throttling to avoid excessive updates.
- **System-Level Location Spoofing**
  - Hooks Android system services (`LocationManagerService`) to spoof:
    - `getLastLocation`
    - `requestLocationUpdates`
    - `getCurrentLocation` (Android 13/14)
    - `LocationCallback.onLocationResult`
  - Works at the framework level affecting all apps using system location APIs.
- **Google Play Services Spoofing**
  - Hooks `FusedLocationProviderClient.getLastLocation` and `LocationCallback.onLocationResult` to inject spoofed locations.
- **WiFi and Cellular Network Spoofing**
  - Overrides `WifiManager.getScanResults` to return fake WiFi scan results.
  - Spoofs telephony methods such as `getCellLocation`, `getNeighboringCellInfo`, and `getAllCellInfo`.
- **Mock Location & Xposed Detection Bypass**
  - Hooks `AppOpsManager.checkOp` to hide mock location usage.
  - Hides Xposed framework presence by intercepting `ClassLoader.loadClass`.
  - Provides internal hooks for secure module status and preferences.
- **Dynamic Location Updates**
  - Supports runtime location updates via a broadcast receiver (`com.hamham.gpsmover.UPDATE_LOCATION`).
  - Shows a notification when location is updated dynamically.
- **Blacklist Support**
  - Excludes important system apps (e.g., Camera, WhatsApp, Play Store) from spoofing.
- **Robustness**
  - Exception handling and null checks prevent crashes.
  - Uses Kotlin coroutines for asynchronous handling.
  - Compatible with multiple Android API levels, including hidden API bypass.
- **Logging & Debugging**
  - Uses `Timber` for structured logging with conditional debug logs.

------

### GPS Mover App

- **Set Fake Location:**
   Instantly set device location to any point on the map without mock location permissions.

- **Favorites Management:**

  - Add,  reorder (drag & drop), and delete favorite locations.
  - Quickly set device location by tapping favorites.

- **Search by Coordinates:**
   Directly search and set any latitude and longitude.

- **Custom Accuracy:**
   Configure reported location accuracy (e.g., 10m, 50m).

- **Randomization Mode:**
   Send randomized locations within a user-defined radius to simulate natural movement.

- **Map Interface:**

  - Google Maps integration.
  - Tap anywhere on the map to select location.
  - Floating action buttons for adding favorites, moving to real location, and toggling spoofing

  ------

  

  ### Favorites Synchronization

  The favorites list is fully synchronized with Firestore database in real-time.

  - When a favorite location is **added**, it is immediately saved to Firestore.
  - When a favorite is **deleted**, it is instantly removed from Firestore.
  - Changes are automatically synced between the app and Firestore, ensuring consistency across devices and sessions.

  

- **Settings:**

  - Theme selection: Light, Dark, or System default.
  - Map type selection: Normal, Satellite, Terrain.
  - Enable/disable advanced system location hook.
  - Configure randomization and accuracy settings.

- **Persistent Notifications:**
   Displays a notification when spoofing is active.

- **Material 3 Design:**
   Modern UI using Material You components supporting light and dark modes.

- **Xposed/LSPosed Integration:**
   Detects module status and prompts if not enabled.

------

## Supported Android Versions

- Minimum: Android 8.1 (API 27) 
- Target/Compile: Android 14 (API 34) (tested up to Android 16)

------

## Dependencies

- Google Maps SDK
- Material Components (Material 3)
- AndroidX libraries (ViewModel, LiveData, Navigation, Room, etc.)
- Hilt (Dependency Injection)
- Timber (Logging)
- Retrofit (Networking)
- Xposed/LSPosed APIs

------

## Installation & Setup

1. **Requirements:**
   - Rooted Android device.
   - LSPosed or Xposed framework installed and enabled.
   - GPS Mover app installed with all necessary permissions (location, storage, notifications).
2. **Enable Module:**
   - Activate the GPS Mover Xposed/LSPosed module.
   - Reboot device if needed.
3. **Using the App:**
   - Open the app to view the map.
   - Use floating action buttons to add favorites, move to real location, or start/stop spoofing.
   - Manage favorites and settings via bottom navigation.

------

## Controlling via ADB Broadcast Commands

You can control GPS Mover remotely via ADB broadcast commands (useful for automation).

**Important:** Always specify package name `-p com.hamham.gpsmover` for reliable delivery on Android 8+.

| Command                | Description                  | Example                                                      |
| ---------------------- | ---------------------------- | ------------------------------------------------------------ |
| `gps.start`            | Start fake GPS spoofing      | `adb shell am broadcast -a gps.start -p com.hamham.gpsmover` |
| `gps.stop`             | Stop spoofing                | `adb shell am broadcast -a gps.stop -p com.hamham.gpsmover`  |
| `gps.set`              | Set specific location        | `adb shell am broadcast -a gps.set --es location "30.0444,31.2357" -p com.hamham.gpsmover` |
| `gps.set` + `random`   | Set randomization radius (m) | `adb shell am broadcast -a gps.set --ei random 10 -p com.hamham.gpsmover` |
| `gps.set` + `accuracy` | Set location accuracy (m)    | `adb shell am broadcast -a gps.set --ei accuracy 15 -p com.hamham.gpsmover` |

*You can combine extras (location, random, accuracy) in a single command.*

------

## Developer

**Mohammed Hamham**
 Email: [dv.hamham@gmail.com](mailto:dv.hamham@gmail.com)

------

## License

See the [LICENSE](https://chatgpt.com/c/LICENSE) file for license details.

------

If you want me to generate a ready-to-use markdown file or add sections like screenshots, usage tips, or contribution guidelines, just let me know!
