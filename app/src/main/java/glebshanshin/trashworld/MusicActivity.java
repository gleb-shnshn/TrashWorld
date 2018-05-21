package glebshanshin.trashworld;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class MusicActivity extends Activity {
    DiscreteSeekBar music, effects;
    float musics, effectes;
    SQLiteDatabase db;
    DBHelper dbHelper;
    Cursor cursor;
    boolean notIntent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.music_main);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        init();
    }

    private void init() {
        cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        musics = cursor.getFloat(22);
        effectes = cursor.getFloat(23);
        cursor.close();
        final TextView musicText = findViewById(R.id.music), effectsText = findViewById(R.id.effects);
        music = findViewById(R.id.musicbar);
        music.setProgress((int) (musics * 100));
        musicText.setText("Музыка " + music.getProgress() + "%");
        music.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                musicText.setText("Музыка " + value + "%");
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
            }
        });
        effects = findViewById(R.id.effectsbar);
        effects.setProgress((int) (effectes * 100));
        effectsText.setText("Звуки " + effects.getProgress() + "%");
        effects.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                effectsText.setText("Звуки " + value + "%");
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
            }
        });
    }

    public void toSettings(View view) {
        if (notIntent) {
            notIntent = false;
            musics = (float) music.getProgress() / 100;
            effectes = (float) effects.getProgress() / 100;
            Toast.makeText(this, musics + "" + effectes, Toast.LENGTH_SHORT).show();
            ContentValues newValues = new ContentValues();
            newValues.put("music", musics);
            newValues.put("effects", effectes);
            db.update("Data", newValues, "_id = 1", null);
            Intent intent = new Intent(MusicActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
