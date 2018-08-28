package glebshanshin.trashworld;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class StatisticsActivity extends UniActivity {
    TextView organicv, plasticv, metalv, glassv, notrecyclev, paperv, mistakev, trashv, TSHv;
    String pref = " : ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_main);
        //масштабирование шрифта
        TSH = TSH + ((2 + (man - 1)) * man / 2) + ((20 + 10 * (car - 1)) * car / 2) + ((100 + 50 * (robot - 1)) * robot / 2) + ((200 + 100 * (factory - 1)) * factory / 2);
        init1();
        fillall();
    }

    public void Yes(View view) {//подтверждение обнуления
        clickPlayer.start();
        setContentView(R.layout.statistics_main);
        update(db);
        init1();
        init(db);
        fillall();
    }

    public void No(View view) {//отказ обнуления
        clickPlayer.start();
        setContentView(R.layout.statistics_main);
        init1();
        init(db);
        fillall();
    }

    public void reset(View view) {//задавание вопроса на обнуления
        clickPlayer.start();
        setContentView(R.layout.check_main);
    }

    private void update(SQLiteDatabase db) {//обнуление базы данных
        db.update("Data", dbHelper.getDefault(), "_id = 1", null);
        StyleableToast.makeText(this, "✓  Все данные стерты", Toast.LENGTH_SHORT, R.style.Clear).show();
    }

    private void fillall() {//заполнение всех пунктов статистики
        glassv.setText(pref + glassc);
        organicv.setText(pref + organicc);
        paperv.setText(pref + paperc);
        trashv.setText(pref + trash);
        notrecyclev.setText(pref + notrecyclec);
        mistakev.setText(pref + mistake);
        metalv.setText(pref + metalc);
        plasticv.setText(pref + plasticc);
        TSHv.setText(getPrice(TSH) + " TSH ");
    }

    private void init1() {//инициализация TextView и масштабирование шрифта
        glassv = findViewById(R.id.glass);
        metalv = findViewById(R.id.metal);
        notrecyclev = findViewById(R.id.notrecycle);
        organicv = findViewById(R.id.organic);
        paperv = findViewById(R.id.paper);
        plasticv = findViewById(R.id.plastic);
        trashv = findViewById(R.id.trash);
        mistakev = findViewById(R.id.mistake);
        TSHv = findViewById(R.id.TSH);
        TSHv.setTextSize(scale * 75f);
        glassv.setTextSize(scale * 65f);
        metalv.setTextSize(scale * 65f);
        notrecyclev.setTextSize(scale * 65f);
        organicv.setTextSize(scale * 65f);
        paperv.setTextSize(scale * 65f);
        plasticv.setTextSize(scale * 65f);
        trashv.setTextSize(scale * 65f);
        mistakev.setTextSize(scale * 65f);
    }

    public void toSettings(View view) {
        transfer(SettingsActivity.class);
    }//переход в активность настроек

    //возврат в настройки через встроенную кнопку назад
    @Override
    public void onBackPressed() {
        transfer(SettingsActivity.class);
        super.onBackPressed();
    }
}