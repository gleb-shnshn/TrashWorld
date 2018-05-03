package glebshanshin.trashworld;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class TestActivity extends Activity {
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.test);
        tv= findViewById(R.id.tv);
        dbHelper= new DBHelper(this);
        db=dbHelper.getWritableDatabase();
        update(db,0,0,0,0,0);
        //insert(db);
        /*cursor = db.query("Data",null,null,null,null,null,null);
        cursor.moveToFirst();
        tv.setText(cursor.getCount()+"");
        int TSH=Integer.parseInt(cursor.getString(1));
        int man=Integer.parseInt(cursor.getString(2));
        int car=Integer.parseInt(cursor.getString(3));
        int robot=Integer.parseInt(cursor.getString(4));
        int factory=Integer.parseInt(cursor.getString(5));*/
    }

    private void insert(SQLiteDatabase db,int TSH,int man,int car,int robot,int factory) {
        ContentValues newValues = new ContentValues();
        newValues.put("TSH",TSH);
        newValues.put("man",man);
        newValues.put("car",car);
        newValues.put("robot",robot);
        newValues.put("factory",factory);
        long t=db.insert("Data", null, newValues);
        if (t==-1){
            Log.i("FUCK", "bad insert");
        }
        else{
            Log.i("FUCK", "good insert"+t);
        }
    }
    private void update(SQLiteDatabase db,int TSH,int man,int car,int robot,int factory) {
        ContentValues newValues = new ContentValues();
        newValues.put("TSH",TSH);
        newValues.put("man",man);
        newValues.put("car",car);
        newValues.put("robot",robot);
        newValues.put("factory",factory);
        long t=db.update("Data", newValues,"_id = 1",null);
        if (t==-1){
            Log.i("FUCK", "bad update");
        }
        else{
            Log.i("FUCK", "good update"+t);
        }
    }

}
