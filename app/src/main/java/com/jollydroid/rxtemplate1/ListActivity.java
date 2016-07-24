package com.jollydroid.rxtemplate1;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;


public class ListActivity extends AppCompatActivity implements IListView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        SQLiteOpenHelper sqLiteOpenHelper = new DBOpenHelper(this);

        StorIOSQLite storIOSQLite= DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(DataModel.class,new)
                .build();
    }
}
