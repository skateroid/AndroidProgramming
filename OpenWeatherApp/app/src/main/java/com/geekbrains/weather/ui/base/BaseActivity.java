package com.geekbrains.weather.ui.base;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.geekbrains.weather.Constants;
import com.geekbrains.weather.OpenWeatherApp.OpenWeather;
import com.geekbrains.weather.model.WeatherRequest;
import com.geekbrains.weather.prefs.PrefsData;
import com.geekbrains.weather.prefs.PrefsHelper;
import com.geekbrains.weather.R;
import com.geekbrains.weather.service.MyService;
import com.geekbrains.weather.ui.createCityFrag.CreateActionFragment;
import com.geekbrains.weather.ui.historyFrag.HistoryFragment;
import com.geekbrains.weather.ui.weatherFrag.WeatherFragment;
import com.geekbrains.weather.ui.webFrag.WebFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseActivity extends AppCompatActivity
        implements BaseView.View, BaseFragment.Callback, NavigationView.OnNavigationItemSelectedListener,
        CreateActionFragment.OnHeadlineSelectedListener, CreateActionFragment.OnCountrySelectedListener{

    public final static String BROADCAST_ACTION = "BROADCAST_ACTION";
    public final static String BROADCAST_ACTION_HUM = "BROADCAST_ACTION_HUM";
    public final static String BROADCAST_ACTION_TEMP = "BROADCAST_ACTION_TEMP";
    public final static String  SENSOR_VAL = "SENSOR_VAL";
    public final static String  SENSOR_VAL2 = "SENSOR_VAL2";
    public final static String  SENSOR_VAL_TEMP = "SENSOR_VAL_TEMP";
    private final static String API_KEY = "8b3ed8818d3b9877948d4467f72a0b27";
    private final static String TAG = "TAG";

    //инициализация переменных
    private FloatingActionButton fab;
    private TextView textView;
    private TextView tv_city;
    private static final String TEXT = "TEXT";
    private static String contry;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private TextView tv_hum;
    private TextView tv_temp;
    private Button button;
    private PrefsHelper prefsHelper;
    private BroadcastReceiver broadcastReceiverAcc;
    private BroadcastReceiver broadcastReceiverHum;
    private BroadcastReceiver broadcastReceiverTemp;

    private OpenWeather openWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            TextView tv = findViewById(R.id.tvUsername);
            contry = savedInstanceState.getString("NAME");
        }
        setContentView(R.layout.activity_base);

        initRetrofit();
        initLayout();

        checkLocationPermission();
        Intent intent = new Intent(BaseActivity.this, MyService.class);
        startService(intent);

        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        broadcastReceiverAcc = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String value = String.valueOf(intent.getFloatExtra(SENSOR_VAL, 0));
                Log.d(SENSOR_VAL, value);
            }
        };
        IntentFilter intentFilterHum = new IntentFilter(BROADCAST_ACTION_HUM);
        broadcastReceiverHum = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String value = String.valueOf(intent.getFloatExtra(SENSOR_VAL2, 0));
                StringBuilder humidity_string = new StringBuilder(String.valueOf(value));
                if (getAnotherFragment() instanceof WeatherFragment) {
                    tv_hum = findViewById(R.id.tv_humidity);
                    tv_hum.setText(humidity_string.append("%"));
                }
                Log.d(SENSOR_VAL2, value);
            }
        };
        IntentFilter intentFilterTemp = new IntentFilter(BROADCAST_ACTION_TEMP);
        broadcastReceiverTemp = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String value = String.valueOf(intent.getFloatExtra(SENSOR_VAL_TEMP, 0));
                if (getAnotherFragment() instanceof WeatherFragment) {
                    tv_temp = findViewById(R.id.bigTemp);
                    tv_temp.setText(String.valueOf(value));
                }
                Log.d(SENSOR_VAL_TEMP, value);
            }
        };
        registerReceiver(broadcastReceiverAcc, intentFilter);
        registerReceiver(broadcastReceiverHum, intentFilterHum);
//        registerReceiver(broadcastReceiverTemp, intentFilterTemp);
    }

    private void initEvents() {

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
    //                savePreferences();    // сохранить настройки
//                    String getSP = prefsHelper.getSharedPreferences(Constants.CITY);
//                    if (!getSP.equals("")) {
                        requestRetrofit("Moscow", API_KEY);
//                    }
                }
            });

    }

    private void initRetrofit() {
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        openWeather = retrofit.create(OpenWeather.class);
    }

    private void initLayout() {
        //устанавливает тулбар
        prefsHelper = new PrefsData(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //устанавливаем drawer (выездное меню)
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //анимация клавищи (три палочки сверху) выездного меня
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //инициализация навигации
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //запускаем CreateActionFragment
                addFragment(new CreateActionFragment());
            }
        });
        tv_city = findViewById(R.id.tv_country);
        button = findViewById(R.id.btn_refresh);

        //addFragment(new WeatherFragment());
        startWeatherFragment(contry);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            addFragment1(new CreateActionFragment());
        }

        String getSP = prefsHelper.getSharedPreferences(Constants.CITY);
        if (!getSP.equals("")) {
//            CollapsingToolbarLayout toolbarLayout = findViewById(R.id.main_collapsing);
//            toolbarLayout.setTitle(getSP);
            getSupportActionBar().setTitle(getSP);
        }
        initEvents();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("NAME", ((TextView) findViewById(R.id.tvUsername)).getText().toString());
        super.onSaveInstanceState(outState);
    }

    private void addFragment(Fragment fragment) {
        //вызываем SupportFragmentManager и указываем в каком контейнере будет находиться наш фрагмент
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack("")
                .commit();
    }


    private void addFragment1(Fragment fragment) {
        //вызываем SupportFragmentManager и указываем в каком контейнере будет находиться наш фрагмент
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame2, fragment)
                .addToBackStack("")
                .commit();
    }

    private void getCurrentFragment() {
        //получаем наименование фрагмента находящегося в контейнере в данных момент
        getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }

    @Override
    public void onBackPressed() {
        //закрываем drawer если он был открыт при нажатии на аппаратную клавишу назад
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //работаем с навигацией
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            // Handle the camera action
        } else if (id == R.id.nav_info) {
            // Handle the camera action
        } else if (id == R.id.nav_history) {
            addFragment(new HistoryFragment());
        } else if (id == R.id.nav_web) {
            addFragment(new WebFragment());
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Boolean inNetworkAvailable() {
        return true;
    }

    @Override
    public void initDrawer(String username, Bitmap profileImage) {
    }

    @Override
    public void onFragmentAttached() {
    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    public void startWeatherFragment(String country) {
        //запускаем WeatherFragment и передаем туда country
        addFragment(WeatherFragment.newInstance(country));
        //cntry = country;


    }

    public Fragment getAnotherFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(BaseActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onArticleSelected(ArrayList<String> position) {
//        String country = position.toString().substring(contry.indexOf("[" + 1), contry
//        .indexOf("]"));
//        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.main_collapsing);
//        toolbarLayout.setTitle(position.get(0));
    }

    @Override
    public void onCountrySelected(String country) {
//        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.main_collapsing);
//        toolbarLayout.setTitle(country);
    }

    private void requestRetrofit(String city, String keyApi){
        openWeather.loadWeather(city, keyApi)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        if (response.body() != null)
                            textView.setText(Double.toString(response.body().getMain().getTemp()));
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        Log.d(TAG,"Error");
                    }
                });

    }
}
