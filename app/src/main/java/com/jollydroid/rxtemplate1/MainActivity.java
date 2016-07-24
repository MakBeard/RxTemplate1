package com.jollydroid.rxtemplate1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements IMainView {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.textdate)
    TextView textdate;
    @BindView(R.id.inputtext)
    EditText inputtext;
    @BindView(R.id.inputdate)
    EditText inputdate;
    @BindView(R.id.savebutton)
    Button savebutton;
    @BindView(R.id.listbutton)
    Button listbutton;
    private IInputPresenter presenter;


    private static final String TAG = MainActivity.class.getSimpleName();
    private DefaultStorIOSQLite mDefaultStorIOSQLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new InputPresenter(this);

    }

    @OnClick(R.id.savebutton)
    public void onClickSave(View view) {
        presenter.saveData();
        Log.d("something","Нажата кнопка сохранить");
    }

    @OnClick(R.id.listbutton)
    public void onClickList(View view) {
        startActivity(new Intent(MainActivity.this,ListActivity.class));
    }

    @Override
    public String getInputText() {
        return inputtext.getText().toString();
    }

    @Override
    public String getInputData() {
        return inputdate.getText().toString();

        SQLiteOpenHelper sqLiteOpenHelper = new DbOpenHelper(this);

        //Получаем с помощью builder'а экземпляр StorIO
        mDefaultStorIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
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
