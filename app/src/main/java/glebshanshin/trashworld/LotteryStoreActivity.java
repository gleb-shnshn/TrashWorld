package glebshanshin.trashworld;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class LotteryStoreActivity extends UniActivity {
    Intent intent;
    TextView TSHv, priceO, priceB, text;
    String m = "Случайное  TSH\nот ";
    long price, nprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lotterystore_main);
        scale = 1 / getResources().getDisplayMetrics().density * 0.5f + getWindowManager().getDefaultDisplay().getHeight() * getWindowManager().getDefaultDisplay().getWidth() * 0.0000001f;
        TSHv = findViewById(R.id.TSH);
        TSHv.setTextSize(scale * 75f);//масштабирование шрифта
        //инициализация TextView
        priceB = findViewById(R.id.price1);
        text = findViewById(R.id.priceB);
        priceO = findViewById(R.id.price);
        //установка масштабируемого размера текста
        text.setTextSize(scale * 55f);
        priceB.setTextSize(scale * 55f);
        priceO.setTextSize(scale * 55f);
        TextView a = findViewById(R.id.des1),
                b = findViewById(R.id.des2),
                c = findViewById(R.id.des3);
        a.setTextSize(scale * 55f);
        b.setTextSize(scale * 55f);
        c.setTextSize(scale * 55f);
        checkPrize();
        updateTSH();
        updatePrice();
        intent = new Intent(LotteryStoreActivity.this, LotteryActivity.class);
    }

    private void checkPrize() {//проверка приза по дополнительным данным из Intent
        long prize = Long.parseLong(getIntent().getExtras().getString("prize"));
        if (prize <= 4) {
            switch ((int) prize) {
                case 0:
                    break;
                case 1:
                    man++;
                    break;
                case 2:
                    car++;
                    break;
                case 3:
                    robot++;
                    break;
                case 4:
                    factory++;
                    break;
            }
        } else {
            TSH += prize;
        }
    }

    private void updatePrice() {//обновление цен
        price = ((man + 1) + ((car + 1) * 10) + ((robot + 1) * 50) + ((factory + 1) * 100)) / 4;//среднее арифметическое между предметами
        String newa = getPrice(price);
        priceO.setText("Цена: " + newa + " TSH");
        nprice = (TSH / 10) + 1000;// 1/10 баланса + 1000
        priceB.setText("Цена: " + getPrice(nprice) + " TSH");
        text.setText(m + getPrice(nprice / 2) + " до " + getPrice((long) (nprice * 1.5)));
    }

    private void update(SQLiteDatabase db) {//обновление базы данных при переходе в другую активность
        ContentValues newValues = new ContentValues();
        newValues.put("TSH", TSH);
        newValues.put("man", man);
        newValues.put("car", car);
        newValues.put("robot", robot);
        newValues.put("factory", factory);
        db.update("Data", newValues, "_id = 1", null);
    }

    private void updateTSH() {//обновление баланса
        String newa = getPrice(TSH);
        TSHv.setText(newa + " TSH ");
    }

    private void toast(long a) {//всплывающее сообщение если недостаточный баланс
        StyleableToast.makeText(getApplicationContext(), "✘  Не хватает " + a + " TSH", Toast.LENGTH_SHORT, R.style.wrong1).show();
    }

    public void toStore(View view) {//переход в класс магазина
        update(db);
        transfer(StoreActivity.class);
    }

    public void buyBronze(View view) {//покупка бронзовой лотерейки
        if (notIntent) {
            if (TSH >= nprice) {
                notIntent = false;
                TSH -= nprice;
                intent.putExtra("1", "bronzel");
                update(db);
                startActivity(intent);
                finish1();
            } else {
                toast(nprice - TSH);
            }
        }
    }

    public void buySilver(View view) {//покупка серебряной лотерейки
        if (notIntent) {
            if (TSH >= price) {
                notIntent = false;
                TSH -= price;
                intent.putExtra("1", "silverl");
                update(db);
                startActivity(intent);
                finish1();
            } else {
                toast(price - TSH);
            }
        }
    }

    public void buyGold(View view) {//покупка золотой лотерейки
        if (notIntent) {
            if (TSH >= 100000) {
                notIntent = false;
                TSH -= 100000;
                intent.putExtra("1", "goldl");
                update(db);
                startActivity(intent);
                finish1();
            } else {
                toast(100000 - TSH);
            }
        }
    }

    //выход в магазин через встроенную кнопку назад
    @Override
    public void onBackPressed() {
        update(db);
        transfer(StoreActivity.class);
        super.onBackPressed();
    }
}