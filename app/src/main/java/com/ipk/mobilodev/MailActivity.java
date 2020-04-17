package com.ipk.mobilodev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ipk.mobilodev.R;

public class MailActivity extends AppCompatActivity {
    EditText destinationMail, header, message;
    TextView atc_txt;
    ImageButton attachment;
    Button send;

    Uri URI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        destinationMail = findViewById(R.id.mail_destMail);
        header= findViewById(R.id.mail_header);
        message = findViewById(R.id.mail_message);
        attachment = findViewById(R.id.mail_atc);
        send =findViewById(R.id.mail_send);
        atc_txt=findViewById(R.id.mail_atc_txt);


        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(Intent.createChooser(intent, "ChooseFile"), 100);
                }catch (Exception e){
                    System.out.println(""+e);
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("message/rfc822");   //important
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{destinationMail.getText().toString()});
                    intent.putExtra(Intent.EXTRA_SUBJECT, header.getText().toString());
                    intent.putExtra(Intent.EXTRA_TEXT, message.getText().toString());
                    if (URI != null) {
                        intent.putExtra(Intent.EXTRA_STREAM, URI);
                    }
                    startActivity(Intent.createChooser(intent, "Uygulama Seçiniz")); //intent action sende gider
                }catch (Exception e){
                    System.out.println(""+e);

                }

            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            URI = data.getData();
            atc_txt.setText(URI.getLastPathSegment());
        }
    }


}
