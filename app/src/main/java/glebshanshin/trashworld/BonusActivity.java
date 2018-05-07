package glebshanshin.trashworld;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class BonusActivity extends Activity {
    DBHelper dbHelper;
    Cursor cursor;
    SQLiteDatabase db;
    ImageView plastic, glass, paper, notrecycle, metal, organic;
    int organicc, plasticc, metalc, glassc, notrecyclec, paperc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.bonus_main);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        init(db);
    }

    private void init1() {
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
    public void init(SQLiteDatabase db) {
        cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        paperc = Integer.parseInt(cursor.getString(13));
        plasticc = Integer.parseInt(cursor.getString(14));
        metalc = Integer.parseInt(cursor.getString(15));
        organicc = Integer.parseInt(cursor.getString(16));
        notrecyclec = Integer.parseInt(cursor.getString(17));
        glassc = Integer.parseInt(cursor.getString(18));
        cursor.close();
        init1();
    }
    public void toPlay(View view) {
        Intent intent = new Intent(BonusActivity.this, PlayActivity.class);
        startActivity(intent);
        finish();
    }
}
