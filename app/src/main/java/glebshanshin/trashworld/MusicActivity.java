package glebshanshin.trashworld;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class MusicActivity extends Activity {
    DiscreteSeekBar music, effects;
    float musics, effectes, scale;
    SQLiteDatabase db;
    DBHelper dbHelper;
    Cursor cursor;
    boolean notIntent = true;
    MediaPlayer menuPlayer, clickPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.music_main);
        dbHelper = new DBHelper(this);
        scale = 1 / getResources().getDisplayMetrics().density * 0.5f + getWindowManager().getDefaultDisplay().getHeight() * getWindowManager().getDefaultDisplay().getWidth() * 0.0000001f;
        db = dbHelper.getWritableDatabase();
        init();
    }

    private void finish1() {
        menuPlayer.stop();
        clickPlayer.setVolume(effectes, effectes);
        clickPlayer.start();
        finish();
    }

    private void init() {
        clickPlayer = MediaPlayer.create(this, R.raw.click);
        cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        musics = cursor.getFloat(22);
        effectes = cursor.getFloat(23);
        cursor.close();
        clickPlayer.setVolume(effectes, effectes);
        final TextView musicText = findViewById(R.id.music), effectsText = findViewById(R.id.effects);
        musicText.setTextSize(scale * 50f);
        effectsText.setTextSize(scale * 50f);
        music = findViewById(R.id.musicbar);
        music.setProgress((int) (musics * 100));
        musicText.setText("Музыка " + music.getProgress() + "%");
        music.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                musicText.setText("Музыка " + value + "%");
                menuPlayer.setVolume((float) value / 100, (float) value / 100);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                clickPlayer.start();
            }
        });
        effects = findViewById(R.id.effectsbar);
        effects.setProgress((int) (effectes * 100));
        effectsText.setText("Звуки " + effects.getProgress() + "%");
        effects.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                effectsText.setText("Звуки " + value + "%");
                clickPlayer.setVolume((float) value / 100, (float) value / 100);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                clickPlayer.start();
            }
        });
    }

    public void toSettings(View view) {
        if (notIntent) {
            notIntent = false;
            musics = (float) music.getProgress() / 100;
            effectes = (float) effects.getProgress() / 100;
            ContentValues newValues = new ContentValues();
            newValues.put("music", musics);
            newValues.put("effects", effectes);
            db.update("Data", newValues, "_id = 1", null);
            Intent intent = new Intent(MusicActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish1();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        menuPlayer.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        menuPlayer = MediaPlayer.create(this, R.raw.menu);
        menuPlayer.setVolume(musics, musics);
        menuPlayer.setLooping(true);
        menuPlayer.start();
    }

    @Override
    public void onBackPressed() {
        if (notIntent) {
            notIntent = false;
            Intent intent1 = new Intent(MusicActivity.this, SettingsActivity.class);
            startActivity(intent1);
            finish1();
        }
        super.onBackPressed();
    }
}
