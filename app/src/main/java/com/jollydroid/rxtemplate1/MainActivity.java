package com.jollydroid.rxtemplate1;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

    private Toolbar mToolbar;
    private DrawerLayout mNavigationView;
    private CoordinatorLayout mCoordinatorLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar();

        mNavigationView = (DrawerLayout) findViewById(R.id.navigation_drawer);
        if (mNavigationView != null) {
            setupDrawer();
        }

        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

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

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * Метод возвращает коллекцию всех данных из БД
     *
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
     *
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
     *
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


    //устанавливает тулбар вместо аппбара
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        //по клику на сендвич открываем navigation drawer
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    //инициализируем drawer
    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        assert navigationView != null;

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.item2:
                        viewPager.setCurrentItem(1);
                        break;

                }
                mNavigationView.closeDrawers();
                return true;
            }
        });

    }

    //закрывает navigation drawer по кнопке назад
    @Override
    public void onBackPressed() {
        if (mNavigationView.isDrawerOpen(GravityCompat.START)) {
            mNavigationView.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //по клику на менюшке(сендвичу) открывается navigation drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationView.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ListItemFragment(), "List Item");
        adapter.addFrag(new AddItemFragment(), "Add Item");
        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
