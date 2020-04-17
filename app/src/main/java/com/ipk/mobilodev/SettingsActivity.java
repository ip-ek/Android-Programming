package com.ipk.mobilodev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ipk.mobilodev.R;

public class SettingsActivity extends AppCompatActivity {
    TextView userName;
    EditText age, height, weight;
    Switch mode;
    Button register;
    RadioGroup radioGroup;
    RadioButton male, female;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        userName=findViewById(R.id.settings_username);
        age=findViewById(R.id.settings_age);
        height=findViewById(R.id.settings_height);
        weight=findViewById(R.id.settings_weight);
        mode=findViewById(R.id.settings_mode);
        register=findViewById(R.id.settings_register);
        radioGroup=findViewById(R.id.settings_gender);
        male=findViewById(R.id.settings_male);
        female=findViewById(R.id.settings_female);


        sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        userName.setText("admin");
        age.setText(sharedpreferences.getString("age", ""));
        height.setText(sharedpreferences.getString("height", ""));
        weight.setText(sharedpreferences.getString("weight", ""));
        male.setChecked(sharedpreferences.getBoolean("Male", false));
        female.setChecked(sharedpreferences.getBoolean("Female", false));
        mode.setChecked(sharedpreferences.getBoolean("mode", false));


        mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mode.isChecked()){
                    mode.setText("Light Mode     ");
                }else{
                    mode.setText("Dark Mode     ");
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("age", age.getText().toString());
                editor.putString("height", height.getText().toString());
                editor.putString("weight", weight.getText().toString());
                //editor.putBoolean("mode", mode.getShowText());
                editor.putBoolean("Female", female.isChecked());
                editor.putBoolean("Male", male.isChecked());
                editor.putBoolean("mode", mode.isChecked());
                editor.apply();


                editor.commit();
                Toast.makeText(getApplicationContext(),"Kaydedildi",Toast.LENGTH_LONG).show();

            }
        });


    }
}
