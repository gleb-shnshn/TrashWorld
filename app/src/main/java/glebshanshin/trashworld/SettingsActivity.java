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

    public void statistics(View view) {
        transfer(StatisticsActivity.class);
    }//переход в класс статистики

    public void toMenu(View view) {
        transfer(MainActivity.class);
    }//переход в главное меню

    public void promo(View view) {
        transfer(PromoActivity.class);
    }//переход в активность промо-кодов

    public void music(View view) {
        transfer(MusicActivity.class);
    }//переход в активность с настройкой музыки

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