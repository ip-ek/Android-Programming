package com.ipk.mobilodev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {
    //Spinner liste ya da dizi ile çalışır. Bu yüzden adapter tanımlanır.
    EditText noteSelect,noteTxt;
    Button noteAdd, noteDelete, noteSave;
    Spinner noteSpin;

    ArrayList<String> arr= new ArrayList<>();
    //Arrayi doldur.
    ArrayAdapter arrayAdapter;

    //static int i=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        noteSelect=findViewById(R.id.note_select);
        noteAdd=findViewById(R.id.note_add);
        noteDelete=findViewById(R.id.note_delete);
        noteSave=findViewById(R.id.note_save);
        noteTxt=findViewById(R.id.note_txt);
        noteSpin=findViewById(R.id.note_spinner);


        arrayAdapter= new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, arr);
        noteSpin.setAdapter(arrayAdapter);

        createFile("admin.txt","");
        final String filename="admin";

        noteSelect.setCursorVisible(false);

        noteSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), ""+noteSpin.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        noteAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteSelect.setCursorVisible(true);
                createFile(noteSelect.getText().toString(), noteTxt.getText().toString());
            }
        });

        noteDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //silinecek
            }
        });

        noteSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFile(noteSpin.getSelectedItem().toString(), noteTxt.getText().toString());
            }
        });


    }

    //openFileOutput -- dosyanın yazmak için açılmasını sağlar
    //fileinputStream -- okuma için
    //context.modeprivate -- sadece bizim uygulamamız yazabilir.
    public void createFile(String filename, String context){
        try{
            FileOutputStream fos =  openFileOutput("filename", Context.MODE_PRIVATE);
            String mesaj="context";
            fos.write(mesaj.getBytes());
            fos.close();
        }catch (Exception e){
            System.out.println(""+e);
            e.printStackTrace();
        }
    }

    public void readFile(String filename){
        try{
            FileInputStream fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            //bufferedReader satır satır okumaya yarar
            BufferedReader read = new BufferedReader(isr);
            TextView textView = findViewById(R.id.note_txt);
            textView.setText(read.readLine());
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
