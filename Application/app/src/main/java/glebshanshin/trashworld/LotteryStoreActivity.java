package glebshanshin.trashworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LotteryStoreActivity extends UniActivity {
    Intent intent;
    TextView TSHv, priceO, priceB, text;
    String m = "Случайное  TSH\nот ";
    long price, nprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lotterystore_main);
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
        String prizeString = getIntent().getStringExtra("prize");
        if (prizeString == null)
            return;
        long prize = Long.parseLong(prizeString);
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
        update();
    }

    private void updatePrice() {//обновление цен
        price = ((man + 1) + ((car + 1) * 10) + ((robot + 1) * 50) + ((factory + 1) * 100)) / 4;//среднее арифметическое между предметами
        String newa = getPrice(price);
        priceO.setText("Цена: " + newa + " TSH");
        nprice = (TSH / 10) + 1000;// 1/10 баланса + 1000
        priceB.setText("Цена: " + getPrice(nprice) + " TSH");
        text.setText(m + getPrice(nprice / 2) + " до " + getPrice((long) (nprice * 1.5)));
        //установка заблокированных(серых) иконок
        if (TSH < nprice)
            findViewById(R.id.buybronze).setBackground(getDrawable(R.drawable.smartbutstoreb));
        if (TSH < price)
            findViewById(R.id.buysilver).setBackground(getDrawable(R.drawable.smartbutstoreb));
        if (TSH < 100000)
            findViewById(R.id.buygold).setBackground(getDrawable(R.drawable.smartbutstoreb));
    }

    private void updateTSH() {//обновление баланса
        String newa = getPrice(TSH);
        TSHv.setText(newa + " TSH ");
    }

    public void toStore(View view) {//переход в класс магазина
        update();
        transfer(StoreActivity.class);
    }

    public void buyBronze(View view) {//покупка бронзовой лотерейки
        buyLottery("bronzel", nprice);
    }

    public void buySilver(View view) {//покупка серебряной лотерейки
        buyLottery("silverl", price);
    }

    public void buyGold(View view) {//покупка золотой лотерейки
        buyLottery("goldl", 100000);
    }

    public void buyLottery(String name, long priceOfLottery) {
        if (notIntent) {
            if (TSH >= priceOfLottery) {
                notIntent = false;
                TSH -= priceOfLottery;
                intent.putExtra("lottery", name);
                newClass = LotteryActivity.class;
                update();
                startActivity(intent);
                finish1();
            } else {
                showToast(priceOfLottery - TSH);
            }
        }
    }

    //выход в магазин через встроенную кнопку назад
    @Override
    public void onBackPressed() {
        update();
        transfer(StoreActivity.class);
        super.onBackPressed();
    }
}