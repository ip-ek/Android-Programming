package com.ipk.mobilodev;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

//alarm da burada
public class DownloadActivity extends AppCompatActivity {
    //alarm
    private Switch startAlarmBtn;
    private TimePickerDialog timePickerDialog;
    final static int REQUEST_CODE = 1;

    Button btn;
    private ProgressBar progressBar;
    TextView txt, saat;
    ImageView img;
    Integer count =1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        progressBar = (ProgressBar) findViewById(R.id.down_progressBar);
        progressBar.setMax(100);
        btn = (Button) findViewById(R.id.down_btn);
        btn.setText("Start");
        txt = (TextView) findViewById(R.id.down_output);
        img=findViewById(R.id.down_img);
        img.setVisibility(View.INVISIBLE);

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 1;
                System.out.println("\ncount:"+ count);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);

                new MyTask().execute(100);
            }
        });

        //alarm
        startAlarmBtn = findViewById(R.id.startAlarmBtn);
        startAlarmBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startAlarmBtn.isChecked()){
                    openPickerDialog(true);
                }else{
                    saat.setText("");
                    cancelAlarm();
                    Toast.makeText(getApplicationContext(), "Alarm İptal Edildi", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    //alarm
    private void openPickerDialog(boolean is24hour) {

        Calendar calendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(
                DownloadActivity.this,
                onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                is24hour);
        timePickerDialog.setTitle("Alarm Ayarla");
        timePickerDialog.show();
    }
    //alarm
    TimePickerDialog.OnTimeSetListener onTimeSetListener
            = new TimePickerDialog.OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            if(calSet.compareTo(calNow) <= 0){

                calSet.add(Calendar.DATE, 1);
            }

            saat=findViewById(R.id.alarm_clock);
            saat.setText(hourOfDay+":"+ minute);
            setAlarm(calSet);
        }};
    //alarm
    private void setAlarm(Calendar alarmCalender){


        Toast.makeText(getApplicationContext(),"Alarm Ayarlandı!",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), REQUEST_CODE, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalender.getTimeInMillis(), pendingIntent);

    }
    private void cancelAlarm(){
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    //download
    class MyTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            Random rand = new Random();
            for (; count <= params[0]; count+=rand.nextInt(10)) {
                try {
                    Log.d("takip", ""+count);
                    Thread.sleep(1000);
                    publishProgress(count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Task Completed.";
        }
        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            txt.setText(result);
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(getBaseContext(), alarmUri);
            ringtone.play();    //telefonun kendi sesi çalar titreşim yok

            Toast.makeText(getApplicationContext(),"İndirme Başarılı", Toast.LENGTH_SHORT).show();
            img.setVisibility(View.VISIBLE);
            btn.setText("Restart");
        }

        @Override
        protected void onPreExecute() {
            txt.setText("Task Starting...");
            img.setVisibility(View.INVISIBLE); //tekrar başlatılırsa gitsin diye :)
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            txt.setText("Running...  %"+ values[0]);
            progressBar.setProgress(values[0]);
        }
    }

}
