package com.ipk.mobilodev;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static java.lang.Math.abs;


public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorActivity classPointer=this;

    long startTime = 0;


    TextView text, acc, light;
    private SensorManager mSensorManager;
    private float last_x, last_y, last_z = 0;
    LinearLayout backend;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            if(millis>5000){
                Toast.makeText(getApplicationContext(),"5 saniye hareketsiz kalındı. Sistem sonu.", Toast.LENGTH_LONG).show();
                finishAffinity();
                System.exit(0);
            }else{
                timerHandler.postDelayed(this, 500);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        text=findViewById(R.id.sensor_text);
        text.setMovementMethod(new ScrollingMovementMethod());
        acc=findViewById(R.id.sensor_acc);
        light=findViewById(R.id.sensor_light);
        //text.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        backend=findViewById(R.id.sensor_background);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> mList= mSensorManager.getSensorList(Sensor.TYPE_ALL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);

        for (int i = 1; i < mList.size(); i++) {
            text.append(""+i+ "-"+ mList.get(i).getName()+"\n");
        }

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            if(abs(z-last_z)>0.3){
                timerHandler.removeCallbacks(timerRunnable);
                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);
            }

            acc.setText(String.format("İvme: x : %f y : %f z : %f", x, y, z));
            last_x = x;
            last_y = y;
            last_z = z;
        }
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            light.setText("Işık Sensör Seviyesi:" + String.valueOf(event.values[0]));
            if(event.values[0]<10){
                //white screen - black text
                backend.setBackgroundColor(getColor(R.color.colorWhite));
                acc.setTextColor(getColor(R.color.colorBlack));
                text.setTextColor(getColor(R.color.colorBlack));
                light.setTextColor(getColor(R.color.colorBlack));
            }else{
                //black screen - white text
                backend.setBackgroundColor(getColor(R.color.colorBlack));
                acc.setTextColor(getColor(R.color.colorWhite));
                text.setTextColor(getColor(R.color.colorWhite));
                light.setTextColor(getColor(R.color.colorWhite));
            }
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }
}
