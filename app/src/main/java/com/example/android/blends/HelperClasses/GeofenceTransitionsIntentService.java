package com.example.android.blends.HelperClasses;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.blends.Activities.MainActivity;
import com.example.android.blends.Activities.MapActivity;
import com.example.android.blends.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService {
    private static final String TAG = GeofenceTransitionsIntentService.class.getSimpleName();

    public GeofenceTransitionsIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this, event.getErrorCode());
            Log.e(TAG, errorMessage);
        }

        // get transition type
        int geofenceTransition = event.getGeofenceTransition();

        // test that the reported transition was of interest
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // get the geofences that were triggered 
            List<Geofence> triggeringGeofences = event.getTriggeringGeofences();

            // get transition details as a String
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences);

            // send notification and log the transition details
            sendNotification(geofenceTransitionDetails);
        } else {
            Log.e(TAG, getString(R.string.geofence_transition_invalid_type));
        }
    }

    private String getGeofenceTransitionDetails(Context context,
                                                int geofenceTransition,
                                                List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // get IDs for each geofence that was triggered
        ArrayList triggeringGeofencesIdList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(",", triggeringGeofencesIdList);
        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }

    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.geofence_transition_exited);
            default:
                return getString(R.string.unknown_geofence_transition);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(String notificationDetails) {
        Intent notificationIntent = new Intent(getApplicationContext(), MapActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_cup_coffee)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_cup_coffee))
                .setColor(Color.MAGENTA)
                .setContentTitle(notificationDetails)
                .setContentText(getString(R.string.geofence_transition_notif_text))
                .setContentIntent(notificationPendingIntent);

        builder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context
                .NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());
    }

}

