package com.jollydroid.rxtemplate1.model;

import com.jollydroid.rxtemplate1.db.tables.DataTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Класс-модель используемых данных с аннотациями для StorIO
 *
 * @author D.Makarov
 * Created 24.07.2016.
 */
@StorIOSQLiteType(table = DataTable.TABLE_DATA)
public class DataModel {

    @StorIOSQLiteColumn(name = DataTable.COLUMN_VALUE)
    protected String value;

    @StorIOSQLiteColumn(name = DataTable.COLUMN_TIMESTAMP, key = true)
    protected long timestamp;

    //Обязательный пустой конструктор для StorIO
    public DataModel() {
    }

    public DataModel(String value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "value='" + value + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
