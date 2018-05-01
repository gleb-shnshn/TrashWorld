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
import android.widget.Toast;

public class StoreActivity extends Activity{
    int factory,robot,car,man,TSH;
    SQLiteDatabase db;
    DBHelper dbHelper;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dbHelper= new DBHelper(this);
        db=dbHelper.getWritableDatabase();
        init(db);
        setContentView(R.layout.store_main);
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
    }
    public void buyMan(View view) {
        if (man+1<=TSH){
            man++;
            TSH-=(man+1);
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
            car++;
            TSH-=((car+1)*10);
        }
        else{
            toast();
        }
    }
    public void buyRobot(View view) {
        if ((robot+1)*50<=TSH){
            robot++;
            TSH-=((robot+1)*50);
        }
        else{
            toast();
        }
    }
    public void buyFactory(View view) {
        if ((factory+1)*100<=TSH){
            factory++;
            TSH-=((factory+1)*100);
        }
        else{
            toast();
        }
    }
    private void update(SQLiteDatabase db,int TSH,int man,int car,int robot,int factory) {
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
    }
}
