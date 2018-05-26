package glebshanshin.trashworld;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.tv.TvView;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class LotteryStoreActivity extends Activity {
    SQLiteDatabase db;
    DBHelper dbHelper;
    Cursor cursor;
    Intent intent;
    TextView TSHv, priceO, priceB, text;
    String m = "Случайное  TSH\nот ";
    long TSH, price, nprice;
    int factory, robot, car, man;
    boolean notIntent = true;
    MediaPlayer menuPlayer;
    float music, effects, scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.lotterystore_main);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        scale = 1 / getResources().getDisplayMetrics().density * 0.5f + getWindowManager().getDefaultDisplay().getHeight() * getWindowManager().getDefaultDisplay().getWidth() * 0.0000001f;
        TSHv = findViewById(R.id.TSH);
        TSHv.setTextSize(scale * 75f);
        init(db);
        checkPrize();
        updateTSH();
        updatePrice();
        intent = new Intent(LotteryStoreActivity.this, LotteryActivity.class);
    }

    private void checkPrize() {
        long prize = Long.parseLong(getIntent().getExtras().getString("prize"));
        if (prize <= 4) {
            switch ((int) prize) {
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
            }
        } else {
            TSH += prize;
        }
    }

    private void updatePrice() {
        price = ((man + 1) + ((car + 1) * 10) + ((robot + 1) * 50) + ((factory + 1) * 100)) / 4;
        String newa = getPrice(price);
        priceO.setText("Цена: " + newa + " TSH");
        nprice = (TSH / 10) + 1000;
        priceB.setText("Цена: " + getPrice(nprice) + " TSH");
        text.setText(m + getPrice(nprice / 2) + " до " + getPrice((long) (nprice * 1.5)));
    }

    public void init(SQLiteDatabase db) {
        cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        TSH = cursor.getLong(1);
        man = cursor.getInt(2);
        car = cursor.getInt(3);
        robot = cursor.getInt(4);
        factory = cursor.getInt(5);
        music = cursor.getFloat(22);
        effects = cursor.getFloat(23);
        cursor.close();
        priceB = findViewById(R.id.price1);
        text = findViewById(R.id.priceB);
        priceO = findViewById(R.id.price);
        text.setTextSize(scale * 55f);
        priceB.setTextSize(scale * 55f);
        priceO.setTextSize(scale * 55f);
        TextView a = findViewById(R.id.des1),
                b = findViewById(R.id.des2),
                c = findViewById(R.id.des3);
        a.setTextSize(scale * 55f);
        b.setTextSize(scale * 55f);
        c.setTextSize(scale * 55f);
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

    private String getPrice(long s) {
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

    private void updateTSH() {
        String newa = getPrice(TSH);
        TSHv.setText(newa + " TSH ");
    }

    private void toast(long a) {
        StyleableToast.makeText(getApplicationContext(), "✘  Не хватает " + a + " TSH", Toast.LENGTH_SHORT, R.style.wrong1).show();
    }

    public void toStore(View view) {
        if (notIntent) {
            notIntent = false;
            Intent intent = new Intent(LotteryStoreActivity.this, StoreActivity.class);
            update(db);
            startActivity(intent);
            finish1();
        }
    }

    public void buyBronze(View view) {
        if (notIntent) {
            if (TSH >= nprice) {
                notIntent = false;
                TSH -= nprice;
                intent.putExtra("1", "bronzel");
                update(db);
                startActivity(intent);
                finish1();
            } else {
                toast(nprice - TSH);
            }
        }
    }

    public void buySilver(View view) {
        if (notIntent) {
            if (TSH >= price) {
                notIntent = false;
                TSH -= price;
                intent.putExtra("1", "silverl");
                update(db);
                startActivity(intent);
                finish1();
            } else {
                toast(price - TSH);
            }
        }
    }

    public void buyGold(View view) {
        if (notIntent) {
            if (TSH >= 100000) {
                notIntent = false;
                TSH -= 100000;
                intent.putExtra("1", "goldl");
                update(db);
                startActivity(intent);
                finish1();
            } else {
                toast(100000 - TSH);
            }
        }
    }

    private void finish1() {
        menuPlayer.stop();
        menuPlayer = MediaPlayer.create(this, R.raw.click);
        menuPlayer.setVolume(effects, effects);
        menuPlayer.setLooping(false);
        menuPlayer.start();
        finish();
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
}