package com.geekbrains.weather.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.geekbrains.weather.ui.base.BaseActivity;

import java.util.List;

public class MyService extends Service implements SensorEventListener {
    IBinder iBinder;
    private SensorManager sensorManager;
    private List<Sensor> listSensors;
    private Sensor sensor;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                sensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY),
                sensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
                sensorManager.SENSOR_DELAY_NORMAL);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            writeData(sensorEvent.values, BaseActivity.SENSOR_VAL, BaseActivity.BROADCAST_ACTION);
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            writeData(sensorEvent.values, BaseActivity.SENSOR_VAL2, BaseActivity.BROADCAST_ACTION_HUM);
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            writeData(sensorEvent.values, BaseActivity.SENSOR_VAL_TEMP, BaseActivity.BROADCAST_ACTION_TEMP);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void writeData(float[] values, String valueTag, String activityTag) {
        try {
            Intent intent = new Intent(activityTag);
            intent.putExtra(valueTag, values[0]);
            sendBroadcast(intent);
        } catch (Throwable t1) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t1.toString(), Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
