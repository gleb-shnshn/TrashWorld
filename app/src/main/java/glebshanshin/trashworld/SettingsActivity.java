package glebshanshin.trashworld;

import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends UniActivity {
    boolean notInInfo = true;//переменная обозначающая нахождение в информации

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);
    }

    public void statistics(View view) {//переход в класс статистики
        transfer(StatisticsActivity.class);
    }

    public void toMenu(View view) {//переход в главное меню
        transfer(MainActivity.class);
    }

    public void promo(View view) {//переход в активность промо-кодов
        transfer(PromoActivity.class);
    }

    public void music(View view) {//переход в активность с музыкой
        transfer(MusicActivity.class);
    }

    public void toInfo(View view) {//показ информации
        clickPlayer.start();
        notInInfo = false;
        setContentView(R.layout.info_main);
    }

    public void toSettings(View view) {//возврат настроек
        clickPlayer.start();
        notInInfo = true;
        setContentView(R.layout.settings_main);
    }

    //переход в класс настроек с помощью встроенной кнопки назад
    @Override
    public void onBackPressed() {
        if (!notInInfo) {
            notInInfo = true;
            setContentView(R.layout.settings_main);
            clickPlayer.start();
        } else {
            transfer(MainActivity.class);
            super.onBackPressed();
        }
    }
}