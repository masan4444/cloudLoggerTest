package com.example.masan.cloudloggertest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.Delayed;

import android.os.Handler;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private Button button;
    private TextView textView;
    private String url;
    private LinkedList<String> data;
    private LinkedList<String> data1;
    private LinkedList<String> data2;

    private CloudLogger cl = null;

    private InetAddress inetAddress;
    private long time1 = 0;
    private long time2;

    protected final static double RAD2DEG = 180/Math.PI;

    private SensorManager sensorManager;
    //http://android.keicode.com/basics/sensors-accelerometers-megnetic.php

    float[] rotationMatrix = new float[9];
    float[] gravity = new float[3];
    float[] geomagnetic = new float[3];
    float[] attitude = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.button);
        textView = (TextView)findViewById(R.id.textView);


        if (cl == null) {
            url = "http://quatronic.php.xdomain.jp/birdman/writer.php";
            cl = new CloudLogger(url);
        }

        data = new LinkedList<>();

        data1 = new LinkedList<>();
        data1.add("1.1");
        data1.add("1.2f");
        data1.add("1.3f");

        data2 = new LinkedList<>();
        data2.add("2.1f");
        data2.add("2.2f");
        data2.add("2.3f");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick","button was clicked");
                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Thread#run","send");
                        cl.send();
                    }
                })).start();
            }
        });

        initSensor();
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    protected void findViews(){
    }

    protected void initSensor(){
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent event) {

        switch(event.sensor.getType()){
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomagnetic = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                gravity = event.values.clone();
                break;
        }

        if(geomagnetic != null && gravity != null){

            SensorManager.getRotationMatrix(
                    rotationMatrix, null,
                    gravity, geomagnetic
            );
            SensorManager.getOrientation(
                    rotationMatrix,
                    attitude
            );

            time2 = System.currentTimeMillis();
            if (time2 - time1 >= 100) {
                data = new LinkedList<String>();
                data.add(String.valueOf(System.currentTimeMillis()));

                for (int i = 0; i <= 2; i++) {
                    data.add(String.valueOf(attitude[i] * RAD2DEG));
                    Log.d(String.valueOf(i), String.valueOf(attitude[i] * RAD2DEG));
                }

                cl.bufferedWrite(data);

                time1 = System.currentTimeMillis();
            }

        }

    }
}