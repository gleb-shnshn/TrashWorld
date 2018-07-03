package glebshanshin.trashworld;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import site.iway.androidhelpers.ScratchCard;

public class LotteryActivity extends UniActivity {
    ScratchCard sc;
    TextView tsh;
    Button btn, back;
    ImageView img;
    long obj;
    private boolean isOpen = false, isBack = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottery_main);
        sc = findViewById(R.id.scratch_view);
        String got = getIntent().getExtras().getString("1");
        sc.setScratchDrawable(getDrawable(getResources().getIdentifier(got, "drawable", getPackageName())));
        Random random = new Random();
        if (got.equals("silverl")) {//если серебряная лотерейка
            img = findViewById(R.id.img);
            obj = random.nextInt(4) + 1;
            switch ((int) obj) {//установка случайного предмета
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
        } else if (got.equals("bronzel")) {//если бронзовая лотерейка
            long nprice = (TSH / 10) + 1000;
            obj = (long) (random.nextFloat() * nprice + nprice / 2);
            tsh = findViewById(R.id.TSH);
            tsh.setText(getPrice(obj) + "\nTSH");//установка приза
        } else if (got.equals("goldl")) {//если золотая лотерейка
            img = findViewById(R.id.img);
            obj = random.nextInt(6) + 5;
            switch ((int) obj) {//установка случайного бонуса
                case 5:
                    glassb = m(glassb) + 1;
                    img.setImageDrawable(getDrawable(getResources().getIdentifier("glass" + glassb, "drawable", getPackageName())));
                    update("glassb", glassb);
                    break;
                case 6:
                    metalb = m(metalb) + 1;
                    img.setImageDrawable(getDrawable(getResources().getIdentifier("metal" + metalb, "drawable", getPackageName())));
                    update("metalb", metalb);
                    break;
                case 7:
                    paperb = m(paperb) + 1;
                    img.setImageDrawable(getDrawable(getResources().getIdentifier("paper" + paperb, "drawable", getPackageName())));
                    update("paperb", paperb);
                    break;
                case 8:
                    organicb = m(organicb) + 1;
                    img.setImageDrawable(getDrawable(getResources().getIdentifier("organic" + organicb, "drawable", getPackageName())));
                    update("organicb", organicb);
                    break;
                case 9:
                    notrecycleb = m(notrecycleb) + 1;
                    img.setImageDrawable(getDrawable(getResources().getIdentifier("notrecycle" + notrecycleb, "drawable", getPackageName())));
                    update("notrecycleb", notrecycleb);
                    break;
                case 10:
                    plasticb = m(plasticb) + 1;
                    img.setImageDrawable(getDrawable(getResources().getIdentifier("plastic" + plasticb, "drawable", getPackageName())));
                    update("plasticb", plasticb);
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
                if (visiblePercent >= 0.7) {//появление кнопки пропустить
                    isOpen = true;
                    btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.smartskip));
                }
                if (visiblePercent >= 0.99) {//появление кнопки назад
                    isOpen = false;
                    sc.setAlpha(0);
                    btn.setBackgroundColor(getResources().getColor(R.color.alpha1));
                    back.setBackgroundDrawable(getResources().getDrawable(R.drawable.smartback));
                    isBack = true;
                }
            }
        });
    }

    private int m(int in) {//проверка является ли максимумом
        if (in == 3) {
            return 2;
        }
        return in;
    }

    private void update(String str, int in) {//обновление базы данных
        ContentValues newValues = new ContentValues();
        newValues.put(str, in);
        db.update("Data", newValues, "_id = 1", null);
    }

    public void skip(View view) {//кнопка пропустить
        if (isOpen) {
            clickPlayer.start();
            isOpen = false;
            sc.setAlpha(0);
            btn.setBackgroundColor(getResources().getColor(R.color.alpha1));
            back.setBackgroundDrawable(getResources().getDrawable(R.drawable.smartback));
            isBack = true;
        }
    }

    public void toBack(View view) {//переход в класс магазин лотереек
        if ((isBack) && (notIntent)) {
            notIntent = false;
            Intent intent = new Intent(LotteryActivity.this, LotteryStoreActivity.class);
            intent.putExtra("prize", obj + "");
            startActivity(intent);
            finish1();
        }
    }

    //выход в магазин лотереек через встроенную кнопку назад
    @Override
    public void onBackPressed() {
        if ((isBack) && (notIntent)) {
            notIntent = false;
            Intent intent = new Intent(LotteryActivity.this, LotteryStoreActivity.class);
            intent.putExtra("prize", obj + "");
            startActivity(intent);
            finish1();
            super.onBackPressed();
        }
    }
}