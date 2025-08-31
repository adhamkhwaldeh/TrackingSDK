# Module LiveTrackingSdk
# Integration Guide: LiveTrackingSDK

## Table of Contents
1. [Introduction](#introduction)
2. [Prerequisites](#prerequisites)
3. [Setup Instructions](#setup-instructions)
    1. [Step 1: Add Gradle Dependencies](#step-1-add-gradle-dependencies)
    2. [Step 2: Add Permissions to AndroidManifest.xml](#step-2-add-permissions-to-androidmanifestxml)
    3. [Step 3: Initialize the SDK](#step-3-initialize-the-sdk)
4. [Usage](#usage)
    1. [Listeners](#listeners)
    2. [Actions](#actions)
5. [API Reference](#api-reference)
6. [Troubleshooting](#troubleshooting)

---

## Introduction

This guide provides step-by-step instructions for integrating the `LiveTrackingSDK` into your Android project. This SDK allows you to easily track user location in both foreground and background modes.

---

## Prerequisites

Before you begin the integration process, make sure you have the following:

- An Android project with a minimum SDK version of 21.
- The following permissions declared in your `AndroidManifest.xml`:
    - `android.permission.ACCESS_FINE_LOCATION`
    - `android.permission.ACCESS_COARSE_LOCATION`
    - `android.permission.POST_NOTIFICATIONS` (for Android 13 and above)

---

## Setup Instructions

### Step 1: Add Gradle Dependencies

Add the `liveTrackingSdk` module as a dependency in your app's `build.gradle.kts` file:

```gradle
dependencies {
    implementation(project(":liveTrackingSdk"))
}
```

### Step 2: Add Permissions to AndroidManifest.xml

Add the following permissions to your `app/src/main/AndroidManifest.xml` file:

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

### Step 3: Initialize the SDK

Initialize the `LiveTrackingManager` in your Application class or a central location in your app. Use the `Builder` to configure the SDK according to your needs.

```kotlin
import com.kerberos.livetrackingsdk.LiveTrackingManager
import com.kerberos.livetrackingsdk.enums.LiveTrackingMode
import com.kerberos.livetrackingsdk.models.DefaultNotificationConfiguration

// ...

val liveTrackingManager = LiveTrackingManager.Builder(applicationContext)
    .setLiveTrackingMode(LiveTrackingMode.FOREGROUND_SERVICE)
    .setMinDistanceMeters(10f)
    .setLocationUpdateInterval(5000) // 5 seconds
    .setBackgroundTrackingToggle(true)
    .build()

// To start the foreground service, you need to provide a notification configuration
val notificationConfig = DefaultNotificationConfiguration(
    defaultIntentActivity = YourMainActivity::class.java,
    contentTitle = "Live Tracking Active",
    contentText = "Your location is being tracked.",
    smallIcon = R.drawable.ic_notification,
    ticker = "Live Tracking"
)
// This configuration is used internally by the SDK when starting the foreground service.
// You do not need to call this yourself, just ensure the configuration is available.
```

---

## Usage

### Listeners

You can add listeners to receive updates on location and tracking status.

#### ITrackingLocationListener

Implement `ITrackingLocationListener` to receive location updates.

```kotlin
val locationListener = object : ITrackingLocationListener {
    override fun onLocationChanged(location: Location) {
        // Handle new location
    }
}

liveTrackingManager.addTrackingLocationListener(locationListener)
```

#### ITrackingStatusListener

Implement `ITrackingStatusListener` to receive updates on the tracking state.

```kotlin
val statusListener = object : ITrackingStatusListener {
    override fun onTrackingStateChanged(state: TrackingState) {
        // Handle tracking state changes (e.g., STARTED, PAUSED, STOPPED)
    }
}

liveTrackingManager.addTrackingStatusListener(statusListener)
```

### Actions

Control the tracking process using the following actions:

- `onStartTracking()`: Starts the tracking service.
- `onResumeTracking()`: Resumes tracking if it was paused.
- `onPauseTracking()`: Pauses the tracking service.
- `onStopTracking()`: Stops the tracking service.

```kotlin
// Example: Start tracking
liveTrackingManager.onStartTracking()
```

---

## API Reference

- **`LiveTrackingManager`**: The main class for interacting with the SDK.
- **`LiveTrackingManager.Builder`**: The builder class for initializing the SDK.
- **`ITrackingLocationListener`**: Interface for receiving location updates.
- **`ITrackingStatusListener`**: Interface for receiving tracking status updates.
- **`LiveTrackingMode`**: Enum for setting the tracking mode (`FOREGROUND_SERVICE` or `BACKGROUND_SERVICE`).
- **`DefaultNotificationConfiguration`**: Data class for configuring the foreground service notification.

---

## Troubleshooting

- **Permissions not granted:** Ensure you have requested the necessary location permissions from the user at runtime.
- **GPS not enabled:** The SDK may throw a `GpsNotEnabledException` if the user's GPS is turned off. Prompt the user to enable it.
- **Foreground service not starting:** Make sure you have provided a valid `DefaultNotificationConfiguration`.
