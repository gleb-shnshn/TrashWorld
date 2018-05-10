package glebshanshin.trashworld;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class StoreActivity extends Activity {
    int factory, robot, car, man, TSH;
    TextView TSHv, priceman1, pricecar1, pricerobot1, pricefactory1,
            priceman10, pricecar10, pricerobot10, pricefactory10,
            priceman50, pricecar50, pricerobot50, pricefactory50,
            countman, countcar, countrobot, countfactory;
    SQLiteDatabase db;
    DBHelper dbHelper;
    Cursor cursor;
    String t = " TSH", tsh, s = " шт.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.store_main);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        TSHv = findViewById(R.id.TSH);
        init(db);
        updateTSH();
        updatePRICE("all");
    }

    private void updatePRICE(String what) {
        switch (what) {
            case "all":
                priceman1.setText(getPrice((man + 1)));
                pricecar1.setText(getPrice((car + 1) * 10));
                pricerobot1.setText(getPrice((robot + 1) * 50));
                pricefactory1.setText(getPrice((factory + 1) * 100));
                priceman10.setText(getPrice(get(10, (man + 1), 1)));
                pricecar10.setText(getPrice(get(10, (car + 1) * 10, 10)));
                pricerobot10.setText(getPrice(get(10,(robot + 1) * 50, 50)));
                pricefactory10.setText(getPrice(get(10, (factory + 1) * 100, 100)));
                priceman50.setText(getPrice(get(50, (man + 1), 1)));
                pricecar50.setText(getPrice(get(50, (car + 1) * 10, 10)));
                pricerobot50.setText(getPrice(get(50, (robot + 1) * 50, 50)));
                pricefactory50.setText(getPrice(get(50, (factory + 1) * 100, 100)));
                break;
            case "man":
                priceman1.setText(getPrice((man + 1)));
                priceman10.setText(getPrice(get(10, (man + 1), 1)));
                priceman50.setText(getPrice(get(50, (man + 1), 1)));
                break;
            case "car":
                pricecar1.setText(getPrice((car + 1) * 10));
                pricecar10.setText(getPrice(get(10, (car + 1) * 10, 10)));
                pricecar50.setText(getPrice(get(50, (car + 1) * 10, 10)));
                break;
            case "robot":
                pricerobot1.setText(getPrice((robot + 1) * 50));
                pricerobot10.setText(getPrice(get(10, (robot + 1) * 50, 50)));
                pricerobot50.setText(getPrice(get(50, (robot + 1) * 50, 50)));
                break;
            case "factory":
                pricefactory1.setText(getPrice((factory + 1) * 100));
                pricefactory10.setText(getPrice(get(10, (factory + 1) * 100, 100)));
                pricefactory50.setText(getPrice(get(50, (factory + 1) * 100, 100)));
                break;
        }
        switch (what) {
            case "all":
                countman.setText(man + s);
                countcar.setText(car + s);
                countrobot.setText(robot + s);
                countfactory.setText(factory + s);
                break;
            case "man":
                countman.setText(man + s);
                break;
            case "car":
                countcar.setText(car + s);
                break;
            case "robot":
                countrobot.setText(robot + s);
                break;
            case "factory":
                countfactory.setText(factory + s);
                break;

        }
    }

    public void toLotteryStore(View view) {
        update(db);
        Intent intent = new Intent(StoreActivity.this, LotteryStoreActivity.class);
        intent.putExtra("prize", "0");
        startActivity(intent);
        finish();
    }

    private int get(int n, int a1, int d) {
        return ((((2 * a1) + (n - 1) * d) * n)/ 2);
    }

    private String getPrice(int s) {
        String newa = "" + s;

        if (newa.length() > 12)
            newa = newa.substring(0, newa.length() - 9) + "T";
        else if (newa.length() > 9)
            newa = newa.substring(0, newa.length() - 9) + "B";
        else if (newa.length() > 6)
            newa = newa.substring(0, newa.length() - 6) + "M";
        else if (newa.length() > 3)
            newa = newa.substring(0, newa.length() - 3) + "K";

        return newa + t;
    }

    private void updateTSH() {
        String m = getPrice(TSH);
        TSHv.setText(m + " ");
    }

    private void update(SQLiteDatabase db) {
        ContentValues newValues = new ContentValues();
        newValues.put("TSH", TSH);
        newValues.put("man", man);
        newValues.put("car", car);
        newValues.put("robot", robot);
        newValues.put("factory", factory);
        db.update("Data", newValues, "_id = 1", null);
    }

    public void init(SQLiteDatabase db) {
        cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        TSH = Integer.parseInt(cursor.getString(1));
        man = Integer.parseInt(cursor.getString(2));
        car = Integer.parseInt(cursor.getString(3));
        robot = Integer.parseInt(cursor.getString(4));
        factory = Integer.parseInt(cursor.getString(5));
        cursor.close();
        priceman1 = findViewById(R.id.price1man);
        pricecar1 = findViewById(R.id.price1car);
        pricerobot1 = findViewById(R.id.price1robot);
        pricefactory1 = findViewById(R.id.price1factory);
        priceman10 = findViewById(R.id.price10man);
        pricecar10 = findViewById(R.id.price10car);
        pricerobot10 = findViewById(R.id.price10robot);
        pricefactory10 = findViewById(R.id.price10factory);
        priceman50 = findViewById(R.id.price50man);
        pricecar50 = findViewById(R.id.price50car);
        pricerobot50 = findViewById(R.id.price50robot);
        pricefactory50 = findViewById(R.id.price50factory);
        countman = findViewById(R.id.countman);
        countcar = findViewById(R.id.countcar);
        countrobot = findViewById(R.id.countrobot);
        countfactory = findViewById(R.id.countfactory);
    }

    private void toast(int a) {
        Toast.makeText(getApplicationContext(), "Не хватает "+a+" TSH", Toast.LENGTH_SHORT).show();
    }

    private void increase(int b, int i, String d) {
        int t = 0;
        switch (d) {
            case "man":
                t = 1;
                break;
            case "car":
                t = 10;
                break;
            case "robot":
                t = 50;
                break;
            case "factory":
                t = 100;
                break;
        }
        int money = get(i, (b+1) * t, t);
        if (money <= TSH) {
            TSH -= money;
            switch (d) {
                case "man":
                    man += i;
                    break;
                case "car":
                    car += i;
                    break;
                case "robot":
                    robot += i;
                    break;
                case "factory":
                    factory += i;
                    break;
            }
            Toast.makeText(this, "Снято " + money+" TSH", Toast.LENGTH_SHORT).show();
            updateTSH();
            updatePRICE(d);
            update(db);
        } else {
            toast(money-TSH);
        }
    }

    public void buyMan1(View view) {
        increase(man, 1, "man");
    }

    public void buyMan10(View view) {
        increase(man, 10, "man");
    }

    public void buyMan50(View view) {
        increase(man, 50, "man");
    }

    public void buyCar1(View view) {
        increase(car, 1, "car");
    }

    public void buyCar10(View view) {
        increase(car, 10, "car");
    }

    public void buyCar50(View view) {
        increase(car, 50, "car");
    }

    public void buyRobot1(View view) {
        increase(robot, 1, "robot");
    }

    public void buyRobot10(View view) {
        increase(robot, 10, "robot");
    }

    public void buyRobot50(View view) {
        increase(robot, 50, "robot");
    }

    public void buyFactory1(View view) {
        increase(factory, 1, "factory");
    }

    public void buyFactory10(View view) {
        increase(factory, 10, "factory");
    }

    public void buyFactory50(View view) {
        increase(factory, 50, "factory");
    }


    public void toPlay(View view) {
        Intent intent = new Intent(StoreActivity.this, PlayActivity.class);
        startActivity(intent);
        update(db);
        finish();
    }
}