package glebshanshin.trashclicker;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends Activity {
    int factory,robot,car,man,TSH;
    int organicc,plasticc,metalc,glassc,notrecyclec,paperc,mistakes;
    DBHelper dbHelper;
    Cursor cursor;
    SQLiteDatabase db;
    private String choice,tag;
    ImageView trash;
    TextView TSHv,TSHsv;
    int Adder=1;
    String tsh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dbHelper= new DBHelper(this);
        db=dbHelper.getWritableDatabase();
        init(db);
        setContentView(R.layout.play_main);
        trash= findViewById(R.id.trash);
        TSHv=findViewById(R.id.TSH);
        TSHsv=findViewById(R.id.TSHs);
        String newa=""+TSH;
        String prefix=new String(new char[10-newa.length()]).replace("\0", " ");
        tsh=prefix+newa;
        TSHv.setText(tsh);
        newTrash();
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
    public void toMenu(View view) {
        Intent intent1 = new Intent(PlayActivity.this, MainActivity.class);
        startActivity(intent1);
        update(db);
        finish();
    }
    public void increaseTSH(){
        String newa=""+(Integer.parseInt(tsh.replaceAll(" ",""))+Adder);
        String prefix=new String(new char[10-newa.length()]).replace("\0", " ");
        tsh=prefix+newa;
        TSH+=Adder;
        TSHv.setText(tsh);

    }
    public void newTrash(){
        String id = ""+((int)(Math.random()*16)+1);
        tag=getTag(id);
        trash.setImageDrawable(getDrawable(getResources().getIdentifier("trash"+id , "drawable", getPackageName())));
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

    public void notrecycle(View view) {
        choice="notrecycle";
        game();
    }

    public void organic(View view) {
        choice="organic";
        game();
    }

    public void plastic(View view) {
        choice="plastic";
        game();
    }

    public void paper(View view) {
        choice="paper";
        game();
    }

    public void metal(View view) {
        choice="metal";
        game();
    }

    public void glass(View view) {
        choice="glass";
        game();
    }
    public void game() {
        if (tag==choice) {
            //Toast.makeText(this, "Правильно", Toast.LENGTH_SHORT).show();
            increaseTSH();
            incCounter(choice);
        }
        else
            Toast.makeText(this,"неПравильно",Toast.LENGTH_SHORT).show();
            mistakes++;
        choice="null";
        newTrash();
    }

    private void incCounter(String choice) {
        switch (choice){
            case "paper":
                paperc++;
            case "plastic":
                plasticc++;
            case "metal":
                metalc++;
            case "notrecycle":
                notrecyclec++;
            case "organic":
                organicc++;
            case "glass":
                glassc++;
        }
    }


    public void toStore(View view) {
        update(db);
        Intent intent = new Intent(PlayActivity.this, StoreActivity.class);
        startActivity(intent);
        finish();
    }
}
