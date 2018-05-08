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
    TextView TSHv, priceO, priceB, text;
    String m = "Случайное  TSH\nот ";
    int factory, robot, car, man, TSH, price, nprice;

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
        String newa = getPrice(price);
        priceO = findViewById(R.id.price);
        priceO.setText("Цена: " + newa + " TSH");
        nprice = (TSH / 10) + 1000;
        priceB = findViewById(R.id.price1);
        text = findViewById(R.id.priceB);
        priceB.setText("Цена: "+getPrice(nprice)+" TSH");
        text.setText(m + getPrice(nprice / 2) + " до " + getPrice((int)(nprice * 1.5)));
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

    private String getPrice(int s) {
        String newa = "" + s;
        if (newa.length() > 9) {
            newa = newa.substring(0, newa.length() - 9) + "B";
        } else if (newa.length() > 6) {
            newa = newa.substring(0, newa.length() - 6) + "M";
        } else if (newa.length() > 3) {
            newa = newa.substring(0, newa.length() - 3) + "K";
        }
        return newa;
    }

    private void updateTSH() {
        String newa = getPrice(TSH);
        TSHv.setText(newa + " TSH ");
    }

    private void toast() {
        Toast.makeText(getApplicationContext(), "Недостаточно TSH", Toast.LENGTH_SHORT).show();
    }

    public void toPlay(View view) {
        Intent intent = new Intent(LotteryStoreActivity.this, PlayActivity.class);
        update(db);
        startActivity(intent);
        finish();
    }

    public void buyBronze(View view) {
        if (TSH >= nprice) {
            TSH -= nprice;
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
