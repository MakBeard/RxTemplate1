package com.jollydroid.rxtemplate1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jollydroid.rxtemplate1.db.tables.DataTable;

/**
 * SQLiteHelper для работы со StorIO
 *
 * @author D.Makarov
 * Created 24.07.2016.
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "DATA";
    private static final int DB_VERSION = 1;

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataTable.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
