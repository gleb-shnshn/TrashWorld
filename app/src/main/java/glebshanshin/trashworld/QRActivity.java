package glebshanshin.trashworld;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.HashMap;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QRActivity extends UniActivity {
    String code;
    private final String server = "https://gleb2700.000webhostapp.com";
    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    DiscreteSeekBar seekbar;
    private insert ins = retrofit.create(insert.class);
    TextView textView;
    boolean notBlock=true;
    boolean qu = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_main);
        textView = findViewById(R.id.TSH);
        textView.setText("" + getPrice(TSH) + " TSH");
        textView.setTextSize(scale * 74f);
        final TextView textView = findViewById(R.id.textView2);
        textView.setTextSize(scale * 40f);//масштабирование шрифта
        seekbar = findViewById(R.id.discrete1);
        seekbar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {//изменение номинала
                String k = "Номинал: ";
                if (value < 1001) {
                    textView.setText(k + value + "K TSH");
                } else if (value < 2001) {
                    textView.setText(k + value % 1000 + "M TSH");
                } else {
                    textView.setText(k + value % 1000 + "B TSH");
                }
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
            }
        });
    }

    public void toBack(View view) {
       transfer(StorageActivity.class);
    }//переход в класс хранения кодов

    //включение и отключение музыки при выключении и выключении приложения
    @Override
    public void onBackPressed() {
        if (notBlock) {
            transfer(StorageActivity.class);
            super.onBackPressed();
        }
    }

    public void Buy(View view) {//покупка нового кода
        clickPlayer.start();
        long value = seekbar.getProgress();
        String code;
        long money;
        if (value <= 1000) {
            code = value + "K";
            money = value * 1000;
        } else if (value <= 2000) {
            code = value % 1000 + "M";
            money = (value % 1000) * 1000000;
        } else {
            code = value % 1000 + "B";
            money = (value % 1000) * 1000000000;
        }
        while (code.length() < 4) {
            code = "0" + code;
        }
        if (TSH >= money) {
            generate(code, money);//если хватает денег генерация кода
        } else {
            toast(money - TSH);
        }
    }

    private void generate(final String qr, final long money) {
        setContentView(R.layout.show_main);//включение разметки показа
        notBlock=false;//заблокировать переход назад
        final Button button8 = findViewById(R.id.button8);
        code = "QR" + qr + UUID.randomUUID().toString().substring(0, 7) + "QR";//генерация кода
        button8.setAlpha(0);
        HashMap<String, String> postDataParams = new HashMap<String, String>();
        postDataParams.put("code", code);
        Call<Object> call = ins.performPostCall(postDataParams);
        call.enqueue(new Callback<Object>() {//формирование запроса на создание кода
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (notIntent) {
                    TSH -= money;
                    update(db);
                    try {//показ кода
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.encodeBitmap(code, BarcodeFormat.QR_CODE, 400, 400);
                        ImageView imageViewQrCode = findViewById(R.id.place);
                        imageViewQrCode.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                    button8.setAlpha(1);
                    qu = true;
                    notBlock=true;
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                transfer(StorageActivity.class);//возвращение в класс хранения кодов и вызов сообщения об отсустствии интернета
                StyleableToast.makeText(getApplicationContext(), "Нет доступа к интернету", Toast.LENGTH_SHORT, R.style.wrong).show();
            }
        });

    }

    private void update(SQLiteDatabase db) { //обновление базы данных при переходе в другую активность
        ContentValues newValues = new ContentValues();
        newValues.put("TSH", TSH);
        newValues.put("qr" + getIntent().getStringExtra("code"), code);
        db.update("Data", newValues, "_id = 1", null);
    }

    public void reload(View view) {//переход в класс хранения промо-кодов
        if (qu) {
            transfer(StorageActivity.class);//этот метод нужен при показе qr кода после его генерации
        }
    }
}
