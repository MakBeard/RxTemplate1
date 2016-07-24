package com.jollydroid.rxtemplate1.db.tables;

/**
 * В классе описаны константы для работы SQL
 *
 * @author D.Makarov
 * Created 24.07.2016.
 */
public class DataTable {

    public static final String TABLE_DATA = "table_data";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_VALUE = "data";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    //Так как это статический вспомогательный класс, запрещаем создание его экземпляров
    private DataTable() {
        throw new IllegalStateException("No instances please");
    }

    public static String getCreateTableQuery() {
        return  "CREATE TABLE " + TABLE_DATA + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_VALUE + " TEXT, "
                + COLUMN_TIMESTAMP + " LONG "
                + ");";
    }
}
