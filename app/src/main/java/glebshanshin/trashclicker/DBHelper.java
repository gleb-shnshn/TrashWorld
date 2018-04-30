package glebshanshin.trashclicker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "itschool.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "MyData";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
            "                    TSH INTEGER NOT NULL DEFAULT 0, \n" +
            "                    TSHs INTEGER NOT NULL DEFAULT 0, \n" +
            "                    man INTEGER NOT NULL DEFAULT 0, \n" +
            "                    car INTEGER NOT NULL DEFAULT 0, \n" +
            "                    robot INTEGER NOT NULL DEFAULT 0, \n" +
            "                    factory INTEGER NOT NULL DEFAULT 0)";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = CREATE_TABLE;
        db.execSQL(query);
        getData(db);
    }

    private void getData(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            System.out.println(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}