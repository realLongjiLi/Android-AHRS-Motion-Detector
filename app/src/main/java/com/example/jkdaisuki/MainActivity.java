package com.example.jkdaisuki;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;

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

    double DATA[][] = new double[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // apply sensor manager
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
        acSensorManager.registerListener(acSensorEventListener, acSensor, SensorManager.SENSOR_DELAY_NORMAL);
        grSensorManager.registerListener(grSensorEventListener, grSensor, SensorManager.SENSOR_DELAY_NORMAL);
        gySensorManager.registerListener(gySensorEventListener, gySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private final SensorEventListener acSensorEventListener =new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            ((TextView) findViewById(R.id.txView)).setText("[" + event.values[0] + ", "
                    + event.values[1] + ", " + event.values[2] + "]");
            for (int i = 0; i < 3; i++) {
                DATA[0][i] = event.values[i];
            }
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
            for (int i = 0; i < 3; i++) {
                DATA[1][i] = event.values[i];
            }
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
            for (int i = 0; i < 3; i++) {
                DATA[2][i] = event.values[i];
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }
    };

    public void onClickStop(View v) {
        acSensorManager.unregisterListener(acSensorEventListener, acSensor);
        grSensorManager.unregisterListener(grSensorEventListener, grSensor);
        gySensorManager.unregisterListener(gySensorEventListener, gySensor);

        double[] tmp = AHRS.MadgwickAHRSupdate(DATA[0][0], DATA[0][1], DATA[0][2], DATA[1][0],
                DATA[1][1], DATA[1][2], DATA[2][0], DATA[2][1], DATA[2][2]);
        ((TextView) findViewById(R.id.textViewComputed)).setText(tmp[0] + ", " + tmp[1] + ", " + tmp[2] + ", " + tmp[3]);
    }

    public void onClickReset(View v) {
        AHRS.resetq();
        Toast.makeText(this, "reset", Toast.LENGTH_LONG).show();
    }
}
