package glebshanshin.trashworld;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlayActivity extends Activity implements OnTouchListener {
    long mills = 500L;
    Vibrator vibrator;
    float music, effects;
    MediaPlayer clickPlayer, playPlayer;
    int factory, robot, car, man;
    long TSH;
    int organicb, plasticb, metalb, glassb, notrecycleb, paperb;
    int organicc, plasticc, metalc, glassc, notrecyclec, paperc, mistakes, Adder, multi;
    DBHelper dbHelper;
    Cursor cursor;
    SQLiteDatabase db;
    TextView TSHv, TSHsv;
    int offset_x = 0;
    int offset_y = 0;
    boolean touchFlag = false;
    boolean dropFlag = false;
    boolean notIntent = true;
    LayoutParams imageParams;
    ImageView circle, trash;
    TextView plastic, glass, metal, organic, notrecycle, paper;
    String choice = "null", tag, tsh, postfix = " TSH";
    int eX, eY;
    int w, h;
    View root;

    public void increaseTSH(int Adder) {
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

    private String getPrice(long s) {
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

    public void toMenu(View view) {
        if (notIntent) {
            notIntent = false;
            Intent intent1 = new Intent(PlayActivity.this, MainActivity.class);
            startActivity(intent1);
            update(db);
            finish1();
        }
    }

    private void finish1() {
        playPlayer.stop();
        clickPlayer.start();
        finish();
    }

    private void update(SQLiteDatabase db) {
        ContentValues newValues = new ContentValues();
        newValues.put("TSH", TSH);
        newValues.put("man", man);
        newValues.put("car", car);
        newValues.put("robot", robot);
        newValues.put("factory", factory);

        newValues.put("paper", paperc);
        newValues.put("plastic", plasticc);
        newValues.put("metal", metalc);
        newValues.put("organic", organicc);
        newValues.put("notrecycle", notrecyclec);
        newValues.put("glass", glassc);
        newValues.put("mistakes", mistakes);
        db.update("Data", newValues, "_id = 1", null);
    }

    private void incCounter(String choice) {
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

    public void toStore(View view) {
        if (notIntent) {
            notIntent = false;
            update(db);
            Intent intent = new Intent(PlayActivity.this, StoreActivity.class);
            startActivity(intent);
            finish1();
        }
    }

    public void game() {
        if (tag.equals(choice)) {
            increaseTSH(Adder);
            incCounter(choice);
        } else {
            vibrator.vibrate(mills);
            mistakes++;
        }
        choice = "null";
        newTrash();
    }

    private String getTag(String id) {
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

    private void check(TextView i) {
        int topY = i.getTop();
        int leftX = i.getLeft();
        int rightX = i.getRight();
        int bottomY = i.getBottom();
        if (eX > leftX && eX < rightX && eY > topY && eY < bottomY) {
            if (!choice.equals("" + i.getTag()))
                clickPlayer.start();
            circle.setImageDrawable(getDrawable(getResources().getIdentifier(i.getTag() + "", "drawable", getPackageName())));
            dropFlag = true;
            choice = "" + i.getTag();
        }
    }

    public void newTrash() {
        String id = "" + ((int) (Math.random() * 32) + 1);
        tag = getTag(id);
        trash.setImageDrawable(getDrawable(getResources().getIdentifier("trash" + id, "drawable", getPackageName())));
    }

    public void init(SQLiteDatabase db) {
        cursor = db.query("Data", null, null, null, null, null, null);
        cursor.moveToFirst();
        TSH = cursor.getLong(1);
        man = cursor.getInt(2);
        car = cursor.getInt(3);
        robot = cursor.getInt(4);
        factory = cursor.getInt(5);
        paperc = cursor.getInt(6);
        plasticc = cursor.getInt(7);
        metalc = cursor.getInt(8);
        organicc = cursor.getInt(9);
        notrecyclec = cursor.getInt(10);
        glassc = cursor.getInt(11);
        mistakes = cursor.getInt(12);
        paperb = cursor.getInt(13);
        plasticb = cursor.getInt(14);
        metalb = cursor.getInt(15);
        organicb = cursor.getInt(16);
        notrecycleb = cursor.getInt(17);
        glassb = cursor.getInt(18);
        multi = cursor.getInt(19);
        music = cursor.getFloat(22);
        effects = cursor.getFloat(23);
        cursor.close();
        initTSHs();
    }

    private void initTSHs() {
        Adder = 1 + man + car * 10 + robot * 50 + factory * 100;
        Adder *= multi;
        String newa = getPrice(Adder);
        TSHsv.setText(newa + " TSH/мусор");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.play_main);
        circle = findViewById(R.id.circle);
        root = findViewById(android.R.id.content).getRootView();
        plastic = findViewById(R.id.plastic);
        glass = findViewById(R.id.glass);
        metal = findViewById(R.id.metal);
        organic = findViewById(R.id.organic);
        notrecycle = findViewById(R.id.notrecycle);
        paper = findViewById(R.id.paper);
        trash = findViewById(R.id.img);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        TSHv = findViewById(R.id.TSH);
        TSHsv = findViewById(R.id.TSHs);
        init(db);
        increaseTSH(0);
        newTrash();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        clickPlayer = MediaPlayer.create(this, R.raw.click);
        clickPlayer.setVolume(effects, effects);
        w = getWindowManager().getDefaultDisplay().getWidth() - 50;
        h = getWindowManager().getDefaultDisplay().getHeight() - 10;
        trash.setOnTouchListener(this);
        root.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (touchFlag) {
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_MOVE:
                            eX = (int) event.getX();
                            eY = (int) event.getY();
                            int x = (int) event.getX() - offset_x;
                            int y = (int) event.getY() - offset_y;
                            if (x > w) x = w;
                            if (y > h) y = h;
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(trash.getWidth(), trash.getHeight()));
                            lp.setMargins(x, y, 0, 0);
                            check(plastic);
                            check(metal);
                            check(glass);
                            check(organic);
                            check(notrecycle);
                            check(paper);
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

    @Override
    protected void onStop() {
        super.onStop();
        playPlayer.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        playPlayer = MediaPlayer.create(this, R.raw.play);
        playPlayer.setVolume(music, music);
        playPlayer.setLooping(true);
        playPlayer.start();
    }
}