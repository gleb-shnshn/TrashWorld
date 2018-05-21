package glebshanshin.trashworld;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class SettingsActivity extends Activity {
    MediaPlayer menuPlayer;
    DBHelper dbHelper;
    SQLiteDatabase db;
    boolean notIntent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.settings_main);
        menuPlayer = MediaPlayer.create(this, R.raw.menu);
        menuPlayer.start();
        menuPlayer.setLooping(true);
    }

    public void statistics(View view) {
        Intent intent = new Intent(SettingsActivity.this, StatisticsActivity.class);
        startActivity(intent);
        finish1();
    }

    private void finish1() {
        menuPlayer.stop();
        menuPlayer = MediaPlayer.create(this, R.raw.click);
        menuPlayer.setVolume(0.4f, 0.4f);
        menuPlayer.setLooping(false);
        menuPlayer.start();
        finish();
    }

    public void toMenu(View view) {
        if (notIntent) {
            notIntent = false;
            Intent intent1 = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent1);
            finish1();
        }
    }

    public void promo(View view) {
        if (notIntent) {
            notIntent = false;
            Intent intent1 = new Intent(SettingsActivity.this, PromoActivity.class);
            startActivity(intent1);
            finish1();
        }
    }

    public void music(View view) {
        if (notIntent) {
            notIntent = false;
            Intent intent1 = new Intent(SettingsActivity.this, MusicActivity.class);
            startActivity(intent1);
            finish1();
        }
    }
}