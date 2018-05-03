package glebshanshin.trashworld;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StatisticsActivity extends Activity {
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    TextView organic,plastic,metal,glass,notrecycle,paper,mistake,trash,TSH;
    int organicc,plasticc,metalc,glassc,notrecyclec,paperc,mistakes,trashc,TSHc;
    String pref="\n:";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_main);
        dbHelper= new DBHelper(this);
        db=dbHelper.getWritableDatabase();
        init(db);
        init1();
        fillall();
    }

    private void fillall() {
        glass.setText(pref+glassc);
        organic.setText(pref+organicc);
        paper.setText(pref+paperc);
        trash.setText(pref+trashc);
        notrecycle.setText(pref+notrecyclec);
        mistake.setText(pref+mistakes);
        metal.setText(pref+metalc);
        plastic.setText(pref+plasticc);
        String newa=""+TSHc;
        String prefix=new String(new char[10-newa.length()]).replace("\0", " ");
        TSH.setText(prefix+newa);
    }

    private void init1() {
        glass=findViewById(R.id.glass);
        metal=findViewById(R.id.metal);
        notrecycle=findViewById(R.id.notrecycle);
        organic=findViewById(R.id.organic);
        paper=findViewById(R.id.paper);
        plastic=findViewById(R.id.plastic);
        trash=findViewById(R.id.trash);
        mistake=findViewById(R.id.mistake);
        TSH=findViewById(R.id.TSH);
    }

    public void init(SQLiteDatabase db){
        cursor = db.query("Data",null,null,null,null,null,null);
        cursor.moveToFirst();
        TSHc=Integer.parseInt(cursor.getString(1));
        paperc=Integer.parseInt(cursor.getString(6));
        plasticc=Integer.parseInt(cursor.getString(7));
        metalc=Integer.parseInt(cursor.getString(8));
        organicc=Integer.parseInt(cursor.getString(9));
        notrecyclec=Integer.parseInt(cursor.getString(10));
        glassc=Integer.parseInt(cursor.getString(11));
        mistakes=Integer.parseInt(cursor.getString(12));
        trashc=plasticc+paperc+metalc+organicc+notrecyclec+glassc;
        cursor.close();
    }

    public void toSettings(View view) {
        Intent intent = new Intent(StatisticsActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }
}
