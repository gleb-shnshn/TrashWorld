package glebshanshin.trashworld;

import android.app.Activity;
import android.content.Intent;
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

import java.util.HashMap;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PromoActivity extends Activity {
    private final String server = "https://gleb2700.000webhostapp.com";
    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(server)
            .build();
    private check che = retrofit.create(check.class);
    private delete del = retrofit.create(delete.class);
    private insert ins = retrofit.create(insert.class);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.promo_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
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
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "bad qr", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void delete(String qr) {
        final String qr1=qr;
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
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void insert(String qr) {
        final String qr1=qr;
        HashMap<String, String> postDataParams = new HashMap<String, String>();
        postDataParams.put("code", qr);
        Call<Object> call = ins.performPostCall(postDataParams);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void decodeMoney(String a) {
        long b = Integer.parseInt(a.substring(2, 5));
        switch (a.substring(5,6)) {
            case "K":
                b *= 1000;
                break;
            case "M":
                b *= 1000000;
                break;
            case "B":
                b*=1000000000;
                break;
        }
        Toast.makeText(getApplicationContext(), ""+b, Toast.LENGTH_SHORT).show();
    }

    public void Scan(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(QRScanActivity.class);
        integrator.initiateScan();
    }

    public void Generate(View view) throws WriterException {
        setContentView(R.layout.qr);
        String code ="QR100K"+ UUID.randomUUID().toString().substring(0,7)+"QR";
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.encodeBitmap(code, BarcodeFormat.QR_CODE, 400, 400);
        ImageView imageViewQrCode =findViewById(R.id.imageView);
        imageViewQrCode.setImageBitmap(bitmap);
        Toast.makeText(this,code,Toast.LENGTH_SHORT).show();
        insert(code);
    }
    public void toBack(View view){
        setContentView(R.layout.promo_main);

    }

    public void toSettings(View view) {
        Intent intent = new Intent(PromoActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }
}