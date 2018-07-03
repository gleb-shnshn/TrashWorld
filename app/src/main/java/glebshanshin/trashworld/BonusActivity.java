package glebshanshin.trashworld;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

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

    public void toAchievements(View view) {//переход в класс Достижений
        transfer(AchievementsActivity.class);
    }

    public void reload(View view) {//кнопка обнуления бонусов при достижении 3 уровня у всех бонусов
        if (flag) {
            clickPlayer.start();
            ContentValues newValues = new ContentValues();
            newValues.put("paperb", 1);
            newValues.put("plasticb", 1);
            newValues.put("metalb", 1);
            newValues.put("organicb", 1);
            newValues.put("notrecycleb", 1);
            newValues.put("glassb", 1);
            newValues.put("multi", multi * 3);
            db.update("Data", newValues, "_id = 1", null);
            init(db);
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