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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.HashMap;
import java.util.UUID;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.promo_main);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        init(db);
    }

    private void update(SQLiteDatabase db) {
        ContentValues newValues = new ContentValues();
        newValues.put("TSH", TSHc);
        db.update("Data", newValues, "_id = 1", null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() != null) {
                if (smartCheck(result.getContents())) {
                    final String QR = result.getContents();
                    HashMap<String, String> postDataParams = new HashMap<String, String>();
                    postDataParams.put("code", QR);
                    Call<Object> call = che.performPostCall(postDataParams);
                    call.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            HashMap<String, String> map = gson.fromJson(response.body().toString(), HashMap.class);
                            if (map.get("success").equals("good")) {
                                delete(QR);
                            } else {
                                StyleableToast.makeText(getApplicationContext(), "Некорректный код", Toast.LENGTH_SHORT, R.style.wrong).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            StyleableToast.makeText(getApplicationContext(), "Нет доступа к интернету", Toast.LENGTH_SHORT, R.style.wrong).show();
                        }
                    });
                } else {
                    StyleableToast.makeText(getApplicationContext(), "Некорректный код", Toast.LENGTH_SHORT, R.style.wrong).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean smartCheck(String code) {
        if ((code.length() == 15)) {
            if ((code.substring(0, 2).equals("QR")) && (code.substring(13, 15).equals("QR"))) {
                return true;
            }
        }
        return false;
    }

    private void delete(String qr) {
        final String qr1 = qr;
        HashMap<String, String> postDataParams = new HashMap<String, String>();
        postDataParams.put("code", qr);
        Call<Object> call = del.performPostCall(postDataParams);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                decodeMoney(qr1);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                StyleableToast.makeText(getApplicationContext(), "Нет доступа к интернету", Toast.LENGTH_SHORT, R.style.wrong).show();
            }
        });
    }

    private void decodeMoney(String a) {
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
    }

    public void Scan(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(QRScanActivity.class);
        integrator.initiateScan();
    }

    public void Generate(View view) {
        Intent intent = new Intent(PromoActivity.this, StorageActivity.class);
        update(db);
        startActivity(intent);
        finish();
    }

    public void toSettings(View view) {
        Intent intent = new Intent(PromoActivity.this, SettingsActivity.class);
        startActivity(intent);
        update(db);
        finish();
    }

    public void init(SQLiteDatabase db) {
        cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        TSHc = cursor.getLong(1);
        cursor.close();
    }
}