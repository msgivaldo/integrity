# Integrity Android Library

**Integrity** is a lightweight, easy-to-use security library for Android designed to detect potential threats to your application's environment. It provides checks for root access, emulators, app cloning, virtualization, and unauthorized modifications.

---

## Features

The library includes several security validation modules:

* **Device Integrity**: Detects if the device is rooted using both Kotlin and Native (C) methods.
* **Emulator Detection**: Identifies if the app is running on a generic or known Android SDK emulator.
* **Virtualization Detection**: Checks for known virtualization/cloning apps (e.g., Parallel Space, VMOS) and looks for virtual libraries in memory.
* **Developer Settings**: Detects if ADB is enabled, Developer Mode is on, or if Package Verification is disabled.
* **App Integrity**: Validates the application's Package Name and SHA-256 Signing Signature against expected values.
* **Background Protection**: Optionally monitors security state changes while the app is in the background.

---

## Installation

Add the library to your project by including the `:integrity` module in your `build.gradle.kts`:

```kotlin
dependencies {
    implementation(project(":integrity"))
}
```

## Quick Start

### 1. Initialization
The library uses Android Startup for automatic initialization, but you can configure it via IntegrityInitializer:

```kotlin
val configuration = buildConfiguration("your-api-key") {
    enableLogging(true)
    enableBackgroundChecks(true)
    setAppId("your.package.name")
    setAppSignature("YOUR_SHA256_HEX_SIGNATURE")
}

Integrity.instance.init(context, configuration)
```

### 2. Running Detections
You can start security scans at any time. The results are delivered asynchronously via a listener:

```kotlin
Integrity.instance.startDetections { result ->
    when (val securityState = result.result) {
        is SecurityCheck.Secure -> {
            // Check passed: validationType is safe
        }
        is SecurityCheck.Flagged -> {
            // Threat detected: securityState.message contains details
        }
        is SecurityCheck.Error -> {
            // Something went wrong during the check
        }
    }
}
```



## Validation Types
The library covers the following ValidationType enums:
```
App: AppSignature, AppPackageName
Root: Root, RootAppsInstalled
Hardware: Emulator
Settings: DeveloperModeEnabled, AdbEnabled, PackageVerifierDisabled
Environment: VirtualizationInstalledApps, InstallationDir, VirtualLibraryPresent
```
## Tech Stack
Language: Kotlin

Native Implementation: C (via JNI) for robust root and virtualization detection.

Data Persistence: Jetpack DataStore (Protobuf) for configuration.

Architecture: Clean architecture with specific IntegrityChecker modules.