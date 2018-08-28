package glebshanshin.trashworld;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StorageActivity extends UniActivity {
    LinearLayout lin1, lin2;
    TextView text1, text2;
    private final String server = "https://gleb2700.000webhostapp.com";
    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private check che = retrofit.create(check.class);
    private delete del = retrofit.create(delete.class);
    private int now;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_main);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        lin1 = findViewById(R.id.lin1);
        lin2 = findViewById(R.id.lin2);
        checkexistingall();//проверка на наличие в серверной базе данных обоих qr кодов
    }

    private void checkexistingall() {
        //проверка по отдельности
        if (!code1.equals("0")) {
            checkexisting(code1, 1);
        }
        if (!code2.equals("0")) {
            checkexisting(code2, 2);
        }
        if (code1.equals("0")) {
            lin1.setBackground(getDrawable(R.drawable.emptyqr));
        } else {
            lin1.setBackground(getDrawable(R.drawable.lookqr));
            text1.setText(Integer.parseInt(code1.substring(2, 5)) + code1.substring(5, 6) + "\nTSH");
        }
        if (code2.equals("0")) {
            lin2.setBackground(getDrawable(R.drawable.emptyqr));
        } else {
            lin2.setBackground(getDrawable(R.drawable.lookqr));
            text2.setText(Integer.parseInt(code2.substring(2, 5)) + code2.substring(5, 6) + "\nTSH");
        }
    }

    private void checkexisting(final String QR, final int num) {
        HashMap<String, String> postDataParams = new HashMap<String, String>();
        postDataParams.put("code", QR);
        Call<Object> call = che.performPostCall(postDataParams);//отправка запроса к серверу на проверку
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.body() != null) {
                    HashMap<String, String> map = gson.fromJson(response.body().toString(), HashMap.class);
                    if (!map.get("success").equals("good")) {//если нет на сервере значит удаляем локально
                        delete(num);
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                StyleableToast.makeText(getApplicationContext(), "Нет доступа к интернету", Toast.LENGTH_SHORT, R.style.wrong).show();
            }
        });
    }

    public void delete(int num) {
        if (num == 1) {
            code1 = "0";
            ContentValues newValues = new ContentValues();
            newValues.put("qr1", 0);
            db.update("Data", newValues, "_id = 1", null);
        } else {
            code2 = "0";
            ContentValues newValues = new ContentValues();
            newValues.put("qr2", 0);
            db.update("Data", newValues, "_id = 1", null);
        }
        transfer(StorageActivity.class);
    }

    public void reload(View view) {
        transfer(StorageActivity.class);
    }//обновляем активность тем самым запуская проверку с сервером

    public void toBack(View view) {
        transfer(PromoActivity.class);
    }//переход в класс промо-кодов

    public void show1(View view) {//по нажатию кнопку
        if (code1.equals("0")) {//если код не существует переход к активности создания кода
            if (notIntent) {
                notIntent = false;
                Intent intent = new Intent(StorageActivity.this, QRActivity.class);
                intent.putExtra("code", "1");
                startActivity(intent);
                finish1();
            }
        } else {//если существует то показ кода
            show(1);
        }
    }

    public void show2(View view) {
        if (code2.equals("0")) {//если код не существует переход к активности создания кода
            if (notIntent) {
                notIntent = false;
                Intent intent = new Intent(StorageActivity.this, QRActivity.class);
                intent.putExtra("code", "2");
                startActivity(intent);
                finish1();
            }
        } else {//если существует то показ кода
            show(2);
        }
    }

    private void show(int i) {//показ кода
        String code;
        if (i == 1) {
            code = code1;
        } else {
            code = code2;
        }
        setContentView(R.layout.show_main);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = null;
        try {
            bitmap = barcodeEncoder.encodeBitmap(code, BarcodeFormat.QR_CODE, 400, 400);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        ImageView imageViewQrCode = findViewById(R.id.place);
        imageViewQrCode.setImageBitmap(bitmap);
    }

    public void delete1(View view) {//удаление первого кода
        if (!code1.equals("0")) {//если существует
            setContentView(R.layout.check_main);//показ разметки с вопросом
            ImageView lin = findViewById(R.id.set);
            lin.setImageDrawable(getDrawable(R.drawable.checkpage2));
            now = 1;
        }
    }

    public void delete2(View view) {//удаление второго кода
        if (!code2.equals("0")) {//если существует
            setContentView(R.layout.check_main);//показ разметки с вопросом
            ImageView lin = findViewById(R.id.set);
            lin.setImageDrawable(getDrawable(R.drawable.checkpage2));
            now = 2;
        }
    }

    public void Yes(View view) {//если согласился удалить
        setContentView(R.layout.storage_main);
        checkexistingall();
        if (now == 1) {
            if (!code1.equals("0")) {
                obDelete(code1);
            }
        } else {
            if (!code2.equals("0")) {
                obDelete(code2);
            }
        }
    }

    private void obDelete(final String code) {
        HashMap<String, String> postDataParams = new HashMap<String, String>();
        postDataParams.put("code", code);
        Call<Object> call = del.performPostCall(postDataParams);//отправка запроса на удаление
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                //удаление и обновление активности
                delete(now);
                decodeMoney(code);
                transfer(StorageActivity.class);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                StyleableToast.makeText(getApplicationContext(), "Нет доступа к интернету", Toast.LENGTH_SHORT, R.style.wrong).show();
            }
        });
    }

    private void decodeMoney(String a) {//восстановление номинала кода
        long b = Integer.parseInt(a.substring(2, 5));
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
        b /= 2;//т.к. возвращаем только половину
        TSH += b;
        StyleableToast.makeText(this, "Вам вернули  " + getPrice(b) + " TSH", Toast.LENGTH_SHORT, R.style.get).show();
        ContentValues newValues = new ContentValues();
        newValues.put("TSH", TSH);
        db.update("Data", newValues, "_id = 1", null);
    }

    public void No(View view) {//если не согласился удалять
        transfer(StorageActivity.class);
    }

    //переход в класс промо-кодов с помощью встроенной кнопки назад
    @Override
    public void onBackPressed() {
        transfer(PromoActivity.class);
        super.onBackPressed();
    }
}