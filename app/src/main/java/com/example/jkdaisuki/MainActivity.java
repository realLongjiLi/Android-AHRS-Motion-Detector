package com.example.jkdaisuki;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // acc sensor manager
    SensorManager acSensorManager;
    // gra sensor manager
    SensorManager grSensorManager;
    // gyr sensor manager
    SensorManager gySensorManager;

    // Accelerometer
    Sensor acSensor;
    // gravity
    Sensor grSensor;
    // gyroscope
    Sensor gySensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button normalizeButton = findViewById(R.id.normalize_button);
        normalizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        acSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        grSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acSensor = acSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        grSensor = grSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        gySensor = gySensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    // 校准
    // 引入数据

    public void onClickBT2(View v) {
        acSensorManager.registerListener(mSensorEventListener, acSensor, SensorManager.SENSOR_DELAY_NORMAL);
        grSensorManager.registerListener(grSensorEventListener, grSensor, SensorManager.SENSOR_DELAY_NORMAL);
        gySensorManager.registerListener(gySensorEventListener, gySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private final SensorEventListener mSensorEventListener=new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            ((TextView) findViewById(R.id.txView)).setText("[" + event.values[0] + ", "
                    + event.values[1] + ", " + event.values[2] + "]");
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }
    };

    private final SensorEventListener grSensorEventListener=new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            ((TextView) findViewById(R.id.txView2)).setText("[" + event.values[0] + ", "
                    + event.values[1] + ", " + event.values[2] + "]");
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }
    };

    private final SensorEventListener gySensorEventListener=new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            ((TextView) findViewById(R.id.txView3)).setText("[" + event.values[0] + ", "
                    + event.values[1] + ", " + event.values[2] + "]");
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }
    };
}
