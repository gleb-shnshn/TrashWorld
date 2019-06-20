package glebshanshin.trashworld;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.muddzdev.styleabletoast.StyleableToast;

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
        App.getInstance().clickPlayer.start();
        setContentView(R.layout.statistics_main);
        resetData();
        update();
        StyleableToast.makeText(this, "✓  Все данные стерты", Toast.LENGTH_SHORT, R.style.Clear).show();
        init1();
        fillall();
    }

    private void resetData() {
        TSH = 0;//очки TSH
        man = 0;//количество уборщиков
        car = 0;//количество мусоровозов
        robot = 0;//количество роботов
        factory = 0;//количество заводов
        paperc = 0;//количество отсортированного мусора из категории бумага
        plasticc = 0;//количество отсортированного мусора из категории пластик
        metalc = 0;//количество отсортированного мусора из категории металл
        organicc = 0;//количество отсортированного мусора из категории органика
        notrecyclec = 0;//количество отсортированного мусора из категории электро
        glassc = 0;//количество отсортированного мусора из категории стекло
        trash = plasticc + paperc + metalc + organicc + notrecyclec + glassc;//общее количество мусора
        mistake = 0;//количество ошибок
        paperb = 1;//бонус для категории бумага
        plasticb = 1;//бонус для категории пластик
        metalb = 1;//бонус для категории металл
        organicb = 1;//бонус для категории органика
        notrecycleb = 1;//бонус для категории электро
        glassb = 1;//бонус для категории стекло
        multi = 1;//общий бонус(появляется по достижении 3 уровня по отдельности)
        code1 = "0";//первый qr код
        code2 = "0";//второй qr кол
        music = 1f;//настройка громкости фоновой музыки
        effects = 1f;//настройка громкости кликов
    }

    public void No(View view) {//отказ обнуления
        App.getInstance().clickPlayer.start();
        setContentView(R.layout.statistics_main);
        init1();
        fillall();
    }

    public void reset(View view) {//задавание вопроса на обнуления
        App.getInstance().clickPlayer.start();
        setContentView(R.layout.check_main);
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