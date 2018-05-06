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

public class LotteryStoreActivity extends Activity {
    SQLiteDatabase db;
    DBHelper dbHelper;
    Cursor cursor;
    Intent intent;
    TextView TSHv, priceO;
    int factory, robot, car, man, TSH;
    int organicc, plasticc, metalc, glassc, notrecyclec, paperc, mistakes, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.lotterystore_main);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        TSHv = findViewById(R.id.TSH);
        init(db);
        checkPrize();
        updateTSH();
        updatePrice();
        intent = new Intent(LotteryStoreActivity.this, LotteryActivity.class);
    }

    private void checkPrize() {
        int prize = Integer.parseInt(getIntent().getExtras().getString("prize"));
        switch (prize) {
            case 0:
                break;
            case 1:
                man++;
                break;
            case 2:
                car++;
                break;
            case 3:
                robot++;
                break;
            case 4:
                factory++;
                break;
            default:
                TSH += prize;
        }
    }

    private void updatePrice() {
        price = ((man + 1) + ((car + 1) * 10) + ((robot + 1) * 50) + ((factory + 1) * 100)) / 4;
        String newa = "" + price;
        if (newa.length() > 9) {
            newa = newa.substring(0, newa.length() - 9) + "B";
        } else if (newa.length() > 6) {
            newa = newa.substring(0, newa.length() - 6) + "M";
        } else if (newa.length() > 3) {
            newa = newa.substring(0, newa.length() - 3) + "K";
        }
        priceO = findViewById(R.id.price);
        priceO.setText("Цена: " + newa + " TSH");
    }

    public void init(SQLiteDatabase db) {
        cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        TSH = Integer.parseInt(cursor.getString(1));
        man = Integer.parseInt(cursor.getString(2));
        car = Integer.parseInt(cursor.getString(3));
        robot = Integer.parseInt(cursor.getString(4));
        factory = Integer.parseInt(cursor.getString(5));
        paperc = Integer.parseInt(cursor.getString(6));
        plasticc = Integer.parseInt(cursor.getString(7));
        metalc = Integer.parseInt(cursor.getString(8));
        organicc = Integer.parseInt(cursor.getString(9));
        notrecyclec = Integer.parseInt(cursor.getString(10));
        glassc = Integer.parseInt(cursor.getString(11));
        mistakes = Integer.parseInt(cursor.getString(12));
        cursor.close();
    }

    private void update(SQLiteDatabase db) {
        ContentValues newValues = new ContentValues();
        newValues.put("TSH", TSH);
        newValues.put("man", man);
        newValues.put("car", car);
        newValues.put("robot", robot);
        newValues.put("factory", factory);

        newValues.put("paper", paperc);
        newValues.put("plastic", plasticc);
        newValues.put("metal", metalc);
        newValues.put("organic", organicc);
        newValues.put("notrecycle", notrecyclec);
        newValues.put("glass", glassc);
        newValues.put("mistakes", mistakes);
        db.update("Data", newValues, "_id = 1", null);
    }

    private void updateTSH() {
        String newa = "" + TSH;
        if (newa.length() > 9) {
            newa = newa.substring(0, newa.length() - 9) + "B";
        }
        if (newa.length() > 6) {
            newa = newa.substring(0, newa.length() - 6) + "M";
        } else if (newa.length() > 3) {
            newa = newa.substring(0, newa.length() - 3) + "K";
        }
        String prefix = new String(new char[10 - newa.length()]).replace("\0", " ");
        TSHv.setText(prefix + newa + " TSH");
    }

    private void toast() {
        Toast.makeText(getApplicationContext(), "Не достаточно TSH", Toast.LENGTH_SHORT).show();
    }

    public void toPlay(View view) {
        Intent intent = new Intent(LotteryStoreActivity.this, PlayActivity.class);
        update(db);
        startActivity(intent);
        finish();
    }

    public void buyBronze(View view) {
        if (TSH >= 1000) {
            TSH -= 1000;
            intent.putExtra("1", "bronzel");
            update(db);
            startActivity(intent);
        } else {
            toast();
        }
    }

    public void buySilver(View view) {
        if (TSH >= price) {
            TSH -= price;
            intent.putExtra("1", "silverl");
            update(db);
            startActivity(intent);
        } else {
            toast();
        }
    }

    public void buyGold(View view) {
        if (TSH >= 10000) {
            TSH -= 10000;
            intent.putExtra("1", "goldl");
            update(db);
            startActivity(intent);
        } else {
            toast();
        }
    }
}
