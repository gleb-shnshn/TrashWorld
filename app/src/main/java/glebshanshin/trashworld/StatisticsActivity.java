package glebshanshin.trashworld;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class StatisticsActivity extends Activity {
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    TextView organic, plastic, metal, glass, notrecycle, paper, mistake, trash, TSH;
    long organicc, plasticc, metalc, glassc, notrecyclec, paperc, mistakes, trashc, man, car, robot, factory;
    long TSHc;
    String pref = "\n:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.statistics_main);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        init(db);
        init1();
        fillall();
    }

    private void fillall() {
        glass.setText(pref + glassc);
        organic.setText(pref + organicc);
        paper.setText(pref + paperc);
        trash.setText(pref + trashc);
        notrecycle.setText(pref + notrecyclec);
        mistake.setText(pref + mistakes);
        metal.setText(pref + metalc);
        plastic.setText(pref + plasticc);
        String newa = "" + TSHc;
        if (newa.length() > 12)
            newa = newa.substring(0, newa.length() - 9) + "T";
        else if (newa.length() > 9)
            newa = newa.substring(0, newa.length() - 9) + "B";
        else if (newa.length() > 6)
            newa = newa.substring(0, newa.length() - 6) + "M";
        else if (newa.length() > 3)
            newa = newa.substring(0, newa.length() - 3) + "K";

        TSH.setText(newa + " TSH ");
    }

    private void init1() {
        glass = findViewById(R.id.glass);
        metal = findViewById(R.id.metal);
        notrecycle = findViewById(R.id.notrecycle);
        organic = findViewById(R.id.organic);
        paper = findViewById(R.id.paper);
        plastic = findViewById(R.id.plastic);
        trash = findViewById(R.id.trash);
        mistake = findViewById(R.id.mistake);
        TSH = findViewById(R.id.TSH);
    }

    public void init(SQLiteDatabase db) {
        cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        TSHc = cursor.getLong(1);
        man = cursor.getLong(2);
        car = cursor.getLong(3);
        robot = cursor.getLong(4);
        factory = cursor.getLong(5);
        TSHc = TSHc + ((2 + (man - 1)) * man / 2) + ((20 + 10 * (car - 1)) * car / 2) + ((100 + 50 * (robot - 1)) * robot / 2) + ((200 + 100 * (factory - 1)) * factory / 2);
        paperc = Integer.parseInt(cursor.getString(6));
        plasticc = Integer.parseInt(cursor.getString(7));
        metalc = Integer.parseInt(cursor.getString(8));
        organicc = Integer.parseInt(cursor.getString(9));
        notrecyclec = Integer.parseInt(cursor.getString(10));
        glassc = Integer.parseInt(cursor.getString(11));
        mistakes = Integer.parseInt(cursor.getString(12));
        trashc = plasticc + paperc + metalc + organicc + notrecyclec + glassc;
        cursor.close();
    }

    public void toSettings(View view) {
        Intent intent = new Intent(StatisticsActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }
}