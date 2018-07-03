package glebshanshin.trashworld;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
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

public class PromoActivity extends UniActivity {
    private final String server = "https://gleb2700.000webhostapp.com";
    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private delete del = retrofit.create(delete.class);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promo_main);
    }

    private void update(SQLiteDatabase db) {//обновление базы данных
        ContentValues newValues = new ContentValues();
        newValues.put("TSH", TSH);
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
                    Call<Object> call = del.performPostCall(postDataParams);//проверка на наличие QR в серверной базе данных
                    call.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            HashMap<String, String> map = gson.fromJson(response.body().toString(), HashMap.class);
                            if (map.get("success").equals("good")) {//если есть посылаем запрос на удаление
                                decodeMoney(QR);
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
        TSH += b;
        StyleableToast.makeText(this, "Вы получили " + t + a.substring(5, 6) + " TSH", Toast.LENGTH_SHORT, R.style.get).show();
        update(db);
    }

    public void Scan(View view) {//кнопка сканирования
        clickPlayer.start();
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(QRScanActivity.class);
        integrator.initiateScan();
    }

    public void Generate(View view) {//переход в класс хранения своих QR кодов
        update(db);
        transfer(StorageActivity.class);
    }

    public void toSettings(View view) {//переход в класс настроек
        update(db);
        transfer(SettingsActivity.class);
    }

    //переход в настройки с помощью встроенной кнопки назад
    @Override
    public void onBackPressed() {
        update(db);
        transfer(SettingsActivity.class);
        super.onBackPressed();
    }
}