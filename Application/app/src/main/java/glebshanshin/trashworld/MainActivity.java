package glebshanshin.trashworld;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends UniActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toStart(View view) {
        transfer(PlayActivity.class);
    }//переход в класс основного геймплея

    public void toExit(View view) {//выход из приложения
        if (notIntent) {
            notIntent = false;
            finish1();
        }
    }

    public void toAchievements(View view) {
        transfer(AchievementsActivity.class);
    }//переход в класс Достижений

    public void toSettings(View view) {
        transfer(SettingsActivity.class);
    }//переход в класс Настроек
}