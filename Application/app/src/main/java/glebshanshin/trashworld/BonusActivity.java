package glebshanshin.trashworld;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.muddzdev.styleabletoast.StyleableToast;


public class BonusActivity extends UniActivity {
    ImageView plastic, glass, paper, notrecycle, metal, organic;
    Button reloadt;
    boolean flag = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bonus_main);
        init1();
        if (organicb + plasticb + metalb + glassb + notrecycleb + paperb == 18) {//проверка 3 уровня у всех
            flag = true;
            StyleableToast.makeText(this, "Вы можете обнулить бонусы и тогда весь ваш TSH/мусор будет умножаться на " + multi * 3, R.style.Clear).show();
            reloadt = findViewById(R.id.reload);
            reloadt.setAlpha(1);
        }
    }

    private void init1() {//инициализация ImageView и установка картинок
        plastic = findViewById(R.id.plastic);
        glass = findViewById(R.id.glass);
        paper = findViewById(R.id.paper);
        notrecycle = findViewById(R.id.notrecycle);
        metal = findViewById(R.id.metal);
        organic = findViewById(R.id.organic);
        plastic.setImageDrawable(getDrawable(getResources().getIdentifier("plastic" + plasticb, "drawable", getPackageName())));
        glass.setImageDrawable(getDrawable(getResources().getIdentifier("glass" + glassb, "drawable", getPackageName())));
        paper.setImageDrawable(getDrawable(getResources().getIdentifier("paper" + paperb, "drawable", getPackageName())));
        notrecycle.setImageDrawable(getDrawable(getResources().getIdentifier("notrecycle" + notrecycleb, "drawable", getPackageName())));
        metal.setImageDrawable(getDrawable(getResources().getIdentifier("metal" + metalb, "drawable", getPackageName())));
        organic.setImageDrawable(getDrawable(getResources().getIdentifier("organic" + organicb, "drawable", getPackageName())));
    }

    public void toAchievements(View view) {
        transfer(AchievementsActivity.class);
    }//переход в класс Достижений

    public void reload(View view) {//кнопка обнуления бонусов при достижении 3 уровня у всех бонусов
        if (flag) {
            App.getInstance().clickPlayer.start();
            paperb = 1;
            plasticb = 1;
            metalb = 1;
            organicb = 1;
            notrecycleb = 1;
            glassb = 1;
            multi *= 3;
            update();
            StyleableToast.makeText(this, "✓  Бонусы успешно обнулены", R.style.Clear).show();
            reloadt.setAlpha(0);
            flag = false;
        }
    }

    //выход в достижения через встроенную кнопку назад
    @Override
    public void onBackPressed() {
        transfer(AchievementsActivity.class);
        super.onBackPressed();
    }
}