package glebshanshin.trashworld;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class UniActivity extends Activity {
    long factory, robot, car, man, TSH;
    int organicc, plasticc, metalc, glassc, notrecyclec, paperc, mistake, trash, multi;
    int paperb, plasticb, metalb, organicb, notrecycleb, glassb;
    float music, effects, scale;
    String code1, code2;
    SQLiteDatabase db;
    DBHelper dbHelper;
    Cursor cursor;
    MediaPlayer menuPlayer, clickPlayer;
    boolean notIntent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        init(db);
        clickPlayer = MediaPlayer.create(this, R.raw.click);
        clickPlayer.setVolume(effects, effects);
    }

    public void init(SQLiteDatabase db) {
        cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        TSH = cursor.getLong(1);
        man = cursor.getLong(2);
        car = cursor.getLong(3);
        robot = cursor.getLong(4);
        factory = cursor.getLong(5);
        paperc = cursor.getInt(6);
        plasticc = cursor.getInt(7);
        metalc = cursor.getInt(8);
        organicc = cursor.getInt(9);
        notrecyclec = cursor.getInt(10);
        glassc = cursor.getInt(11);
        trash = plasticc + paperc + metalc + organicc + notrecyclec + glassc;
        mistake = cursor.getInt(12);
        paperb = cursor.getInt(13);
        plasticb = cursor.getInt(14);
        metalb = cursor.getInt(15);
        organicb = cursor.getInt(16);
        notrecycleb = cursor.getInt(17);
        glassb = cursor.getInt(18);
        multi = cursor.getInt(19);
        code1 = cursor.getString(20);
        code2 = cursor.getString(21);
        music = cursor.getFloat(22);
        effects = cursor.getFloat(23);
        cursor.close();
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
        menuPlayer.setVolume(music, music);
        menuPlayer.setLooping(true);
        menuPlayer.start();
    }

    public void finish1() { //отключение музыки при выходе из активности
        menuPlayer.stop();
        clickPlayer.start();
        finish();
    }

    public void transfer(Class class1) {
        if (notIntent) {
            notIntent = false;
            Intent intent = new Intent(this, class1);
            startActivity(intent);
            finish1();
        }
    }

    public String getPrice(long s) {//масштабирование цены
        String newa = "" + s;
        if (newa.length() > 12)
            newa = newa.substring(0, newa.length() - 12) + "T";
        else if (newa.length() > 9)
            newa = newa.substring(0, newa.length() - 9) + "B";
        else if (newa.length() > 6)
            newa = newa.substring(0, newa.length() - 6) + "M";
        else if (newa.length() > 3)
            newa = newa.substring(0, newa.length() - 3) + "K";

        return newa;
    }
}
