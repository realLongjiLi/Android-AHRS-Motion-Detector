package com.example.jkdaisuki;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.MessageFormat;
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

    boolean stop = false;

    Object DATA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DATA = Array.newInstance(double.class, 3, 3);

        // apply sensor manager
        acSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        grSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acSensor = acSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        grSensor = grSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        gySensor = gySensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    public void onClickBT2(View v) {
        acSensorManager.registerListener(acSensorEventListener, acSensor, SensorManager.SENSOR_DELAY_NORMAL);
        grSensorManager.registerListener(grSensorEventListener, grSensor, SensorManager.SENSOR_DELAY_NORMAL);
        gySensorManager.registerListener(gySensorEventListener, gySensor, SensorManager.SENSOR_DELAY_NORMAL);

        Watcher myWatcher = new Watcher();
        stop = false;
        myWatcher.execute(DATA);
    }

    private final SensorEventListener acSensorEventListener =new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            ((TextView) findViewById(R.id.txView)).setText(MessageFormat.format(
                    "[{0}, {1}, {2}]", event.values[0], event.values[1], event.values[2]));
            Object tmp = Array.get(DATA, 0);
            for (int i = 0; i < 3; i++) {
                Array.set(tmp, i, event.values[i]);
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
            ((TextView) findViewById(R.id.txView2)).setText(MessageFormat.format(
                    "[{0}, {1}, {2}]", event.values[0], event.values[1], event.values[2]));
            Object tmp = Array.get(DATA, 1);
            for (int i = 0; i < 3; i++) {
                Array.set(tmp, i, event.values[i]);
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
            ((TextView) findViewById(R.id.txView3)).setText(MessageFormat.format(
                    "[{0}, {1}, {2}]", event.values[0], event.values[1], event.values[2]));
            Object tmp = Array.get(DATA, 2);
            for (int i = 0; i < 3; i++) {
                Array.set(tmp, i, event.values[i]);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }
    };

    public void onClickStop(View v) {
        stop = true;
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
        Toast.makeText(this, "已重置", Toast.LENGTH_LONG).show();
    }

    public void onClickSave(View v) {
        // save linkedList
        StringBuilder stringBuilder = new StringBuilder();
        for (double[] each : linkedList) {
            String str = each[1] + ", " + each[2] + ", " + each[3] + "\n";
            stringBuilder.append(str);
        }

        Log.d("DEBUT", stringBuilder.toString());
//        try {
//            File file = new File(Environment.getExternalStorageDirectory(), "data.csv");
//            FileOutputStream fos = new FileOutputStream(file);
//            byte[] bytes = stringBuilder.toString().getBytes();
//            fos.write(bytes);
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void setResult(String in) {
        ((TextView) findViewById(R.id.textViewComputed)).setText(in);
    }

    private class Watcher extends AsyncTask<Object, String, Void> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Object... arrayObjectList) {
            while (!stop) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Convert to normal double[][] array.
                double[][] realArray = new double[3][3];
                for (int i = 0; i < 3; i++) {
                    Object row = Array.get(arrayObjectList[0], i);
                    for (int j = 0; j < 3; j++) {
                        realArray[i][j] = (double) Array.get(row, j);
                    }
                }

                double[] result = AHRS.MadgwickAHRSupdate(realArray[0][0], realArray[0][1],
                        realArray[0][2], realArray[1][0], realArray[1][1], realArray[1][2],
                        realArray[2][0], realArray[2][1], realArray[2][2]);
                String strOutput = decimalFormat.format(result[0]) + ", "
                        + decimalFormat.format(result[1])
                        + ", " + decimalFormat.format(result[2]) + ", "
                        + decimalFormat.format(result[3]);
                publishProgress(strOutput);
                linkedList.add(result);
            }
        return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            setResult(values[0]);
        }

        @Override
        protected void onCancelled() {

        }
    }

}
