package com.example.android.blends.WidgetData;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.example.android.blends.Data.PlacesContract;

import java.util.Random;

public class WidgetCafeAppService extends IntentService {

    public final static String ACTION_UPDATE_WIDGET = "com.example.android.blends.WidgetData" +
            ".action.update_widget";
    private static final String TAG = "WidgetCafeAppService";

    public WidgetCafeAppService() {
        super("WidgetCafeAppService");
    }

    public static void startUpdatingWidget(Context context) {
        Intent intent = new Intent(context, WidgetCafeAppService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                updateWidget();
            }
        }
    }

    @SuppressLint({"Recycle", "NewApi"})
    private void updateWidget() {

        String[] projection = new String[]{PlacesContract.PlacesEntry.COLUMN_PLACE_NAME,
                PlacesContract.PlacesEntry.COLUMN_PLACE_ADDRESS,
                PlacesContract.PlacesEntry.COLUMN_PLACE_NUMBER};

        Log.e(TAG, "updateWidget");
        Cursor cursor = getContentResolver().query(PlacesContract.PlacesEntry.PLACES_CONTENT_URI,
                projection,
                null, null, null, null);
        Random random = new Random();

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToPosition(random.nextInt(cursor.getCount()));
            String widgetCafeName = cursor.getString(cursor.getColumnIndex(PlacesContract
                    .PlacesEntry.COLUMN_PLACE_NAME));
            String widgetCafeAddress = cursor.getString(cursor.getColumnIndex(PlacesContract
                    .PlacesEntry.COLUMN_PLACE_ADDRESS));
            String widgetCafeNumber = cursor.getString(cursor.getColumnIndex(PlacesContract
                    .PlacesEntry.COLUMN_PLACE_NUMBER));

            cursor.close();

            Log.e(TAG, "updateWidget: "+widgetCafeName + " "+ widgetCafeAddress + " " + widgetCafeNumber );
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                    CafeAppWidget.class));
            CafeAppWidget.updateCafeWidget(this, appWidgetManager, appWidgetIds, widgetCafeName,
                    widgetCafeAddress, widgetCafeNumber);
        }
    }
}
