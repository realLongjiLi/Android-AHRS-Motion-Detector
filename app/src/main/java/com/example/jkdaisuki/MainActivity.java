package com.example.jkdaisuki;

import android.Manifest;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    // acc sensor manager
    SensorManager acSensorManager;
    // gra sensor manager
    SensorManager grSensorManager;
    // gyr sensor manager
    SensorManager gySensorManager;

    // accelerometer
    Sensor acSensor;
    // gravity sensor
    Sensor grSensor;
    // gyroscope sensor
    Sensor gySensor;

    // text views
    TextView textViewGRAVITY;
    TextView textViewGYROSCOPE;
    TextView textViewACCELEROMETER;
    TextView textViewComputed;

    // button views
    Button buttonStart;
    Button buttonStop;
    Button buttonToLog;
    Button buttonReset;
//    Button buttonBatchCollection = findViewById(R.id.buttonCollectData);

    // booleans
    boolean lastStateStart;
    boolean lastStateStop;
    boolean lastStateToLog;
    boolean lastStateReset;

    DecimalFormat decimalFormat = new DecimalFormat("#.000000");

    LinkedList<double[]> linkedList = new LinkedList<>();

    double[][] DATA = new double[3][3];

    private boolean YB = true;

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

        textViewGRAVITY = findViewById(R.id.textViewGRAVITY);
        textViewGYROSCOPE = findViewById(R.id.textViewGYROSCOPE);
        textViewACCELEROMETER = findViewById(R.id.textViewACCELEROMETER);
        textViewComputed = findViewById(R.id.textViewComputed);

        buttonStart = findViewById(R.id.buttonStartRecording);
        buttonStop = findViewById(R.id.buttonStopRecording);
        buttonToLog = findViewById(R.id.buttonToLogcat);
        buttonReset = findViewById(R.id.buttonReset);

        buttonStart.setEnabled(true);
        buttonStop.setEnabled(false);
        buttonToLog.setEnabled(false);

        requestStorage();
    }

    // 校准
    // 引入数据

    public void onClickStartRecording(View v) {
        // register 3 sensor listeners
        acSensorManager.registerListener(acSensorEventListener, acSensor, SensorManager.SENSOR_DELAY_NORMAL);
        grSensorManager.registerListener(grSensorEventListener, grSensor, SensorManager.SENSOR_DELAY_NORMAL);
        gySensorManager.registerListener(gySensorEventListener, gySensor, SensorManager.SENSOR_DELAY_NORMAL);

        buttonStart.setEnabled(false);
        buttonStop.setEnabled(true);
    }

    private final SensorEventListener acSensorEventListener =new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            textViewACCELEROMETER.setText(MessageFormat.format(
                    "[{0}, {1}, {2}]", event.values[0], event.values[1], event.values[2]));
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
            textViewGRAVITY.setText(MessageFormat.format(
                    "[{0}, {1}, {2}]", event.values[0], event.values[1], event.values[2]));
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
            textViewGYROSCOPE.setText(MessageFormat.format(
                    "[{0}, {1}, {2}]", event.values[0], event.values[1], event.values[2]));
            for (int i = 0; i < 3; i++) {
                DATA[0][i] = event.values[i];
            }
            double[] tmp = AHRS.MadgwickAHRSupdate(DATA[0][0], DATA[0][1], DATA[0][2], DATA[1][0],
                    DATA[1][1], DATA[1][2], DATA[2][0], DATA[2][1], DATA[2][2]);
            String output = decimalFormat.format(tmp[0]) + ", " + decimalFormat.format(tmp[1])
                    + ", " + decimalFormat.format(tmp[2]) + ", "
                    + decimalFormat.format(tmp[3]);
            textViewComputed.setText(output);
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

        buttonStart.setEnabled(true);
        buttonStop.setEnabled(false);
        buttonToLog.setEnabled(true);
    }

    public void onClickReset(View v) {
        AHRS.resetq();
        linkedList = new LinkedList<>();
//        Toast.makeText(this, "reset", Toast.LENGTH_SHORT).show();

        buttonStart.setEnabled(true);
        buttonStop.setEnabled(false);
        buttonToLog.setEnabled(false);
    }

    public void onClickPrintToLog(View v) {
        // convert linkedList
        StringBuilder stringBuilder = new StringBuilder();
        for (double[] each : linkedList) {
            String str = each[1] + ", " + each[2] + ", " + each[3] + "\n";
            stringBuilder.append(str);
        }

        Log.d("DEBUT", stringBuilder.toString());

        /*
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "data.csv");
            FileOutputStream fos = new FileOutputStream(file);
            byte[] bytes = stringBuilder.toString().getBytes();
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

    public void onClickBatchCollectData(View v) {
        if (YB) {
            YB = false;
            lastStateStart = buttonStart.isEnabled();
            lastStateStop = buttonStop.isEnabled();
            lastStateToLog = buttonToLog.isEnabled();
            lastStateReset = buttonReset.isEnabled();
            onClickStartRecording(v);
            buttonStart.setEnabled(false);
            buttonStop.setEnabled(false);
            buttonToLog.setEnabled(false);
            buttonReset.setEnabled(false);
        } else {
            YB = true;
            buttonStart.setEnabled(lastStateStart);
            buttonStop.setEnabled(lastStateStop);
            buttonToLog.setEnabled(lastStateToLog);
            buttonReset.setEnabled(lastStateReset);
            // stop
            onClickStop(v);
            // save
            StringBuilder stringBuilder = new StringBuilder();
            for (double[] each : linkedList) {
                String str = each[1] + ", " + each[2] + ", " + each[3] + "\n";
                stringBuilder.append(str);
            }
            Calendar calendar = Calendar.getInstance();
            String time = "" + calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE)
                    + String.format("%02d", calendar.get(Calendar.SECOND));
            writeStringToCSV(stringBuilder.toString(), time);
            // reset
            onClickReset(v);
        }
    }

    public void writeStringToCSV(String toWrite, String fileName) {
        String filePath = Environment.getExternalStorageDirectory().toString()
                + File.separator + fileName + ".csv";
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            outputStream.write(toWrite.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestStorage() {
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }
}
