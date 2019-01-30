package com.example.android.blends.WidgetData;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.blends.Activities.MainActivity;
import com.example.android.blends.R;

/**
 * Implementation of App Widget functionality.
 */
public class CafeAppWidget extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId, String widgetCafeName, String
                                                widgetCafeAddress,
                                        String widgetCafeNumber) {

        CharSequence widgetText = context.getString(R.string.widget_title);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cafe_app_widget);
        views.setTextViewText(R.id.widget_title, widgetText);

        // onclick pending intent
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_cafe_logo, pendingIntent);

        // intent for updating widget
        Intent updateWidgetIntent = new Intent(context, WidgetCafeAppService.class);
        updateWidgetIntent.setAction(WidgetCafeAppService.ACTION_UPDATE_WIDGET);
        PendingIntent updatePendingIntent = PendingIntent.getService(context, 0,
                updateWidgetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_title, updatePendingIntent);

        //Set text for widget view
        if (widgetCafeName != null) {
            views.setTextViewText(R.id.widget_cafe_name, widgetCafeName);
        }
        if (widgetCafeAddress != null) {
            views.setTextViewText(R.id.widget_cafe_address, widgetCafeAddress);
        }
        if (widgetCafeNumber != null) {
            views.setTextViewText(R.id.widget_cafe_number, widgetCafeNumber);
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetCafeAppService.startUpdatingWidget(context);
    }

    public static void updateCafeWidget(Context context, AppWidgetManager appWidgetManager, int[]
            appWidgetIds, String widgetCafeName, String widgetCafeAddress, String
                                                widgetCafeNumber) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, widgetCafeName,
                    widgetCafeAddress, widgetCafeNumber);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

