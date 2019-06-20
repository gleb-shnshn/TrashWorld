package glebshanshin.trashworld;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.muddzdev.styleabletoast.StyleableToast;

public class UniActivity extends AppCompatActivity {//Общая активность в которой описаны общие методы и поля
    long factory, robot, car, man, TSH;
    int organicc, plasticc, metalc, glassc, notrecyclec, paperc, mistake, trash, multi;
    int paperb, plasticb, metalb, organicb, notrecycleb, glassb;
    float music, effects, scale;
    String code1, code2;
    StyleableToast toast;
    Class newClass;
    boolean notIntent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();//получение данных из базы данных
        newClass = getClass();
    }

    public void update() {
        SharedPreferences sp = App.getInstance().getStorage();
        SharedPreferences.Editor ed = sp.edit();
        ed.putLong("TSH", TSH);//очки TSH
        ed.putLong("man", man);//количество уборщиков
        ed.putLong("car", car);//количество мусоровозов
        ed.putLong("robot", robot);//количество роботов
        ed.putLong("factory", factory);//количество заводов
        ed.putInt("paperc", paperc);//количество отсортированного мусора из категории бумага
        ed.putInt("plasticc", plasticc);//количество отсортированного мусора из категории пластик
        ed.putInt("metalc", metalc);//количество отсортированного мусора из категории металл
        ed.putInt("organicc", organicc);//количество отсортированного мусора из категории органика
        ed.putInt("notrecyclec", notrecyclec);//количество отсортированного мусора из категории электро
        ed.putInt("glassc", glassc);//количество отсортированного мусора из категории стекло
        ed.putInt("mistake", mistake);//количество ошибок
        ed.putInt("paperb", paperb);//бонус для категории бумага
        ed.putInt("plasticb", plasticb);//бонус для категории пластик
        ed.putInt("metalb", metalb);//бонус для категории металл
        ed.putInt("organicb", organicb);//бонус для категории органика
        ed.putInt("notrecycleb", notrecycleb);//бонус для категории электро
        ed.putInt("glassb", glassb);//бонус для категории стекло
        ed.putInt("multi", multi);//общий бонус(появляется по достижении 3 уровня по отдельности)
        ed.putString("code1", code1);//первый qr код
        ed.putString("code2", code2);//второй qr кол
        ed.putFloat("music", music);//настройка громкости фоновой музыки
        ed.putFloat("effects", effects);//настройка громкости кликов
        ed.apply();
    }

    public void init() {
        SharedPreferences sp = App.getInstance().getStorage();
        TSH = sp.getLong("TSH", 0);//очки TSH
        man = sp.getLong("man", 0);//количество уборщиков
        car = sp.getLong("car", 0);//количество мусоровозов
        robot = sp.getLong("robot", 0);//количество роботов
        factory = sp.getLong("factory", 0);//количество заводов
        paperc = sp.getInt("paperc", 0);//количество отсортированного мусора из категории бумага
        plasticc = sp.getInt("plasticc", 0);//количество отсортированного мусора из категории пластик
        metalc = sp.getInt("metalc", 0);//количество отсортированного мусора из категории металл
        organicc = sp.getInt("organicc", 0);//количество отсортированного мусора из категории органика
        notrecyclec = sp.getInt("notrecyclec", 0);//количество отсортированного мусора из категории электро
        glassc = sp.getInt("glassc", 0);//количество отсортированного мусора из категории стекло
        trash = plasticc + paperc + metalc + organicc + notrecyclec + glassc;//общее количество мусора
        mistake = sp.getInt("mistake", 0);//количество ошибок
        paperb = sp.getInt("paperb", 1);//бонус для категории бумага
        plasticb = sp.getInt("plasticb", 1);//бонус для категории пластик
        metalb = sp.getInt("metalb", 1);//бонус для категории металл
        organicb = sp.getInt("organicb", 1);//бонус для категории органика
        notrecycleb = sp.getInt("notrecycleb", 1);//бонус для категории электро
        glassb = sp.getInt("glassb", 1);//бонус для категории стекло
        multi = sp.getInt("multi", 1);//общий бонус(появляется по достижении 3 уровня по отдельности)
        code1 = sp.getString("code1", "0");//первый qr код
        code2 = sp.getString("code2", "0");//второй qr кол
        music = sp.getFloat("music", 1f);//настройка громкости фоновой музыки
        effects = sp.getFloat("effects", 1f);//настройка громкости кликов
        scale = 1 / getResources().getDisplayMetrics().density * 0.5f + getWindowManager().getDefaultDisplay().getHeight() * getWindowManager().getDefaultDisplay().getWidth() * 0.0000001f;
        //показатель площади для масштабирования шрифтов
        App.getInstance().clickPlayer.setVolume(effects, effects);
        App.getInstance().menuPlayer.setVolume(music, music);
    }

    @Override//выключение фоновой музыки при сворачивании и выключении экрана
    protected void onStop() {
        super.onStop();
        if ((newClass.getName()).equals(getClass().getName())) {
            App.getInstance().menuPlayer.pause();
            Log.d("tokens", "stopped " + getClass().getName());
        }
    }

    @Override
    protected void onStart() {//включение фоновой музыки при включении приложения
        super.onStart();
        Log.d("tokens", "started " + getClass().getName());
        App.getInstance().menuPlayer.start();
    }

    public void finish1() { //отключение музыки при выходе из активности
        App.getInstance().clickPlayer.start();
        finish();
    }

    public void showToast(long a) {//вызов всплывающего сообщения при нехватке баланса
        if (toast != null)//удаление предыдущего сообщения чтобы не заспамлять
            toast.cancel();
        StyleableToast t = StyleableToast.makeText(getApplicationContext(), "✘  Не хватает " + a + " TSH", R.style.wrong1);
        toast = t;
        t.show();
    }

    public void transfer(Class newClass) {//переход в новую активность
        if (notIntent) {//чтобы не создавалось лишних активностей, нельзя вызвать более одного раза
            notIntent = false;
            this.newClass = newClass;
            Intent intent = new Intent(this, newClass);
            startActivity(intent);
            finish1();
        }
    }

    public String getPrice(long s) {//масштабирование цены
        String newa = "" + s;
        if (newa.length() > 12)
            newa = newa.substring(0, newa.length() - 12) + "T";//триллионы
        else if (newa.length() > 9)
            newa = newa.substring(0, newa.length() - 9) + "B";//миллиарды(billions)
        else if (newa.length() > 6)
            newa = newa.substring(0, newa.length() - 6) + "M";//миллионы
        else if (newa.length() > 3)
            newa = newa.substring(0, newa.length() - 3) + "K";//тысячи(k - kilo)

        return newa;
    }
}
