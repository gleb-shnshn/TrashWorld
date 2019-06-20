package glebshanshin.trashworld;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlayActivity extends UniActivity implements OnTouchListener {
    long mills = 500L;
    Vibrator vibrator;
    long Adder;
    String id = "1";
    TextView TSHv, TSHsv;
    int offset_x = 0;
    int offset_y = 0;
    boolean touchFlag = false;//переменная обозначающая наличие касания
    boolean dropFlag = false;//переменная обозначающая наличие касания в каком то из секторов
    LayoutParams imageParams;
    ImageView circle, trash;
    TextView plastic, glass, metal, organic, notrecycle, paper;
    String choice = "null", tag, tsh, postfix = " TSH";
    int eX, eY;
    int w, h;
    View root;

    public void increaseTSH(long Adder) {//увеличение очков
        //учет бонусов для каждого вида
        if (choice.equals("org")) {
            Adder *= organicb;
        } else if (choice.equals("ele")) {
            Adder *= notrecycleb;
        } else if (choice.equals("pap")) {
            Adder *= paperb;
        } else if (choice.equals("pla")) {
            Adder *= plasticb;
        } else if (choice.equals("gla")) {
            Adder *= glassb;
        } else if (choice.equals("met")) {
            Adder *= metalb;
        }
        TSH += Adder;
        String newa = getPrice(TSH);
        tsh = newa + postfix;
        TSHv.setText(tsh);

    }

    public void toMenu(View view) {//выход в главное меню
        update();
        transfer(MainActivity.class);
    }

    private void incCounter(String choice) {//изменение числа правильно отсортированного мусора
        switch (choice) {
            case "pap":
                paperc++;
                break;
            case "pla":
                plasticc++;
                break;
            case "met":
                metalc++;
                break;
            case "ele":
                notrecyclec++;
                break;
            case "org":
                organicc++;
                break;
            case "gla":
                glassc++;
                break;
        }
    }

    public void toStore(View view) {//переход в класс магазина
        update();
        transfer(StoreActivity.class);
    }

    public void game() {//метод для проверки правильности выбора сектора
        if (tag.equals(choice)) {
            if (Integer.parseInt(id) == 19 || Integer.parseInt(id) == 15)//появление фактов
                new UniPopup.Builder(PlayActivity.this, getString(getResources().getIdentifier("fact" + ((int) (Math.random() * 19) + 1), "string", getPackageName())), true).build().show();
            increaseTSH(Adder);
            incCounter(choice);
        } else {
            vibrator.vibrate(mills);//вибрация при неправильном выборе
            mistake++;
            //показ сообщения об ошибке
            new UniPopup.Builder(PlayActivity.this, getString(R.string.category)+" "+getString(getResources().getIdentifier(tag, "string", getPackageName())), false).build().show();
        }
        choice = "null";
        newTrash();
    }

    private String getTag(String id) {//определение вида мусора по номеру
        int id1 = Integer.parseInt(id);
        if ((id1 >= 1) & (id1 <= 3)) {
            return "gla";
        } else if ((id1 >= 4) & (id1 <= 9)) {
            return "met";
        } else if ((id1 >= 10) & (id1 <= 19)) {
            return "pap";
        } else if ((id1 >= 20) & (id1 <= 25)) {
            return "pla";
        } else if ((id1 >= 26) & (id1 <= 29)) {
            return "org";
        } else if ((id1 >= 30) & (id1 <= 33)) {
            return "ele";
        } else {
            return "none";
        }
    }

    private boolean check(TextView i) {//проверка на выбор сектора
        int topY = i.getTop();
        int leftX = i.getLeft();
        int rightX = i.getRight();
        int bottomY = i.getBottom();
        if (eX > leftX && eX < rightX && eY > topY && eY < bottomY) {
            if (!choice.equals("" + i.getTag())) {//проверка на повтор, чтобы не устанавливать одинаковую картинку по несколько раз
                App.getInstance().clickPlayer.start();
                circle.setImageDrawable(getDrawable(getResources().getIdentifier(i.getTag() + "", "drawable", getPackageName())));
                dropFlag = true;
                choice = "" + i.getTag();
            }
            return true;
        }
        return false;
    }

    public void newTrash() {//генерация нового мусора
        int id1 = ((int) (Math.random() * 32) + 1);
        if (id1 == Integer.parseInt(id)) {//если одинаковый мусор то прибавляем два к номеру мусора
            id1 = id1 + 2;
            if (id1 > 33) {// если после этого номер больше чем количество мусора, то отнимаем пять
                id1 = id1 - 5;
            }
        }
        id = id1 + "";
        tag = getTag(id);
        trash.setImageDrawable(getDrawable(getResources().getIdentifier("trash" + id, "drawable", getPackageName())));
    }

    private void initTSHs() {//инициализация "увеличителя", т.е. количество TSH/мусор
        Adder = 1 + man + car * 10 + robot * 50 + factory * 100;
        Adder *= multi;
        String newa = getPrice(Adder);
        TSHsv.setText(newa + " TSH/мусор");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_main);
        init1();
        //масштабирование шрифтов
        TSHv.setTextSize(scale * 65f);
        TSHsv.setTextSize(scale * 65f);
        initTSHs();
        increaseTSH(0);
        newTrash();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //получение размера экрана
        w = getWindowManager().getDefaultDisplay().getWidth() - 50;
        h = getWindowManager().getDefaultDisplay().getHeight() - 10;
        trash.setOnTouchListener(this);
        root.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (touchFlag) { //если есть касание
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_MOVE:
                            eX = (int) event.getX();
                            eY = (int) event.getY();
                            int x = (int) event.getX() - offset_x;
                            int y = (int) event.getY() - offset_y;
                            if (x > w) x = w;
                            if (y > h) y = h;
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(trash.getWidth(), trash.getHeight()));
                            lp.setMargins(x, y, 0, 0);//установка отступов у мусора согласно касанию
                            if (!((check(plastic)) | check(metal) | check(glass) | check(organic) | check(notrecycle) | check(paper)) && !choice.equals("")) {
                                choice = "";
                                circle.setImageDrawable(getDrawable(R.color.alpha1));
                                dropFlag = false;
                            }//проверка всех TextView
                            trash.setLayoutParams(lp);
                            break;
                        case MotionEvent.ACTION_UP:
                            touchFlag = false;
                            if (dropFlag) {
                                dropFlag = false;
                                circle.setImageDrawable(getDrawable(R.color.alpha1));
                                game();
                                trash.setLayoutParams(imageParams);
                            } else {
                                trash.setLayoutParams(imageParams);
                            }
                            break;
                        default:
                            break;
                    }
                }
                return true;
            }
        });
    }

    private void init1() {//инициализация ImageView и TextView
        circle = findViewById(R.id.circle);
        root = findViewById(android.R.id.content).getRootView();
        plastic = findViewById(R.id.plastic);
        glass = findViewById(R.id.glass);
        metal = findViewById(R.id.metal);
        organic = findViewById(R.id.organic);
        notrecycle = findViewById(R.id.notrecycle);
        paper = findViewById(R.id.paper);
        trash = findViewById(R.id.img);
        TSHv = findViewById(R.id.TSH);
        TSHsv = findViewById(R.id.TSHs);
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchFlag = true;
                offset_x = (int) event.getX();
                offset_y = (int) event.getY();
                imageParams = v.getLayoutParams();
                break;
            case MotionEvent.ACTION_UP:
                touchFlag = false;
                break;
            default:
                break;
        }
        return false;
    }

    //выход в главное меню через встроенную кнопку назад
    @Override
    public void onBackPressed() {
        update();
        transfer(MainActivity.class);
        super.onBackPressed();
    }
}