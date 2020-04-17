package com.ipk.mobilodev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ipk.mobilodev.R;

public class MainActivity extends AppCompatActivity {
//mesajlar hariç şuan tammam
    EditText userName, password;
    Button loginButton;
    private int girisHakkı;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName =findViewById(R.id.main_username);
        password=findViewById(R.id.main_password);
        loginButton = findViewById(R.id.main_login);

        girisHakkı=3;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                if(password.getText().toString().equals("0000") && userName.getText().toString().equals("admin")){
                    Toast.makeText(getApplicationContext(),"Giriş Başarılı",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    password.setText("");
                    userName.setText("");
                }
                else{
                    password.setText("");
                    userName.setText("");
                    girisHakkı--;
                    if(girisHakkı==0){
                        Toast.makeText(getApplicationContext(),"Sistem sonu.",Toast.LENGTH_LONG).show();
                        //alert verebiliriz
                        finishAffinity();
                    }else{
                        String s="Hatalı giriş.\nKalan hak: "+girisHakkı;
                        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }


}
