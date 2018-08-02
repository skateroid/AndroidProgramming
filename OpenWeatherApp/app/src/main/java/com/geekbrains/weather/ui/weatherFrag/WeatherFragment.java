package com.geekbrains.weather.ui.weatherFrag;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geekbrains.weather.Constants;
import com.geekbrains.weather.prefs.PrefsData;
import com.geekbrains.weather.prefs.PrefsHelper;
import com.geekbrains.weather.R;
import com.geekbrains.weather.ui.base.BaseFragment;
import com.geekbrains.weather.ui.createCityFrag.CreateActionFragment;

import java.util.ArrayList;

public class WeatherFragment extends BaseFragment implements CreateActionFragment.OnHeadlineSelectedListener {

    private static final String ARG_COUNTRY = "ARG_COUNTRY";
    private String country;
    private TextView textView;
    private FloatingActionButton fab;
    private BottomAppBar bar;
    private TextView textView_temp;
    private PrefsHelper prefsHelper;
//    private TextView textView_humidity;
//    private SensorManager sensorManager;
//    private Sensor temp_sensor;
//    private Sensor humidity_sensor;

    public WeatherFragment() {
//        Особенностью поведения android-а состоит в том, что в любой момент
//        он может убить конкретный фрагмент (с случаи нехватки памяти например)
//        и потом попытаться восстановить его, используя конструктор без параметров,
//                следовательно передача параметров через конструкторы черевата
//        крэшами приложения в произвольный момент времени.
    }

    public static WeatherFragment newInstance(String country) {
//        Для того что бы положить требуемые значения во фрагмент,
//        нужно обернуть их в Bundle и передать через метод setArguments.
//        Стандартным способом передачи параметров считается создание статического
//        метода newInstance (...),
//        а для восстановление параметров используется метод getArguments(...),вызываемый в
//        методе жизненного цикла onCreate (...) .
        Bundle args = new Bundle();
        args.putString(ARG_COUNTRY, country);
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            country = getArguments().getString(ARG_COUNTRY);
        }
        prefsHelper = new PrefsData(getBaseActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_layout, container, false);
    }

    @Override
    protected void initLayout(View view, Bundle savedInstanceState) {
        textView = view.findViewById(R.id.tv_country);
//        textView_humidity = view.findViewById(R.id.tv_humidity);
        textView_temp = view.findViewById(R.id.bigTemp);
        textView_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), textView_temp);
                popupMenu.inflate(R.menu.popup_temp);
                popupMenu.show();
            }
        });
        //проверяем нашу переменную если она не пустая показываем город, если наоборот - ничего не показываем
        if (country != null && country.length() > 0) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(country);
        } else {
            textView.setVisibility(View.GONE);
        }

        String getSP = prefsHelper.getSharedPreferences(Constants.CITY);
        if (!getSP.equals("")) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(getSP);
        }

        ((TextView) getBaseActivity().findViewById(R.id.tv_humidity)).setText("30%");
        ((TextView) getBaseActivity().findViewById(R.id.tv_pressure)).setText("752mmHg");

//        sensorManager = (SensorManager) getBaseActivity().getSystemService(Context.SENSOR_SERVICE);
//
//        temp_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
//        sensorManager.registerListener(temp_listener, temp_sensor, SensorManager.SENSOR_DELAY_NORMAL);

//        humidity_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
//        sensorManager.registerListener(humidity_listener, humidity_sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public static WeatherFragment getWeatherFragment() {
        return new WeatherFragment();
    }

    @Override
    public void onResume() {
        bar = getBaseActivity().findViewById(R.id.bottomAppBar);
        bar.setVisibility(View.VISIBLE);
        fab = getBaseActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        super.onResume();
    }

    @Override
    public void onArticleSelected(ArrayList<String> citiesList) {
//        country = citiesList.toString();
//        if (country.equals("[Weather]")) {
//            textView.setVisibility(View.GONE);
//        } else {
//            textView.setVisibility(View.VISIBLE);
//            textView.setText(country.substring(country.indexOf("[") + 1, country.indexOf("]")));
//        }
    }
//    SensorEventListener temp_listener = new SensorEventListener() {
//        @Override
//        public void onSensorChanged(SensorEvent sensorEvent) {
//            textView_temp.setText(String.valueOf(sensorEvent.values[0]));
//        }
//
//        @Override
//        public void onAccuracyChanged(Sensor sensor, int i) {
//
//        }
//    };

//    SensorEventListener humidity_listener = new SensorEventListener() {
//        @Override
//        public void onSensorChanged(SensorEvent sensorEvent) {
//            StringBuilder humidity_string = new StringBuilder(String.valueOf(sensorEvent.values[0]));
//            textView_humidity.setText(humidity_string.append("%"));
//        }
//
//        @Override
//        public void onAccuracyChanged(Sensor sensor, int i) {
//
//        }
//    };
}
