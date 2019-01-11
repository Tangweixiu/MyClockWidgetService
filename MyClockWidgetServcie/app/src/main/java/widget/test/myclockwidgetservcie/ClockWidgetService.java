package widget.test.myclockwidgetservcie;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ClockWidgetService extends Service {

    private static final String TAG = "ClockWidgetService";

    private final String ACTION_UPDATE_ALL = "widget.test.myclockwidgetservcie.UPDATE_TIME";

    private static final int UPDATE_TIME = 1000;


    private Timer timer;

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private Bitmap bitmap;

    public ClockWidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "--onCreate--");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                upData();
                Log.i(TAG, "timer run: ");
            }
        }, 0, 1000);

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "--onDestroy--");
        timer = null;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "--onStartCommand--");
        return super.onStartCommand(intent, flags, startId);
    }

    private Bitmap createViewBitmap(View v){
        Bitmap ClockBitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ClockBitmap);
        v.draw(canvas);
        return ClockBitmap;
    }

    private void upData() {
        String widgetText = sdf.format(new Date());
        Log.i(TAG, "upData: time"+ widgetText);
        Context mContext = getApplicationContext();
        ClockView clockView = new ClockView(mContext);
        bitmap = (Bitmap)createViewBitmap(clockView);
        String path = saveBitmapToPic(this, "customview.png", bitmap);
        bitmap.recycle();
        File newFile = new File(path);
        Uri imageUri = CustomViewFileProvider.getUriForFile(this, CustomViewFileProvider.AUTHORITIES, newFile);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.clock_widget);
        views.setTextViewText(R.id.ditialTimer, widgetText);
        views.setImageViewUri(R.id.analogClock, imageUri);
        AppWidgetManager manager = AppWidgetManager.getInstance(mContext);
        ComponentName componentName = new ComponentName(mContext, ClockWidget.class);
        // Instruct the widget manager to update the widget
        manager.updateAppWidget(componentName, views);
    }

    private String saveBitmapToPic(Context context, String name, Bitmap b) {
        if (b == null) {
            return null;
        }
        context.deleteFile(name);
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(name, Context.MODE_PRIVATE);
            //fos = new FileOutputStream(screenShotFile,Context.MODE_WORLD_READABLE);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
                return context.getFilesDir() + "/" + name;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
