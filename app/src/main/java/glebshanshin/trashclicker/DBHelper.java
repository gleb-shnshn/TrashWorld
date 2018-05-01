package glebshanshin.trashclicker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DATAforTC.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Data";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
            "                    TSH TEXT NOT NULL DEFAULT 0, \n" +
            "                    man TEXT NOT NULL DEFAULT 0, \n" +
            "                    car TEXT NOT NULL DEFAULT 0, \n" +
            "                    robot TEXT NOT NULL DEFAULT 0, \n" +
            "                    factory TEXT NOT NULL DEFAULT 0)";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = CREATE_TABLE;
        db.execSQL(query);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}