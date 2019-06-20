package glebshanshin.trashworld;

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
    boolean isOpen = false, isBack = false;//флаги для проверки доситупна ли кнопка пропустить и кнопка назад
    Random random = new Random();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottery_main);
        sc = findViewById(R.id.scratch_view);
        String got = getIntent().getExtras().getString("lottery");
        sc.setScratchDrawable(getDrawable(getResources().getIdentifier(got, "drawable", getPackageName())));
        switch (got) {
            case "bronzel": //если бронзовая лотерейка
                ifBronze();
                break;
            case "silverl": //если серебряная лотерейка
                ifSilver();
                break;
            case "goldl": //если золотая лотерейка
                ifGold();
                break;
        }
        btn = findViewById(R.id.button);
        back = findViewById(R.id.first);
        btn.setAlpha(0);
        back.setAlpha(0);
        sc.setScratchWidth(150f);
        sc.setOnScratchListener(new ScratchCard.OnScratchListener() {
            @Override
            public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                if (visiblePercent >= 0.99) {//появление кнопки назад
                    isOpen = false;
                    sc.setAlpha(0);
                    btn.setAlpha(0);//исчезание кнопки пропустить
                    back.setAlpha(1);
                    isBack = true;
                } else if (visiblePercent >= 0.7 && !isBack) {//появление кнопки пропустить
                    isOpen = true;
                    btn.setAlpha(1);
                }
            }
        });
    }

    private void ifGold() {
        img = findViewById(R.id.img);
        obj = random.nextInt(6) + 5;
        switch ((int) obj) {//установка случайного бонуса
            case 5://стекло
                glassb = wrapMax(glassb) + 1;
                img.setImageDrawable(getDrawable(getResources().getIdentifier("glass" + glassb, "drawable", getPackageName())));
                break;
            case 6://металл
                metalb = wrapMax(metalb) + 1;
                img.setImageDrawable(getDrawable(getResources().getIdentifier("metal" + metalb, "drawable", getPackageName())));
                break;
            case 7://бумага
                paperb = wrapMax(paperb) + 1;
                img.setImageDrawable(getDrawable(getResources().getIdentifier("paper" + paperb, "drawable", getPackageName())));
                break;
            case 8://органика
                organicb = wrapMax(organicb) + 1;
                img.setImageDrawable(getDrawable(getResources().getIdentifier("organic" + organicb, "drawable", getPackageName())));
                break;
            case 9://электро
                notrecycleb = wrapMax(notrecycleb) + 1;
                img.setImageDrawable(getDrawable(getResources().getIdentifier("notrecycle" + notrecycleb, "drawable", getPackageName())));
                break;
            case 10://пластик
                plasticb = wrapMax(plasticb) + 1;
                img.setImageDrawable(getDrawable(getResources().getIdentifier("plastic" + plasticb, "drawable", getPackageName())));
                break;
        }
        update();
        obj = 0;
    }

    private void ifBronze() {
        long nprice = (TSH / 10) + 1000;
        obj = (long) (random.nextFloat() * nprice + nprice / 2);
        tsh = findViewById(R.id.TSH);
        tsh.setText(getPrice(obj) + "\nTSH");//установка приза
    }

    private void ifSilver() {
        img = findViewById(R.id.img);
        obj = random.nextInt(4) + 1;
        switch ((int) obj) {//установка случайного предмета
            case 1://уборщик
                img.setImageDrawable(getDrawable(R.drawable.man));
                break;
            case 2://мусоровоз
                img.setImageDrawable(getDrawable(R.drawable.car));
                break;
            case 3://робот
                img.setImageDrawable(getDrawable(R.drawable.robot));
                break;
            case 4://завод
                img.setImageDrawable(getDrawable(R.drawable.factory));
                break;
        }
    }

    private int wrapMax(int in) {//проверка является ли максимумом
        return in - (in / 3);
    }

    public void skip(View view) {//кнопка пропустить
        if (isOpen) {
            App.getInstance().clickPlayer.start();
            isOpen = false;
            sc.setAlpha(0);
            btn.setAlpha(0);//исчезание кнопки пропустить
            back.setAlpha(1);//появление кнопки назад
            isBack = true;
        }
    }

    public void toBack(View view) {//переход в класс магазин лотереек
        if ((isBack) && (notIntent)) {
            notIntent = false;
            Intent intent = new Intent(LotteryActivity.this, LotteryStoreActivity.class);
            newClass = LotteryStoreActivity.class;
            intent.putExtra("prize", obj + "");//передача приза в активности магазина лотереек
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
            intent.putExtra("prize", obj + "");//передача приза в активности магазина лотереек
            startActivity(intent);
            finish1();
            super.onBackPressed();
        }
    }
}