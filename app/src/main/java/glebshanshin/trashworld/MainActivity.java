package glebshanshin.trashworld;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends UniActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toStart(View view) {//переход в класс основного геймплея
        transfer(PlayActivity.class);
    }

    public void toExit(View view) {//выход из приложения
        if (notIntent) {
            notIntent = false;
            finish1();
        }
    }

    public void toAchievements(View view) {//переход в класс Достижений
        transfer(AchievementsActivity.class);
    }

    public void toSettings(View view) {//переход в класс Настроек
        transfer(SettingsActivity.class);
    }
}