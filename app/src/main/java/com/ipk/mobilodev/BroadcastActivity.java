package com.ipk.mobilodev;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class BroadcastActivity extends AppCompatActivity {
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        txt=findViewById(R.id.broadcast_txt);
        txt.setMovementMethod(new ScrollingMovementMethod());

        try{
            FileInputStream fis = openFileInput("receiver.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader read = new BufferedReader(isr);
            String tmp, context="";
            while((tmp=read.readLine())!=null){
                context=context+tmp+"\n";
            }
            txt.setText(context);
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
