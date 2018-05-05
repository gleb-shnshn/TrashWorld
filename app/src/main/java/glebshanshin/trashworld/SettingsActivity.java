package glebshanshin.trashworld;

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

public class SettingsActivity extends Activity {
    DBHelper dbHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.settings_main);
    }

    public void statistics(View view) {
        Intent intent = new Intent(SettingsActivity.this, StatisticsActivity.class);
        startActivity(intent);
        finish();
    }

    public void reset(View view) {
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        update(db);
    }

    private void update(SQLiteDatabase db) {
        ContentValues newValues = new ContentValues();
        newValues.put("TSH", 0);
        newValues.put("man", 0);
        newValues.put("car", 0);
        newValues.put("robot", 0);
        newValues.put("factory", 0);

        newValues.put("paper", 0);
        newValues.put("plastic", 0);
        newValues.put("metal", 0);
        newValues.put("organic", 0);
        newValues.put("notrecycle", 0);
        newValues.put("glass", 0);
        newValues.put("mistakes", 0);
        db.update("Data", newValues, "_id = 1", null);
        Toast.makeText(this, "Все данные стерты", Toast.LENGTH_SHORT).show();
    }

    public void toMenu(View view) {
        Intent intent1 = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent1);
        finish();
    }
}