package glebshanshin.trashclicker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DATAforT.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Data";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
            "                    TSH TEXT NOT NULL DEFAULT 0, \n" +
            "                    man TEXT NOT NULL DEFAULT 0, \n" +
            "                    car TEXT NOT NULL DEFAULT 0, \n" +
            "                    robot TEXT NOT NULL DEFAULT 0, \n" +
            "                    factory TEXT NOT NULL DEFAULT 0, \n" +
            "                    paper TEXT NOT NULL DEFAULT 0, \n" +
            "                    plastic TEXT NOT NULL DEFAULT 0, \n" +
            "                    metal TEXT NOT NULL DEFAULT 0, \n" +
            "                    organic TEXT NOT NULL DEFAULT 0, \n" +
            "                    notrecycle TEXT NOT NULL DEFAULT 0, \n" +
            "                    glass TEXT NOT NULL DEFAULT 0, \n" +
            "                    mistakes TEXT NOT NULL DEFAULT 0)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = CREATE_TABLE;
        db.execSQL(query);
        insert(db);
    }

    public void insert(SQLiteDatabase db) {
        ContentValues newValues = new ContentValues();
        newValues.put("TSH",0);
        newValues.put("man",0);
        newValues.put("car",0);
        newValues.put("robot",0);
        newValues.put("factory",0);

        newValues.put("paper",0);
        newValues.put("plastic",0);
        newValues.put("metal",0);
        newValues.put("organic",0);
        newValues.put("notrecycle",0);
        newValues.put("glass", 0);
        newValues.put("mistakes",0);
        db.insert(TABLE_NAME,null,newValues);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}