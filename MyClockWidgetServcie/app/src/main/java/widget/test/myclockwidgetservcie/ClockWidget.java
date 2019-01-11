package widget.test.myclockwidgetservcie;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static android.graphics.Bitmap.createBitmap;

/**
 * Implementation of App Widget functionality.
 */
public class ClockWidget extends AppWidgetProvider {
    private static final String TAG = "ClockWidget";
    static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    private final Intent TIME_SERVICE_INTENT =
            new Intent("android.appwidget.action.MY_APP_WIDGET_SERVICE");

    private final String ACTION_UPDATE_ALL = "widget.test.myclockwidgetservcie.UPDATE_TIME";

    Context mContext;
    private Timer timer;
    private AppWidgetManager mappWidgetManager;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String widgetText = sdf.format(new Date());
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_widget);
        views.setTextViewText(R.id.ditialTimer, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "--onReceive--");

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.i(TAG, "--onEnabled--");

        super.onEnabled(context);

        Intent mTimerIntent = new Intent(context, ClockWidgetService.class);
        context.startService(mTimerIntent);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled

        super.onDisabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        context.stopService(new Intent(context, ClockWidgetService.class));
        Log.i(TAG, "onDeleted: Success");
    }
}

