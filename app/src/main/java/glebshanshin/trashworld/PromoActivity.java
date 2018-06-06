package glebshanshin.trashworld;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PromoActivity extends Activity {
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    long TSHc;
    private final String server = "https://gleb2700.000webhostapp.com";
    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private check che = retrofit.create(check.class);
    private delete del = retrofit.create(delete.class);
    boolean notIntent = true;
    MediaPlayer menuPlayer;
    float music, effects;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.promo_main);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        init(db);
    }

    private void update(SQLiteDatabase db) {//обновление базы данных
        ContentValues newValues = new ContentValues();
        newValues.put("TSH", TSHc);
        db.update("Data", newValues, "_id = 1", null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//ожидание ответа от сканера
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                if (smartCheck(result.getContents())) {//если прошло быструю проверку
                    final String QR = result.getContents();
                    HashMap<String, String> postDataParams = new HashMap<String, String>();
                    postDataParams.put("code", QR);
                    Call<Object> call = che.performPostCall(postDataParams);//проверка на наличие QR в серверной базе данных
                    call.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            HashMap<String, String> map = gson.fromJson(response.body().toString(), HashMap.class);
                            if (map.get("success").equals("good")) {//если есть посылаем запрос на удаление
                                delete(QR);
                            } else {//если нет то неккоректный код
                                StyleableToast.makeText(getApplicationContext(), "Некорректный код", Toast.LENGTH_SHORT, R.style.wrong).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {//если ошибка с доступом
                            StyleableToast.makeText(getApplicationContext(), "Нет доступа к интернету", Toast.LENGTH_SHORT, R.style.wrong).show();
                        }
                    });
                } else {//если не прошло быструю проверку
                    StyleableToast.makeText(getApplicationContext(), "Некорректный код", Toast.LENGTH_SHORT, R.style.wrong).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean smartCheck(String code) {//быстрая проверка
        // код формируется длиной 15 - QR потом номинал - 4 знака, потом семизначный случайный набор и опять QR
        // QR999KaaaaaaaQR
        if ((code.length() == 15)) {
            if ((code.substring(0, 2).equals("QR")) && (code.substring(13, 15).equals("QR"))) {
                return true;
            }
        }
        return false;
    }

    private void delete(String qr) {//запрос на удаление
        final String qr1 = qr;
        HashMap<String, String> postDataParams = new HashMap<String, String>();
        postDataParams.put("code", qr);
        Call<Object> call = del.performPostCall(postDataParams);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {//если удаление удачно
                decodeMoney(qr1);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {//если нет доступа
                StyleableToast.makeText(getApplicationContext(), "Нет доступа к интернету", Toast.LENGTH_SHORT, R.style.wrong).show();
            }
        });
    }

    private void decodeMoney(String a) {//восстановление количества денег по номиналу
        long t = Integer.parseInt(a.substring(2, 5));
        long b = t;
        switch (a.substring(5, 6)) {
            case "K":
                b *= 1000;
                break;
            case "M":
                b *= 1000000;
                break;
            case "B":
                b *= 1000000000;
                break;
        }
        TSHc += b;
        StyleableToast.makeText(this, "Вы получили " + t + a.substring(5, 6) + " TSH", Toast.LENGTH_SHORT, R.style.get).show();
        update(db);
    }

    public void Scan(View view) {//кнопка сканирования
        menuPlayer.stop();
        menuPlayer = MediaPlayer.create(this, R.raw.click);
        menuPlayer.setVolume(effects, effects);
        menuPlayer.setLooping(false);
        menuPlayer.start();
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(QRScanActivity.class);
        integrator.initiateScan();
    }

    public void Generate(View view) {//переход в класс хранения своих QR кодов
        if (notIntent) {
            notIntent = false;
            Intent intent = new Intent(PromoActivity.this, StorageActivity.class);
            update(db);
            startActivity(intent);
            finish1();
        }
    }

    public void toSettings(View view) {//переход в класс настроек
        if (notIntent) {
            notIntent = false;
            Intent intent = new Intent(PromoActivity.this, SettingsActivity.class);
            startActivity(intent);
            update(db);
            finish1();
        }
    }

    public void init(SQLiteDatabase db) { //получение данных из базы данных
        cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        TSHc = cursor.getLong(1);
        music = cursor.getFloat(22);
        effects = cursor.getFloat(23);
        cursor.close();
    }

    private void finish1() {//выключение музыки при переходе в другую активность
        menuPlayer.stop();
        menuPlayer = MediaPlayer.create(this, R.raw.click);
        menuPlayer.setVolume(effects, effects);
        menuPlayer.setLooping(false);
        menuPlayer.start();
        finish();
    }

    //включение и отключение музыки при выключении и выключении приложения
    @Override
    protected void onStop() {
        super.onStop();
        menuPlayer.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        menuPlayer = MediaPlayer.create(this, R.raw.menu);
        menuPlayer.setVolume(music, music);
        menuPlayer.setLooping(true);
        menuPlayer.start();
    }

    //переход в настройки с помощью встроенной кнопки назад
    @Override
    public void onBackPressed() {
        if (notIntent) {
            notIntent = false;
            Intent intent1 = new Intent(PromoActivity.this, SettingsActivity.class);
            startActivity(intent1);
            update(db);
            finish1();
        }
        super.onBackPressed();
    }
}