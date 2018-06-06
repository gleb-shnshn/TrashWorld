package glebshanshin.trashworld;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class QRActivity extends Activity {
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    String code;
    private final String server = "https://gleb2700.000webhostapp.com";
    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    DiscreteSeekBar seekbar;
    private insert ins = retrofit.create(insert.class);
    long TSHc;
    TextView textView;
    boolean notIntent = true, notBlock=true;
    boolean qu = false;
    float scale;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.qr_main);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        scale = 1 / getResources().getDisplayMetrics().density * 0.5f + getWindowManager().getDefaultDisplay().getHeight() * getWindowManager().getDefaultDisplay().getWidth() * 0.0000001f;
        init(db);
        final TextView textView = findViewById(R.id.textView2);
        textView.setTextSize(scale * 40f);//масштабирование шрифта
        seekbar = findViewById(R.id.discrete1);
        seekbar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {//изменение номинала
                String k = "Номинал: ";
                if (value < 1000) {
                    textView.setText(k + value + "K TSH");
                } else if (value < 2000) {
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

    private String getPrice(long s) { //масштабирование номинала
        String newa = "" + s;
        if (newa.length() > 12)
            newa = newa.substring(0, newa.length() - 12) + "T";
        else if (newa.length() > 9)
            newa = newa.substring(0, newa.length() - 9) + "B";
        else if (newa.length() > 6)
            newa = newa.substring(0, newa.length() - 6) + "M";
        else if (newa.length() > 3)
            newa = newa.substring(0, newa.length() - 3) + "K";

        return newa;
    }

    public void init(SQLiteDatabase db) {//получение данных из базы данных
        cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        TSHc = cursor.getLong(1);
        textView = findViewById(R.id.TSH);
        textView.setText("" + getPrice(TSHc) + " TSH");
        textView.setTextSize(scale * 74f);//масштабирование шрифта
        cursor.close();
    }

    public void toBack(View view) {//переход в класс хранения кодов
        if (notIntent) {
            notIntent = false;
            Intent intent = new Intent(QRActivity.this, StorageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //включение и отключение музыки при выключении и выключении приложения
    @Override
    public void onBackPressed() {
        if (notBlock) {
            notIntent = false;
            Intent intent1 = new Intent(QRActivity.this, StorageActivity.class);
            startActivity(intent1);
            super.onBackPressed();
        }
    }

    public void Buy(View view) {//покупка нового кода
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
        if (TSHc >= money) {
            generate(code, money);//если хватает денег генерация кода
        } else {
            StyleableToast.makeText(getApplicationContext(), "✘  Не хватает " + (money - TSHc) + " TSH", Toast.LENGTH_SHORT, R.style.wrong1).show();
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
                    TSHc -= money;
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
                if (notIntent) {
                    notIntent = false;
                    Intent intent = new Intent(QRActivity.this, StorageActivity.class);
                    startActivity(intent);
                    finish();
                }
                StyleableToast.makeText(getApplicationContext(), "Нет доступа к интернету", Toast.LENGTH_SHORT, R.style.wrong).show();
            }
        });

    }

    private void update(SQLiteDatabase db) { //обновление базы данных при переходе в другую активность
        ContentValues newValues = new ContentValues();
        newValues.put("TSH", TSHc);
        newValues.put("qr" + getIntent().getStringExtra("code"), code);
        db.update("Data", newValues, "_id = 1", null);
    }

    public void reload(View view) {//переход в класс хранения промо-кодов
        if (qu && notIntent) {
            notIntent = false;
            Intent intent = new Intent(QRActivity.this, StorageActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
