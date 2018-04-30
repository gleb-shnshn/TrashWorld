package glebshanshin.trashclicker;

import android.database.sqlite.SQLiteDatabase;

public class test {
    public static void main(String[] args) {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = new SQLiteDatabase("as");
        dbHelper.getData();
    }
}
