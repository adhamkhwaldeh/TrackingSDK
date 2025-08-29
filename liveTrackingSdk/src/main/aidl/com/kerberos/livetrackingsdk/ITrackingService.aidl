// ITrackingService.aidl
package com.kerberos.livetrackingsdk;

// Declare any non-default types here with import statements

interface ITrackingService {

    boolean startTracking();

    boolean pauseTracking();

    boolean resumeTracking();

    boolean stopTracking();

}