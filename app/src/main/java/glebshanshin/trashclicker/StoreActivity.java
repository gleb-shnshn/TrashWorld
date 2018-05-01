package glebshanshin.trashclicker;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class StoreActivity extends Activity{
    int factory,robot,car,man,TSH;
    SQLiteDatabase db;
    DBHelper dbHelper;
    Cursor cursor;
    TextView TSHv,priceman,pricecar,pricerobot,pricefactory,countman,countcar,countrobot,countfactory;
    String tsh;
    String t=" TSH";
    String u="У вас сейчас\n   ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.store_main);
        dbHelper= new DBHelper(this);
        db=dbHelper.getWritableDatabase();
        TSHv=findViewById(R.id.TSH);
        init(db);
        updateTSH();
        updatePRICE("all");
    }

    private void updatePRICE(String what) {
        switch (what){
            case "all":
                priceman.setText((man+1)+t);
                pricecar.setText((car+1)*10+t);
                pricerobot.setText((robot+1)*50+t);
                pricefactory.setText((factory+1)*100+t);
            case "man":
                priceman.setText((man+1)+t);
            case "car":
                pricecar.setText((car+1)*10+t);
            case "robot":
                pricerobot.setText((robot+1)*50+t);
            case "factory":
                pricefactory.setText((factory+1)*100+t);

        }
        switch (what){
            case "all":
                countman.setText(getstr(man));
                countcar.setText(getstr(car));
                countrobot.setText(getstr(robot));
                countfactory.setText(getstr(factory));
            case "man":
                countman.setText(getstr(man));
            case "car":
                countcar.setText(getstr(car));
            case "robot":
                countrobot.setText(getstr(robot));
            case "factory":
                countfactory.setText(getstr(factory));

        }
    }

    private String getstr(int what) {
        String newa=""+what;
        int d=2-newa.length();
        if (d<0){
            d=0;
        }
        String prefix=new String(new char[d]).replace("\0", " ");
        return u+prefix+newa+"/100";
    }

    private void updateTSH() {
        String newa=""+TSH;
        String prefix=new String(new char[10-newa.length()]).replace("\0", " ");
        tsh=prefix+newa;
        TSHv.setText(tsh);
    }
    public void init(SQLiteDatabase db){
        cursor = db.query("Data",null,null,null,null,null,null);
        cursor.moveToFirst();
        TSH=Integer.parseInt(cursor.getString(1));
        man=Integer.parseInt(cursor.getString(2));
        car=Integer.parseInt(cursor.getString(3));
        robot=Integer.parseInt(cursor.getString(4));
        factory=Integer.parseInt(cursor.getString(5));
        cursor.close();
        priceman=findViewById(R.id.text2man);
        pricecar=findViewById(R.id.text2car);
        pricerobot=findViewById(R.id.text2robot);
        pricefactory=findViewById(R.id.text2factory);
        countman=findViewById(R.id.textman);
        countcar=findViewById(R.id.textcar);
        countrobot=findViewById(R.id.textrobot);
        countfactory=findViewById(R.id.textfactory);
    }
    public void buyMan(View view) {
        if (man+1<=TSH){
            TSH-=(man+1);
            man++;
            updateTSH();
            updatePRICE("man");
        }
        else{
            toast();
        }
    }

    private void toast() {
        Toast.makeText(getApplicationContext(),"Не достаточно TSH",Toast.LENGTH_SHORT).show();
    }

    public void buyCar(View view) {
        if ((car+1)*10<=TSH){
            TSH-=((car+1)*10);
            car++;
            updateTSH();
            updatePRICE("car");
        }
        else{
            toast();
        }
    }



    public void buyRobot(View view) {
        if ((robot+1)*50<=TSH){
            TSH-=((robot+1)*50);
            robot++;
            updateTSH();
            updatePRICE("robot");
        }
        else{
            toast();
        }
    }
    public void buyFactory(View view) {
        if ((factory+1)*100<=TSH){
            TSH-=((factory+1)*100);
            factory++;
            updateTSH();
            updatePRICE("factory");
        }
        else{
            toast();
        }
    }
    private void update(SQLiteDatabase db) {
        ContentValues newValues = new ContentValues();
        newValues.put("TSH",TSH);
        newValues.put("man",man);
        newValues.put("car",car);
        newValues.put("robot",robot);
        newValues.put("factory",factory);
        db.update("Data", newValues,"_id = 1",null);
    }


    public void toPlay(View view) {
        Intent intent = new Intent(StoreActivity.this, PlayActivity.class);
        startActivity(intent);
        update(db);
    }
}
