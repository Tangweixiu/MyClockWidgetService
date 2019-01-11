package widget.test.myclockwidgetservcie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.FakeClock);
        ClockView clockView = new ClockView(MainActivity.this);
        Bitmap bitmap = (Bitmap)createViewBitmap(clockView);
        String path = saveBitmapToPic(this, "customview.png", bitmap);
        bitmap.recycle();
        File newFile = new File(path);
        Uri imageUri = CustomViewFileProvider.getUriForFile(this, CustomViewFileProvider.AUTHORITIES, newFile);
        img.setImageURI(imageUri);
    }
    private Bitmap createViewBitmap(View v){
        Bitmap ClockBitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ClockBitmap);
        v.draw(canvas);
        return ClockBitmap;
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
