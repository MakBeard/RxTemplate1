package com.jollydroid.rxtemplate1;

/**
 * Created by User on 24.07.2016.
 */
public class DataTable {
    public static final String TABLE_DATA = "data";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATA_TEXT = "text";
    public static final String COLUMN_DATA_DATA = "data";


    public static String getCreateTableQuery() {
        return  "CREATE TABLE " + TABLE_DATA + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DATA_TEXT + " TEXT, "
                + COLUMN_DATA_DATA + " TEXT"
                + ");";
    }

}
