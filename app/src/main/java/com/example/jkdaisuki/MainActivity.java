package com.example.jkdaisuki;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.LinkedList;

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

    DecimalFormat decimalFormat = new DecimalFormat("#.000000");

    LinkedList<double[]> linkedList = new LinkedList<>();

    double[][] DATA = new double[3][3];

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
                DATA[1][i] = event.values[i];
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
                DATA[2][i] = event.values[i];
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
                DATA[0][i] = event.values[i];
            }
            double[] tmp = AHRS.MadgwickAHRSupdate(DATA[0][0], DATA[0][1], DATA[0][2], DATA[1][0],
                    DATA[1][1], DATA[1][2], DATA[2][0], DATA[2][1], DATA[2][2]);
            String output = decimalFormat.format(tmp[0]) + ", " + decimalFormat.format(tmp[1])
                    + ", " + decimalFormat.format(tmp[2]) + ", "
                    + decimalFormat.format(tmp[3]);
            ((TextView) findViewById(R.id.textViewComputed)).setText(output);
            linkedList.add(tmp);
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
        /*
        double[] tmp = AHRS.MadgwickAHRSupdate(DATA[0][0], DATA[0][1], DATA[0][2], DATA[1][0],
                DATA[1][1], DATA[1][2], DATA[2][0], DATA[2][1], DATA[2][2]);
        ((TextView) findViewById(R.id.textViewComputed)).setText(tmp[0] + ", " + tmp[1] + ", " + tmp[2] + ", " + tmp[3]); */
    }

    public void onClickReset(View v) {
        AHRS.resetq();
        Toast.makeText(this, "reset", Toast.LENGTH_LONG).show();
    }

    public void onClickSave(View v) {
        // save linkedList
        StringBuilder stringBuilder = new StringBuilder();
        for (double[] each : linkedList) {
            String str = each[1] + ", " + each[2] + ", " + each[3] + "\n";
            stringBuilder.append(str);
        }

        try {
            Log.d("DEBUT", stringBuilder.toString());
            File file = new File(Environment.getExternalStorageDirectory(), "data.csv");
            FileOutputStream fos = new FileOutputStream(file);
            byte[] bytes = stringBuilder.toString().getBytes();
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
