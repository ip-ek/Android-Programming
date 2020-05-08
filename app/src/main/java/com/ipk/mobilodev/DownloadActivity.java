package com.ipk.mobilodev;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class DownloadActivity extends AppCompatActivity {

    Button btn;
    private ProgressBar progressBar;
    TextView txt;
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
    }


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
