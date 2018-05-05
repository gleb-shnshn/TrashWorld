package glebshanshin.trashworld;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import android.widget.Toast;

public class PlayActivity extends Activity implements OnTouchListener {
    int factory,robot,car,man,TSH;
    int organicc,plasticc,metalc,glassc,notrecyclec,paperc,mistakes,Adder=1;
    DBHelper dbHelper;
    Cursor cursor;
    SQLiteDatabase db;
    TextView TSHv,TSHsv;
    private int offset_x = 0;
    private int offset_y = 0;
    Boolean touchFlag = false;
    boolean dropFlag = false;
    LayoutParams imageParams;
    ImageView plastic,glass,metal,organic,notrecycle,paper,trash,now;
    String choice,tag,tsh;
    int eX, eY;

    public void increaseTSH(){
        String newa=""+(Integer.parseInt(tsh.replaceAll(" ",""))+Adder);
        String prefix=new String(new char[10-newa.length()]).replace("\0", " ");
        tsh=prefix+newa;
        TSH+=Adder;
        TSHv.setText(tsh);

    }
    public void toMenu(View view) {
        Intent intent1 = new Intent(PlayActivity.this, MainActivity.class);
        startActivity(intent1);
        update(db);
        finish();
    }
    private void update(SQLiteDatabase db) {
        ContentValues newValues = new ContentValues();
        newValues.put("TSH",TSH);
        newValues.put("man",man);
        newValues.put("car",car);
        newValues.put("robot",robot);
        newValues.put("factory",factory);

        newValues.put("paper",paperc);
        newValues.put("plastic",plasticc);
        newValues.put("metal",metalc);
        newValues.put("organic",organicc);
        newValues.put("notrecycle",notrecyclec);
        newValues.put("glass", glassc);
        newValues.put("mistakes",mistakes);
        db.update("Data", newValues,"_id = 1",null);
    }
    private void incCounter(String choice) {
        switch (choice){
            case "paper":
                paperc++;
                break;
            case "plastic":
                plasticc++;
                break;
            case "metal":
                metalc++;
                break;
            case "notrecycle":
                notrecyclec++;
                break;
            case "organic":
                organicc++;
                break;
            case "glass":
                glassc++;
                break;
        }
    }

    public void toStore(View view) {
        update(db);
        Intent intent = new Intent(PlayActivity.this, StoreActivity.class);
        startActivity(intent);
        finish();
    }
    public void game() {
        if (tag.equals(choice)) {
            //Toast.makeText(this, "Правильно", Toast.LENGTH_SHORT).show();
            increaseTSH();
            incCounter(choice);
        }
        else {
            Toast.makeText(this, "неПравильно", Toast.LENGTH_SHORT).show();
            mistakes++;
        }
        choice="null";
        newTrash();
    }
    private String getTag(String id) {
        int id1=Integer.parseInt(id);
        if ((id1>=1)&(id1<=3)){
            return "glass";
        }
        else if ((id1>=4)&(id1<=5)){
            return "metal";
        }
        else if ((id1>=6)&(id1<=8)){
            return "notrecycle";
        }
        else if ((id1>=9)&(id1<=9)){
            return "organic";
        }
        else if ((id1>=10)&(id1<=13)){
            return "paper";
        }
        else if ((id1>=14)&(id1<=16)){
            return "plastic";
        }
        else{
            return "none";
        }
    }
    private void check(ImageView i){
        int topY = i.getTop();
        int leftX = i.getLeft();
        int rightX = i.getRight();
        int bottomY = i.getBottom();
        if (eX > leftX && eX < rightX && eY > topY && eY < bottomY) {
            i.setBackground(getDrawable(getResources().getIdentifier(i.getTag()+"w" , "drawable", getPackageName())));
            dropFlag = true;
            choice=""+i.getTag();
            now=findViewById(i.getId());
        }
        else {
            i.setBackground(getDrawable(getResources().getIdentifier(i.getTag()+"" , "drawable", getPackageName())));
        }
    }
    public void newTrash(){
        String id = ""+((int)(Math.random()*16)+1);
        tag=getTag(id);
        trash.setImageDrawable(getDrawable(getResources().getIdentifier("trash"+id , "drawable", getPackageName())));
    }
    public void init(SQLiteDatabase db){
        cursor = db.query("Data",null,null,null,null,null,null);
        cursor.moveToFirst();
        TSH=Integer.parseInt(cursor.getString(1));
        man=Integer.parseInt(cursor.getString(2));
        car=Integer.parseInt(cursor.getString(3));
        robot=Integer.parseInt(cursor.getString(4));
        factory=Integer.parseInt(cursor.getString(5));
        paperc=Integer.parseInt(cursor.getString(6));
        plasticc=Integer.parseInt(cursor.getString(7));
        metalc=Integer.parseInt(cursor.getString(8));
        organicc=Integer.parseInt(cursor.getString(9));
        notrecyclec=Integer.parseInt(cursor.getString(10));
        glassc=Integer.parseInt(cursor.getString(11));
        mistakes=Integer.parseInt(cursor.getString(12));
        cursor.close();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.play_main);
        View root = findViewById(android.R.id.content).getRootView();
        plastic=findViewById(R.id.plastic);
        glass=findViewById(R.id.glass);
        metal=findViewById(R.id.metal);
        organic=findViewById(R.id.organic);
        notrecycle=findViewById(R.id.notrecycle);
        paper=findViewById(R.id.paper);
        trash =  findViewById(R.id.img);
        dbHelper= new DBHelper(this);
        db=dbHelper.getWritableDatabase();
        init(db);
        TSHv=findViewById(R.id.TSH);
        TSHsv=findViewById(R.id.TSHs);
        String newa=""+TSH;
        String prefix=new String(new char[10-newa.length()]).replace("\0", " ");
        tsh=prefix+newa;
        TSHv.setText(tsh);
        newTrash();
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
                            int w = getWindowManager().getDefaultDisplay().getWidth() - 50;
                            int h = getWindowManager().getDefaultDisplay().getHeight() - 10;
                            if (x > w) x = w;
                            if (y > h) y = h;
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(trash.getWidth(),trash.getHeight()));
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
                                now.setBackground(getDrawable(getResources().getIdentifier(choice , "drawable", getPackageName())));
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
}