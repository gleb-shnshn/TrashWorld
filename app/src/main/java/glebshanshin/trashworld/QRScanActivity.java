package glebshanshin.trashworld;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.view.View;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class QRScanActivity extends CaptureActivity {
    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.qrscan_main);
        return findViewById(R.id.place);
    }

    public void toPromo(View view) {
        finish1();
    }

    private void finish1() {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        float effects = cursor.getFloat(23);
        cursor.close();
        MediaPlayer menuPlayer = MediaPlayer.create(this, R.raw.click);
        menuPlayer.setVolume(effects, effects);
        menuPlayer.setLooping(false);
        menuPlayer.start();
        finish();
    }

    @Override
    public void onBackPressed() {
        finish1();
        super.onBackPressed();
    }
}
