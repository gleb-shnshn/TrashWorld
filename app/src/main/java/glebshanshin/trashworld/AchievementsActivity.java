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

public class AchievementsActivity extends Activity {
    ImageView mistakes, mistakeb, mistakeg, carb, factoryb, glassb, manb, metalb, notrecycleb, organicb, paperb, plasticb, robotb, trashb, carg, factoryg, glassg, mang, metalg, notrecycleg, organicg, paperg, plasticg, robotg, trashg, cars, factorys, glasss, mans, metals, notrecycles, organics, papers, plastics, robots, trashs;
    int factory, robot, car, man;
    int organicc, plasticc, metalc, glassc, notrecyclec, paperc, mistake, trash;
    DBHelper dbHelper;
    Cursor cursor;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.achievements_main);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        init(db);
        init1();
        fillall();
    }

    public void init(SQLiteDatabase db) {
        cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        man = cursor.getInt(2);
        car = cursor.getInt(3);
        robot = cursor.getInt(4);
        factory = cursor.getInt(5);
        paperc = cursor.getInt(6);
        plasticc = cursor.getInt(7);
        metalc = cursor.getInt(8);
        organicc = cursor.getInt(9);
        notrecyclec = cursor.getInt(10);
        glassc = cursor.getInt(11);
        mistake = cursor.getInt(12);
        cursor.close();
        trash = plasticc + paperc + metalc + organicc + notrecyclec + glassc;
    }

    private void fillall() {
        if (man >= 1) {
            manb.setImageDrawable(getDrawable(R.drawable.manb));
            if (man >= 10) {
                mans.setImageDrawable(getDrawable(R.drawable.mans));
                if (man >= 100)
                    mang.setImageDrawable(getDrawable(R.drawable.mang));
            }
        }
        if (car >= 1) {
            carb.setImageDrawable(getDrawable(R.drawable.carb));
            if (car >= 10) {
                cars.setImageDrawable(getDrawable(R.drawable.cars));
                if (car >= 100)
                    carg.setImageDrawable(getDrawable(R.drawable.carg));
            }
        }
        if (robot >= 1) {
            robotb.setImageDrawable(getDrawable(R.drawable.robotb));
            if (robot >= 10) {
                robots.setImageDrawable(getDrawable(R.drawable.robots));
                if (robot >= 100)
                    robotg.setImageDrawable(getDrawable(R.drawable.robotg));
            }
        }
        if (factory >= 1) {
            factoryb.setImageDrawable(getDrawable(R.drawable.factoryb));
            if (factory >= 10) {
                factorys.setImageDrawable(getDrawable(R.drawable.factorys));
                if (factory >= 100)
                    factoryg.setImageDrawable(getDrawable(R.drawable.factoryg));
            }
        }
        if (paperc >= 1) {
            paperb.setImageDrawable(getDrawable(R.drawable.paperb));
            if (paperc >= 10) {
                papers.setImageDrawable(getDrawable(R.drawable.papers));
                if (paperc >= 100)
                    paperg.setImageDrawable(getDrawable(R.drawable.paperg));
            }
        }
        if (glassc >= 1) {
            glassb.setImageDrawable(getDrawable(R.drawable.glassb));
            if (glassc >= 10) {
                glasss.setImageDrawable(getDrawable(R.drawable.glasss));
                if (glassc >= 100)
                    glassg.setImageDrawable(getDrawable(R.drawable.glassg));
            }
        }
        if (metalc >= 1) {
            metalb.setImageDrawable(getDrawable(R.drawable.metalb));
            if (metalc >= 10) {
                metals.setImageDrawable(getDrawable(R.drawable.metals));
                if (metalc >= 100)
                    metalg.setImageDrawable(getDrawable(R.drawable.metalg));
            }
        }
        if (notrecyclec >= 1) {
            notrecycleb.setImageDrawable(getDrawable(R.drawable.notrecycleb));
            if (notrecyclec >= 10) {
                notrecycles.setImageDrawable(getDrawable(R.drawable.notrecycles));
                if (notrecyclec >= 100)
                    notrecycleg.setImageDrawable(getDrawable(R.drawable.notrecycleg));
            }
        }
        if (organicc >= 1) {
            organicb.setImageDrawable(getDrawable(R.drawable.organicb));
            if (organicc >= 10) {
                organics.setImageDrawable(getDrawable(R.drawable.organics));
                if (organicc >= 100)
                    organicg.setImageDrawable(getDrawable(R.drawable.organicg));
            }
        }
        if (plasticc >= 1) {
            plasticb.setImageDrawable(getDrawable(R.drawable.plasticb));
            if (plasticc >= 10) {
                plastics.setImageDrawable(getDrawable(R.drawable.plastics));
                if (plasticc >= 100)
                    plasticg.setImageDrawable(getDrawable(R.drawable.plasticg));
            }
        }
        if (trash >= 1) {
            trashb.setImageDrawable(getDrawable(R.drawable.trashb));
            if (trash >= 10) {
                trashs.setImageDrawable(getDrawable(R.drawable.trashs));
                if (trash >= 100)
                    trashg.setImageDrawable(getDrawable(R.drawable.trashg));
            }
        }
        if (mistake >= 1) {
            mistakeb.setImageDrawable(getDrawable(R.drawable.mistakeb));
            if (mistake >= 10) {
                mistakes.setImageDrawable(getDrawable(R.drawable.mistakes));
                if (mistake >= 100)
                    mistakeg.setImageDrawable(getDrawable(R.drawable.mistakeg));
            }
        }
    }

    private void init1() {
        carb = findViewById(R.id.carb);
        factoryb = findViewById(R.id.factoryb);
        glassb = findViewById(R.id.glassb);
        manb = findViewById(R.id.manb);
        metalb = findViewById(R.id.metalb);
        notrecycleb = findViewById(R.id.notrecycleb);
        organicb = findViewById(R.id.organicb);
        paperb = findViewById(R.id.paperb);
        plasticb = findViewById(R.id.plasticb);
        robotb = findViewById(R.id.robotb);
        trashb = findViewById(R.id.trashb);
        carg = findViewById(R.id.carg);
        factoryg = findViewById(R.id.factoryg);
        glassg = findViewById(R.id.glassg);
        mang = findViewById(R.id.mang);
        metalg = findViewById(R.id.metalg);
        notrecycleg = findViewById(R.id.notrecycleg);
        organicg = findViewById(R.id.organicg);
        paperg = findViewById(R.id.paperg);
        plasticg = findViewById(R.id.plasticg);
        robotg = findViewById(R.id.robotg);
        trashg = findViewById(R.id.trashg);
        cars = findViewById(R.id.cars);
        factorys = findViewById(R.id.factorys);
        glasss = findViewById(R.id.glasss);
        mans = findViewById(R.id.mans);
        metals = findViewById(R.id.metals);
        notrecycles = findViewById(R.id.notrecycles);
        organics = findViewById(R.id.organics);
        papers = findViewById(R.id.papers);
        plastics = findViewById(R.id.plastics);
        robots = findViewById(R.id.robots);
        trashs = findViewById(R.id.trashs);
        mistakes = findViewById(R.id.mistakes);
        mistakeb = findViewById(R.id.mistakeb);
        mistakeg = findViewById(R.id.mistakeg);
    }

    public void toMenu(View view) {
        Intent intent1 = new Intent(AchievementsActivity.this, MainActivity.class);
        startActivity(intent1);
        finish();
    }

    public void toBonus(View view) {
        Intent intent1 = new Intent(AchievementsActivity.this, BonusActivity.class);
        startActivity(intent1);
        finish();
    }
}