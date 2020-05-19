package com.ipk.mobilodev;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class ActivityIntentService extends IntentService {

    public ActivityIntentService() {
        super(ActivityIntentService.class.getSimpleName());
    }

    protected static final String TAG = ActivityIntentService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        // Log.d(TAG,TAG + "onCreate()");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,TAG + "onHandleIntent()");
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

        // Get the list of the probable activities associated with the current state of the
        // device. Each activity is associated with a confidence level, which is an int between
        // 0 and 100.

        List<DetectedActivity> detectedActivities = result.getProbableActivities();

        for (DetectedActivity activity : detectedActivities) {
            //Log.d(TAG, "Detected activity: " + activity.getType() + ", " + activity.getConfidence());
            broadcastActivity(activity);
        }
    }

    private void broadcastActivity(DetectedActivity activity) {
        // Log.d(TAG,TAG+ "broadcastActivity()");
        Intent intent = new Intent("activity_intent");
        intent.putExtra("type", activity.getType());
        intent.putExtra("confidence", activity.getConfidence());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}

