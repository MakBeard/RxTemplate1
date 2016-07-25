package com.jollydroid.rxtemplate1;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jollydroid.rxtemplate1.db.DbOpenHelper;
import com.jollydroid.rxtemplate1.db.tables.DataTable;
import com.jollydroid.rxtemplate1.model.DataModel;
import com.jollydroid.rxtemplate1.model.DataModelSQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private DefaultStorIOSQLite mDefaultStorIOSQLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteOpenHelper sqLiteOpenHelper = new DbOpenHelper(this);

        //Получаем с помощью builder'а экземпляр StorIO
        mDefaultStorIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                //Класс XXXSQLiteTypeMapping генерируется автоматически, при наличии аннотаций
                .addTypeMapping(DataModel.class, new DataModelSQLiteTypeMapping())
                .build();


        for (DataModel dataModel : getMockData(5)) {
            Log.d(TAG, "MAKTAG put to DB: " + dataModel.toString());
            putDataToStorIO(dataModel);
        }

        List<DataModel> storedDataList = loadDataFromDb();
        for (DataModel dataModel : storedDataList) {
            Log.d(TAG, "MAKTAG load from DB: " + dataModel.toString());
        }
    }

    /**
     * Метод возвращает коллекцию всех данных из БД
     * @return List объектов DataModel
     */
    private List<DataModel> loadDataFromDb() {
        return mDefaultStorIOSQLite
                .get()
                .listOfObjects(DataModel.class)
                .withQuery(
                        Query.builder()
                                .table(DataTable.TABLE_DATA)
                                .build())
                .prepare()
                .executeAsBlocking();
    }

    /**
     * Метод сохраянет в БД заданный объект типа DataModel
     * @param dataModel сохраняемый объект
     */
    private void putDataToStorIO(DataModel dataModel) {
        mDefaultStorIOSQLite
                .put()
                .object(dataModel)
                .prepare()
                .executeAsBlocking();
    }

    /**
     * Метод для тестирования, генерирует список DataModel
     * @return список объектов DataModel
     */
    private List<DataModel> getMockData(int count) {

        List<DataModel> mockList = new ArrayList<>();

        for (int i = 0; i <= count; i++) {
            try {
                mockList.add(new DataModel("Строка " + i, System.currentTimeMillis()));

                //Ждём 1 мс, что бы не было одинакового времени
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return mockList;
    }
}
