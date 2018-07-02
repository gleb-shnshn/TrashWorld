package glebshanshin.trashworld;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class StoreActivity extends Activity {
    long factory, robot, car, man;
    long TSH;
    TextView TSHv, priceman1, pricecar1, pricerobot1, pricefactory1,
            priceman10, pricecar10, pricerobot10, pricefactory10,
            priceman50, pricecar50, pricerobot50, pricefactory50,
            countman, countcar, countrobot, countfactory;
    SQLiteDatabase db;
    DBHelper dbHelper;
    Cursor cursor;
    String t = " TSH", tsh, s = " шт.";
    boolean notIntent = true;
    MediaPlayer menuPlayer, clickPlayer;
    float music, effects, scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.store_main);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        scale = 1 / getResources().getDisplayMetrics().density * 0.5f + getWindowManager().getDefaultDisplay().getHeight() * getWindowManager().getDefaultDisplay().getWidth() * 0.0000001f;
        init(db);
        updateTSH();
        updatePRICE("all");
        updateBlocked();
        clickPlayer = MediaPlayer.create(this, R.raw.click);
        clickPlayer.setVolume(effects, effects);
    }

    private void updateBlocked() {
        ((ImageView)(findViewById(R.id.m1))).setImageDrawable(getDrawable(R.drawable.smartst1b));
        ((ImageView)(findViewById(R.id.m10))).setImageDrawable(getDrawable(R.drawable.smartst10b));
        ((ImageView)(findViewById(R.id.m50))).setImageDrawable(getDrawable(R.drawable.smartst50b));
        ((ImageView)(findViewById(R.id.c1))).setImageDrawable(getDrawable(R.drawable.smartst1b));
        ((ImageView)(findViewById(R.id.c10))).setImageDrawable(getDrawable(R.drawable.smartst10b));
        ((ImageView)(findViewById(R.id.c50))).setImageDrawable(getDrawable(R.drawable.smartst50b));
        ((ImageView)(findViewById(R.id.r1))).setImageDrawable(getDrawable(R.drawable.smartst1b));
        ((ImageView)(findViewById(R.id.r10))).setImageDrawable(getDrawable(R.drawable.smartst10b));
        ((ImageView)(findViewById(R.id.r50))).setImageDrawable(getDrawable(R.drawable.smartst50b));
        ((ImageView)(findViewById(R.id.f1))).setImageDrawable(getDrawable(R.drawable.smartst1b));
        ((ImageView)(findViewById(R.id.f10))).setImageDrawable(getDrawable(R.drawable.smartst10b));
        ((ImageView)(findViewById(R.id.f50))).setImageDrawable(getDrawable(R.drawable.smartst50b));
        if (TSH >= man + 1) {
            ((ImageView)(findViewById(R.id.m1))).setImageDrawable(getDrawable(R.drawable.smartst1));
            if (TSH >= get(10, (man + 1), 1)) {
                ((ImageView)(findViewById(R.id.m10))).setImageDrawable(getDrawable(R.drawable.smartst10));
                if (TSH >= get(50, (man + 1), 1)) {
                    ((ImageView)(findViewById(R.id.m50))).setImageDrawable(getDrawable(R.drawable.smartst50));

                }
            }
        }
        if (TSH >= (car + 1) * 10) {
            ((ImageView)(findViewById(R.id.c1))).setImageDrawable(getDrawable(R.drawable.smartst1));
            if (TSH >= get(10, (car + 1) * 10, 10)) {
                ((ImageView)(findViewById(R.id.c10))).setImageDrawable(getDrawable(R.drawable.smartst10));
                if (TSH >= get(50, (car + 1) * 10, 10)) {
                    ((ImageView)(findViewById(R.id.c50))).setImageDrawable(getDrawable(R.drawable.smartst50));
                }
            }
        }
        if (TSH >= (robot + 1) * 50) {
            ((ImageView)(findViewById(R.id.r1))).setImageDrawable(getDrawable(R.drawable.smartst1));
            if (TSH >= get(10, (robot + 1) * 50, 50)) {
                ((ImageView)(findViewById(R.id.r10))).setImageDrawable(getDrawable(R.drawable.smartst10));
                if (TSH >= get(50, (robot + 1) * 50, 50)) {
                    ((ImageView)(findViewById(R.id.r50))).setImageDrawable(getDrawable(R.drawable.smartst50));
                }
            }
        }
        if (TSH >= (factory + 1) * 100) {
            ((ImageView)(findViewById(R.id.f1))).setImageDrawable(getDrawable(R.drawable.smartst1));
            if (TSH >= get(10, (factory + 1) * 100, 100)) {
                ((ImageView)(findViewById(R.id.f10))).setImageDrawable(getDrawable(R.drawable.smartst10));
                if (TSH >= get(50, (factory + 1) * 100, 100)) {
                    ((ImageView)(findViewById(R.id.f50))).setImageDrawable(getDrawable(R.drawable.smartst50));
                }
            }
        }
    }

    private void updatePRICE(String what) {//обновление цен и количества
        switch (what) {
            case "all":
                priceman1.setText(getPrice((man + 1)));
                pricecar1.setText(getPrice((car + 1) * 10));
                pricerobot1.setText(getPrice((robot + 1) * 50));
                pricefactory1.setText(getPrice((factory + 1) * 100));
                priceman10.setText(getPrice(get(10, (man + 1), 1)));
                pricecar10.setText(getPrice(get(10, (car + 1) * 10, 10)));
                pricerobot10.setText(getPrice(get(10, (robot + 1) * 50, 50)));
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

    public void toLotteryStore(View view) {//переход в класс магазин лотереек
        if (notIntent) {
            notIntent = false;
            update(db);
            Intent intent = new Intent(StoreActivity.this, LotteryStoreActivity.class);
            intent.putExtra("prize", "0");
            startActivity(intent);
            finish1();
        }
    }

    private long get(int n, long a1, int d) {
        return ((((2 * a1) + (n - 1) * d) * n) / 2);
    }//получение цены по формуле алгебраической прогрессии

    private String getPrice(long s) {//масштабирование ценников и баланса
        String newa = "" + s;

        if (newa.length() > 12)
            newa = newa.substring(0, newa.length() - 12) + "T";
        else if (newa.length() > 9)
            newa = newa.substring(0, newa.length() - 9) + "B";
        else if (newa.length() > 6)
            newa = newa.substring(0, newa.length() - 6) + "M";
        else if (newa.length() > 3)
            newa = newa.substring(0, newa.length() - 3) + "K";

        return newa + t;
    }

    private void updateTSH() {//установка нового баланса TSH
        String m = getPrice(TSH);
        TSHv.setText(m + "");
    }

    private void update(SQLiteDatabase db) {//обновление базы данных при переходе на новую активность
        ContentValues newValues = new ContentValues();
        newValues.put("TSH", TSH);
        newValues.put("man", man);
        newValues.put("car", car);
        newValues.put("robot", robot);
        newValues.put("factory", factory);
        db.update("Data", newValues, "_id = 1", null);
    }

    public void init(SQLiteDatabase db) {//получение данных из базы данных и инициализация TextView
        cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        TSH = cursor.getLong(1);
        man = cursor.getLong(2);
        car = cursor.getLong(3);
        robot = cursor.getLong(4);
        factory = cursor.getLong(5);
        music = cursor.getFloat(22);
        effects = cursor.getFloat(23);
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
        TSHv = findViewById(R.id.TSH);
    }

    private void setTextSize() {//установка масштабируемых шрифтов
        TSHv.setTextSize(scale * 74f);
        priceman1.setTextSize(scale * 55f);
        priceman10.setTextSize(scale * 55f);
        priceman50.setTextSize(scale * 55f);
        pricecar1.setTextSize(scale * 55f);
        pricecar10.setTextSize(scale * 55f);
        pricecar50.setTextSize(scale * 55f);
        pricerobot1.setTextSize(scale * 55f);
        pricerobot10.setTextSize(scale * 55f);
        pricerobot50.setTextSize(scale * 55f);
        pricefactory1.setTextSize(scale * 55f);
        pricefactory10.setTextSize(scale * 55f);
        pricefactory50.setTextSize(scale * 55f);
        countman.setTextSize(scale * 45f);
        countcar.setTextSize(scale * 45f);
        countrobot.setTextSize(scale * 45f);
        countfactory.setTextSize(scale * 45f);
        TextView a = findViewById(R.id.sopman),
                b = findViewById(R.id.sopcar),
                c = findViewById(R.id.soprobot),
                d = findViewById(R.id.sopfactory);
        a.setTextSize(scale * 40f);
        b.setTextSize(scale * 40f);
        c.setTextSize(scale * 40f);
        d.setTextSize(scale * 40f);
    }

    private void toast(long a) {//вызов всплывающего сообщения при нехватке баланса
        StyleableToast.makeText(getApplicationContext(), "✘  Не хватает " + a + " TSH", Toast.LENGTH_SHORT, R.style.wrong1).show();
    }

    private void increase(long b, int i, String d) {// покупка товара(количество уже купленного, количество нужное купить, название покупки)
        clickPlayer.start();
        int t = 0;
        switch (d) {//определение цены первого предмета
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
        long money = get(i, (b + 1) * t, t);//цена которую надо заплатить
        if (money <= TSH) {//если хватает
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
            updateTSH();
            updatePRICE(d);
            updateBlocked();
            update(db);
        } else {//если не хватает
            toast(money - TSH);
        }
    }

    //OnClick для каждой кнопки
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


    public void toPlay(View view) {//переход в класс основного геймплея
        if (notIntent) {
            notIntent = false;
            Intent intent = new Intent(StoreActivity.this, PlayActivity.class);
            startActivity(intent);
            update(db);
            finish1();
        }
    }

    private void finish1() {//отключение музыки при выходе из активности
        menuPlayer.stop();
        menuPlayer = MediaPlayer.create(this, R.raw.click);
        menuPlayer.setVolume(effects, effects);
        menuPlayer.setLooping(false);
        menuPlayer.start();
        finish();
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

    //выход в игру через встроенную кнопку назад
    @Override
    public void onBackPressed() {
        if (notIntent) {
            notIntent = false;
            Intent intent1 = new Intent(StoreActivity.this, PlayActivity.class);
            startActivity(intent1);
            update(db);
            finish1();
        }
        super.onBackPressed();
    }
}