package glebshanshin.trashworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.clock.scratch.ScratchView;

import java.util.Random;

public class LotteryActivity extends Activity {
    ScratchView sc;
    TextView tv, tsh;
    Button btn, back;
    ImageView img;
    int obj;
    private boolean isOpen = false, isBack = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.lottery_main);
        sc = findViewById(R.id.scratch_view);
        String got = getIntent().getExtras().getString("1");
        sc.setWatermark(getResources().getIdentifier(got, "drawable", getPackageName()));
        Random random = new Random();
        if (got.equals("silverl")) {
            img = findViewById(R.id.img);
            obj = random.nextInt(4) + 1;
            switch (obj) {
                case 1:
                    img.setImageDrawable(getDrawable(R.drawable.man));
                    break;
                case 2:
                    img.setImageDrawable(getDrawable(R.drawable.car));
                    break;
                case 3:
                    img.setImageDrawable(getDrawable(R.drawable.robot));
                    break;
                case 4:
                    img.setImageDrawable(getDrawable(R.drawable.factory));
                    break;
            }
        } else if (got.equals("bronzel")) {
            obj = random.nextInt(1500) + 500;
            tsh = findViewById(R.id.TSH);
            tsh.setText(obj + " TSH");
        } else if (got.equals("goldl")) {
            img = findViewById(R.id.img2);
            obj = random.nextInt(6) + 5;
            tsh = findViewById(R.id.TSH);
            switch (obj) {
                case 5:
                    img.setImageDrawable(getDrawable(R.drawable.glassstat));
                    break;
                case 6:
                    img.setImageDrawable(getDrawable(R.drawable.metalstat));
                    break;
                case 7:
                    img.setImageDrawable(getDrawable(R.drawable.paperstat));
                    break;
                case 8:
                    img.setImageDrawable(getDrawable(R.drawable.organicstat));
                    break;
                case 9:
                    img.setImageDrawable(getDrawable(R.drawable.notrecyclestat));
                    break;
                case 10:
                    img.setImageDrawable(getDrawable(R.drawable.plasticstat));
                    break;
            }
            tsh.setText("\n     X2");
        }
        sc.setMaskColor(0x00000000);
        sc.setMaxPercent(80);
        btn = findViewById(R.id.button);
        back = findViewById(R.id.back);
        btn.setBackgroundColor(getResources().getColor(R.color.alpha1));
        back.setBackgroundColor(getResources().getColor(R.color.alpha1));
        sc.setEraseStatusListener(new ScratchView.EraseStatusListener() {
            @Override
            public void onProgress(int percent) {
            }

            @Override
            public void onCompleted(View view) {
                btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.skip));
                isOpen = true;
            }
        });
    }

    public void skip(View view) {
        if (isOpen) {
            sc.clear();
            isOpen = false;
            btn.setBackgroundColor(getResources().getColor(R.color.alpha1));
            back.setBackgroundDrawable(getResources().getDrawable(R.drawable.backbut1));
            isBack = true;
        }
    }

    public void toBack(View view) {
        if (isBack) {
            Intent intent = new Intent(LotteryActivity.this, LotteryStoreActivity.class);
            intent.putExtra("prize", obj + "");
            startActivity(intent);
            finish();
        }
    }
}