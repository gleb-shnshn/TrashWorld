package glebshanshin.trashworld;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class UniActivity extends Activity {//Общая активность в которой описаны общие методы и поля
    long factory, robot, car, man, TSH;
    int organicc, plasticc, metalc, glassc, notrecyclec, paperc, mistake, trash, multi;
    int paperb, plasticb, metalb, organicb, notrecycleb, glassb;
    float music, effects, scale;
    String code1, code2;
    StyleableToast p;
    SQLiteDatabase db;
    DBHelper dbHelper;
    Cursor cursor;
    MediaPlayer menuPlayer, clickPlayer;//плееры отдельно для кликов и фоновой музыки
    boolean notIntent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        init(db);//получение данных из базы данных
        clickPlayer = MediaPlayer.create(this, R.raw.click);//настройка плеера для кликов
        clickPlayer.setVolume(effects, effects);
    }

    public void init(SQLiteDatabase db) {
        cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        TSH = cursor.getLong(1);//очки TSH
        man = cursor.getLong(2);//количество уборщиков
        car = cursor.getLong(3);//количество мусоровозов
        robot = cursor.getLong(4);//количество роботов
        factory = cursor.getLong(5);//количество заводов
        paperc = cursor.getInt(6);//количество отсортированного мусора из категории бумага
        plasticc = cursor.getInt(7);//количество отсортированного мусора из категории пластик
        metalc = cursor.getInt(8);//количество отсортированного мусора из категории металл
        organicc = cursor.getInt(9);//количество отсортированного мусора из категории органика
        notrecyclec = cursor.getInt(10);//количество отсортированного мусора из категории электро
        glassc = cursor.getInt(11);//количество отсортированного мусора из категории стекло
        trash = plasticc + paperc + metalc + organicc + notrecyclec + glassc;//общее количество мусора
        mistake = cursor.getInt(12);//количество ошибок
        paperb = cursor.getInt(13);//бонус для категории бумага
        plasticb = cursor.getInt(14);//бонус для категории пластик
        metalb = cursor.getInt(15);//бонус для категории металл
        organicb = cursor.getInt(16);//бонус для категории органика
        notrecycleb = cursor.getInt(17);//бонус для категории электро
        glassb = cursor.getInt(18);//бонус для категории стекло
        multi = cursor.getInt(19);//общий бонус(появляется по достижении 3 уровня по отдельности)
        code1 = cursor.getString(20);//первый qr код
        code2 = cursor.getString(21);//второй qr кол
        music = cursor.getFloat(22);//настройка громкости фоновой музыки
        effects = cursor.getFloat(23);//настройка громкости кликов
        scale = 1 / getResources().getDisplayMetrics().density * 0.5f + getWindowManager().getDefaultDisplay().getHeight() * getWindowManager().getDefaultDisplay().getWidth() * 0.0000001f;
        //показатель площади для масштабирования шрифтов
        cursor.close();
    }

    @Override//выключение фоновой музыки при сворачивании и выключении экрана
    protected void onStop() {
        super.onStop();
        menuPlayer.stop();
    }

    @Override
    protected void onStart() {//включение фоновой музыки при включении приложения
        super.onStart();
        menuPlayer = MediaPlayer.create(this, R.raw.menu);
        menuPlayer.setVolume(music, music);
        menuPlayer.setLooping(true);
        menuPlayer.start();
    }

    public void finish1() { //отключение музыки при выходе из активности
        menuPlayer.stop();
        clickPlayer.start();
        finish();
    }
    public void toast(long a) {//вызов всплывающего сообщения при нехватке баланса
        if (p != null)//удаление предыдущего сообщения чтобы не заспамлять
            p.cancel();
        StyleableToast t = StyleableToast.makeText(getApplicationContext(), "✘  Не хватает " + a + " TSH", Toast.LENGTH_SHORT, R.style.wrong1);
        p = t;
        t.show();
    }
    public void transfer(Class class1) {//переход в новую активность
        if (notIntent) {//чтобы не создавалось лишних активностей, нельзя вызвать более одного раза
            notIntent = false;
            Intent intent = new Intent(this, class1);
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
