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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.clock.scratch.ScratchView;

import java.util.Random;

public class LotteryActivity extends Activity {
    ScratchView sc;
    TextView tv, tsh;
    Button btn, back;
    ImageView img;
    DBHelper dbHelper;
    Cursor cursor;
    SQLiteDatabase db;
    int organicc, plasticc, metalc, glassc, notrecyclec, paperc;
    int obj;
    private boolean isOpen = false, isBack = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.lottery_main);
        sc = findViewById(R.id.scratch_view);
        String got = getIntent().getExtras().getString("1");
        sc.setWatermark(getResources().getIdentifier(got, "drawable", getPackageName()));
        Random random = new Random();
        if (got.equals("silverl")) {
            img = findViewById(R.id.img);
            obj = random.nextInt(4) + 1;
            switch (obj) {
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
            obj = random.nextInt(1500) + 500;
            tsh = findViewById(R.id.TSH);
            tsh.setText(obj + " TSH");
        } else if (got.equals("goldl")) {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
            init(db);
            img = findViewById(R.id.img);
            obj = random.nextInt(6) + 5;
            switch (obj) {
                case 5:
                    glassc = m(glassc)+1;
                    img.setImageDrawable(getDrawable(getResources().getIdentifier("glass" + glassc, "drawable", getPackageName())));
                    update("glassb", glassc);
                    break;
                case 6:
                    metalc = m(metalc)+1;
                    img.setImageDrawable(getDrawable(getResources().getIdentifier("metal" + metalc, "drawable", getPackageName())));
                    update("metalb", metalc);
                    break;
                case 7:
                    paperc = m(paperc)+1;
                    img.setImageDrawable(getDrawable(getResources().getIdentifier("paper" + paperc, "drawable", getPackageName())));
                    update("paperb", paperc);
                    break;
                case 8:
                    organicc = m(organicc)+1;
                    img.setImageDrawable(getDrawable(getResources().getIdentifier("organic" + organicc, "drawable", getPackageName())));
                    update("organicb", organicc);
                    break;
                case 9:
                    notrecyclec = m(notrecyclec)+1;
                    img.setImageDrawable(getDrawable(getResources().getIdentifier("notrecycle" + notrecyclec, "drawable", getPackageName())));
                    update("notrecycleb", notrecyclec);
                    break;
                case 10:
                    plasticc = m(plasticc)+1;
                    img.setImageDrawable(getDrawable(getResources().getIdentifier("plastic" + plasticc, "drawable", getPackageName())));
                    update("plasticb", plasticc);
                    break;

            }
            obj = 0;
        }
        sc.setMaskColor(0x00000000);
        sc.setMaxPercent(80);
        btn = findViewById(R.id.button);
        back = findViewById(R.id.back);
        btn.setBackgroundColor(getResources().getColor(R.color.alpha1));
        back.setBackgroundColor(getResources().getColor(R.color.alpha1));
        sc.setEraseStatusListener(new ScratchView.EraseStatusListener() {
            @Override
            public void onProgress(int percent) {
            }

            @Override
            public void onCompleted(View view) {
                btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.skip));
                isOpen = true;
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
            sc.clear();
            isOpen = false;
            btn.setBackgroundColor(getResources().getColor(R.color.alpha1));
            back.setBackgroundDrawable(getResources().getDrawable(R.drawable.backbut1));
            isBack = true;
        }
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
    }

    public void toBack(View view) {
        if (isBack) {
            Intent intent = new Intent(LotteryActivity.this, LotteryStoreActivity.class);
            intent.putExtra("prize", obj + "");
            startActivity(intent);
            finish();
        }
    }
}