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
import android.widget.TextView;

import java.util.Random;

import site.iway.androidhelpers.ScratchCard;

public class LotteryActivity extends Activity {
    ScratchCard sc;
    TextView tsh;
    Button btn, back;
    ImageView img;
    DBHelper dbHelper;
    Cursor cursor;
    SQLiteDatabase db;
    int organicc, plasticc, metalc, glassc, notrecyclec, paperc;
    long TSH, obj;
    private boolean isOpen = false, isBack = false, notIntent = true;
    MediaPlayer menuPlayer;
    float music, effects;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.lottery_main);
        sc = findViewById(R.id.scratch_view);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        init(db);
        String got = getIntent().getExtras().getString("1");
        sc.setScratchDrawable(getDrawable(getResources().getIdentifier(got, "drawable", getPackageName())));
        Random random = new Random();
        if (got.equals("silverl")) {
            img = findViewById(R.id.img);
            obj = random.nextInt(4) + 1;
            switch ((int) obj) {
                case 1:
                    img.setImageDrawable(getDrawable(R.drawable.man));
                    break;
                case 2:
                    img.setImageDrawable(getDrawable(R.drawable.car));
                    break;
                case 3:
                    img.setImageDrawable(getDrawable(R.drawable.robot));
                    break;
                case 4:
                    img.setImageDrawable(getDrawable(R.drawable.factory));
                    break;
            }
        } else if (got.equals("bronzel")) {
            long nprice = (TSH / 10) + 1000;
            obj = (long) (random.nextFloat() * nprice + nprice / 2);
            tsh = findViewById(R.id.TSH);
            tsh.setText(getPrice(obj));
        } else if (got.equals("goldl")) {
            img = findViewById(R.id.img);
            obj = random.nextInt(6) + 5;
            switch ((int) obj) {
                case 5:
                    glassc = m(glassc) + 1;
                    img.setImageDrawable(getDrawable(getResources().getIdentifier("glass" + glassc, "drawable", getPackageName())));
                    update("glassb", glassc);
                    break;
                case 6:
                    metalc = m(metalc) + 1;
                    img.setImageDrawable(getDrawable(getResources().getIdentifier("metal" + metalc, "drawable", getPackageName())));
                    update("metalb", metalc);
                    break;
                case 7:
                    paperc = m(paperc) + 1;
                    img.setImageDrawable(getDrawable(getResources().getIdentifier("paper" + paperc, "drawable", getPackageName())));
                    update("paperb", paperc);
                    break;
                case 8:
                    organicc = m(organicc) + 1;
                    img.setImageDrawable(getDrawable(getResources().getIdentifier("organic" + organicc, "drawable", getPackageName())));
                    update("organicb", organicc);
                    break;
                case 9:
                    notrecyclec = m(notrecyclec) + 1;
                    img.setImageDrawable(getDrawable(getResources().getIdentifier("notrecycle" + notrecyclec, "drawable", getPackageName())));
                    update("notrecycleb", notrecyclec);
                    break;
                case 10:
                    plasticc = m(plasticc) + 1;
                    img.setImageDrawable(getDrawable(getResources().getIdentifier("plastic" + plasticc, "drawable", getPackageName())));
                    update("plasticb", plasticc);
                    break;

            }
            obj = 0;
        }
        btn = findViewById(R.id.button);
        back = findViewById(R.id.first);
        btn.setBackgroundColor(getResources().getColor(R.color.alpha1));
        back.setBackgroundColor(getResources().getColor(R.color.alpha1));
        sc.setScratchWidth(150f);
        sc.setOnScratchListener(new ScratchCard.OnScratchListener() {
            @Override
            public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                if (visiblePercent >= 0.66) {
                    isOpen = true;
                    btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.skip));
                }
            }
        });
    }

    private int m(int in) {
        if (in == 3) {
            return 2;
        }
        return in;
    }

    private void update(String str, int in) {
        ContentValues newValues = new ContentValues();
        newValues.put(str, in);
        db.update("Data", newValues, "_id = 1", null);
    }

    public void skip(View view) {
        if (isOpen) {
            isOpen = false;
            sc.setAlpha(0);
            btn.setBackgroundColor(getResources().getColor(R.color.alpha1));
            back.setBackgroundDrawable(getResources().getDrawable(R.drawable.backbut1));
            isBack = true;
        }
    }

    public void init(SQLiteDatabase db) {
        cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        TSH = cursor.getLong(1);
        paperc = cursor.getInt(13);
        plasticc = cursor.getInt(14);
        metalc = cursor.getInt(15);
        organicc = cursor.getInt(16);
        notrecyclec = cursor.getInt(17);
        glassc = cursor.getInt(18);
        music = cursor.getFloat(22);
        effects = cursor.getFloat(23);
        cursor.close();
    }

    public void toBack(View view) {
        if ((isBack) && (notIntent)) {
            notIntent = false;
            Intent intent = new Intent(LotteryActivity.this, LotteryStoreActivity.class);
            intent.putExtra("prize", obj + "");
            startActivity(intent);
            finish1();
        }
    }

    private String getPrice(long s) {
        String newa = "" + s;
        if (newa.length() > 10) {
            newa = newa.substring(0, newa.length() - 9) + "B";
        } else if (newa.length() > 7) {
            newa = newa.substring(0, newa.length() - 6) + "M";
        } else if (newa.length() > 4) {
            newa = newa.substring(0, newa.length() - 3) + "K";
        }
        return newa;
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