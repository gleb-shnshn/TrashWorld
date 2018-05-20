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
    boolean notIntent = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.qr);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        init(db);
        final TextView textView = findViewById(R.id.textView2);
        seekbar = findViewById(R.id.discrete1);
        seekbar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
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

    private String getPrice(long s) {
        String newa = "" + s;
        if (newa.length() > 12)
            newa = newa.substring(0, newa.length() - 9) + "T";
        else if (newa.length() > 9)
            newa = newa.substring(0, newa.length() - 9) + "B";
        else if (newa.length() > 6)
            newa = newa.substring(0, newa.length() - 6) + "M";
        else if (newa.length() > 3)
            newa = newa.substring(0, newa.length() - 3) + "K";

        return newa;
    }

    public void init(SQLiteDatabase db) {
        cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        TSHc = cursor.getLong(1);
        textView = findViewById(R.id.TSH);
        textView.setText("" + getPrice(TSHc) + " TSH");
        cursor.close();
    }

    public void toBack(View view) {
        if (notIntent) {
            notIntent = false;
            Intent intent = new Intent(QRActivity.this, StorageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void Buy(View view) {
        int value = seekbar.getProgress();
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
            TSHc -= money;
            textView.setText("" + getPrice(TSHc) + " TSH");
            generate(code);
        } else {
            StyleableToast.makeText(getApplicationContext(), "✘  Не хватает " + (money - TSHc) + " TSH", Toast.LENGTH_SHORT, R.style.wrong1).show();
        }
    }

    private void generate(String money) {
        code = "QR" + money + UUID.randomUUID().toString().substring(0, 7) + "QR";

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(code, BarcodeFormat.QR_CODE, 400, 400);
            ImageView imageViewQrCode = findViewById(R.id.imageView);
            imageViewQrCode.setImageBitmap(bitmap);
            insert(code);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void insert(String qr) {
        HashMap<String, String> postDataParams = new HashMap<String, String>();
        postDataParams.put("code", qr);
        Call<Object> call = ins.performPostCall(postDataParams);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (notIntent) {
                    notIntent = false;
                    update(db);
                    Intent intent = new Intent(QRActivity.this, StorageActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                StyleableToast.makeText(getApplicationContext(), "Нет доступа к интернету", Toast.LENGTH_SHORT, R.style.wrong).show();
            }
        });
    }

    private void update(SQLiteDatabase db) {
        ContentValues newValues = new ContentValues();
        newValues.put("TSH", TSHc);
        newValues.put("qr" + getIntent().getStringExtra("code"), code);
        db.update("Data", newValues, "_id = 1", null);
    }
}
