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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class BonusActivity extends Activity {
    DBHelper dbHelper;
    Cursor cursor;
    SQLiteDatabase db;
    ImageView plastic, glass, paper, notrecycle, metal, organic;
    Button reloadt;
    int organicc, plasticc, metalc, glassc, notrecyclec, paperc, multi;
    boolean flag = false;
    boolean notIntent = true;
    float music, effects;
    MediaPlayer menuPlayer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.bonus_main);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        init(db);
        if (organicc + plasticc + metalc + glassc + notrecyclec + paperc == 18) {//проверка 3 уровня у всех
            flag = true;
            StyleableToast.makeText(this, "Вы можете обнулить бонусы и тогда весь ваш TSH/мусор будет умножаться на " + multi * 3, R.style.Clear).show();
            reloadt = findViewById(R.id.reload);
            reloadt.setBackgroundDrawable(getDrawable(R.drawable.reload));
        }
    }

    private void init1() {//инициализация ImageView и установка картинок
        plastic = findViewById(R.id.plastic);
        glass = findViewById(R.id.glass);
        paper = findViewById(R.id.paper);
        notrecycle = findViewById(R.id.notrecycle);
        metal = findViewById(R.id.metal);
        organic = findViewById(R.id.organic);
        plastic.setImageDrawable(getDrawable(getResources().getIdentifier("plastic" + plasticc, "drawable", getPackageName())));
        glass.setImageDrawable(getDrawable(getResources().getIdentifier("glass" + glassc, "drawable", getPackageName())));
        paper.setImageDrawable(getDrawable(getResources().getIdentifier("paper" + paperc, "drawable", getPackageName())));
        notrecycle.setImageDrawable(getDrawable(getResources().getIdentifier("notrecycle" + notrecyclec, "drawable", getPackageName())));
        metal.setImageDrawable(getDrawable(getResources().getIdentifier("metal" + metalc, "drawable", getPackageName())));
        organic.setImageDrawable(getDrawable(getResources().getIdentifier("organic" + organicc, "drawable", getPackageName())));
    }

    public void init(SQLiteDatabase db) {//получение данных из базы данных
        cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        paperc = cursor.getInt(13);
        plasticc = cursor.getInt(14);
        metalc = cursor.getInt(15);
        organicc = cursor.getInt(16);
        notrecyclec = cursor.getInt(17);
        glassc = cursor.getInt(18);
        multi = cursor.getInt(19);
        music = cursor.getFloat(22);
        effects = cursor.getFloat(23);
        cursor.close();
        init1();
    }

    private void finish1() {//отключение музыки при выходе из активности
        menuPlayer.stop();
        menuPlayer = MediaPlayer.create(this, R.raw.click);
        menuPlayer.setVolume(effects, effects);
        menuPlayer.setLooping(false);
        menuPlayer.start();
        finish();
    }

    public void toAchievements(View view) {//переход в класс Достижений
        if (notIntent) {
            notIntent = false;
            Intent intent = new Intent(BonusActivity.this, AchievementsActivity.class);
            startActivity(intent);
            finish1();
        }
    }

    public void reload(View view) {//кнопка обнуления бонусов при достижении 3 уровня у всех бонусов
        if (flag) {
            ContentValues newValues = new ContentValues();
            newValues.put("paperb", 1);
            newValues.put("plasticb", 1);
            newValues.put("metalb", 1);
            newValues.put("organicb", 1);
            newValues.put("notrecycleb", 1);
            newValues.put("glassb", 1);
            newValues.put("multi", multi * 3);
            db.update("Data", newValues, "_id = 1", null);
            init(db);
            StyleableToast.makeText(this, "✓  Бонусы успешно обнулены", R.style.Clear).show();
            reloadt.setAlpha(0);
            flag = false;
        }
    }

    //включение и отключение музыки при выключении и выключении приложения
    @Override
    protected void onStop() {
        super.onStop();
        menuPlayer.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        menuPlayer = MediaPlayer.create(this, R.raw.menu);
        menuPlayer.setVolume(music, music);
        menuPlayer.setLooping(true);
        menuPlayer.start();
    }

    //выход в достижения через встроенную кнопку назад
    @Override
    public void onBackPressed() {
        if (notIntent) {
            notIntent = false;
            Intent intent = new Intent(BonusActivity.this, AchievementsActivity.class);
            startActivity(intent);
            finish1();
        }
        super.onBackPressed();
    }
}