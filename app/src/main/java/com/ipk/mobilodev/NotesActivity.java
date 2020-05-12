package com.ipk.mobilodev;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {
    //Spinner liste ya da dizi ile çalışır. Bu yüzden adapter tanımlanır.
    EditText noteName,noteTxt;
    Button noteAdd, noteDelete, noteSave;
    Spinner noteSpin;

    ArrayList<String> arr=new ArrayList<>();
    //Arrayi doldur.
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        noteName=findViewById(R.id.note_name);
        noteDelete=findViewById(R.id.note_delete);
        noteSave=findViewById(R.id.note_save);
        noteTxt=findViewById(R.id.note_txt);
        noteSpin=findViewById(R.id.note_spinner);

        try{
            arr.add("");
            FileInputStream fis = openFileInput("admin");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader read =new BufferedReader(isr);
            String tmp;
            while((tmp=read.readLine())!=null){
                arr.add(tmp);
            }
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        arrayAdapter= new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, arr);
        noteSpin.setAdapter(arrayAdapter);
        createFile("admin","");

        noteSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(noteSpin.getSelectedItem().toString().equals("")){
                    noteTxt.setText("");
                    noteName.setText("");
                    noteName.setEnabled(true);
                }else{
                    noteName.setEnabled(false);
                    readFile(noteSpin.getSelectedItem().toString());
                    //Toast.makeText(getApplicationContext(), ""+noteSpin.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        noteDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noteSpin.isSelected() || noteSpin.getSelectedItem().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Lütfen spinden kayıtlı bir dosya seçin", Toast.LENGTH_LONG).show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(NotesActivity.this);
                    builder.setTitle("Uyarı!");
                    builder.setMessage(noteSpin.getSelectedItem().toString()+ " - silinsin mi?");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            delete(""+noteSpin.getSelectedItem().toString());
                        }
                    });

                    builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create().show();
                }
            }
        });

        noteSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //daha önceden kaydolduysa tekrar eklemezsin güncellersin
                if(noteName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Not ismi boş olamaz",Toast.LENGTH_LONG).show();
                }else{
                    if(noteName.getText().toString().equals("admin")){
                        Toast.makeText(getApplicationContext(),"Not ismi admin olamaz",Toast.LENGTH_LONG).show();
                    }else{
                        if(arr.contains(noteName.getText().toString())) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(NotesActivity.this);
                            builder.setTitle("Uyarı!");
                            builder.setMessage("Aynı isimde kayıtlı dosya mevcut. Üzerine yazılsın mı?");
                            builder.setCancelable(false);

                            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //yeni oluşan not
                                    createFile(noteName.getText().toString(), noteTxt.getText().toString());
                                    noteName.setEnabled(true);
                                }
                            });

                            builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder.create().show();
                        }else{
                            createFile(noteName.getText().toString(), noteTxt.getText().toString());
                            noteName.setEnabled(true);
                        }
                    }
                }
            }
        });


    }

    //openFileOutput -- dosyanın yazmak için açılmasını sağlar
    //fileinputStream -- okuma için
    //context.modeprivate -- sadece bizim uygulamamız yazabilir.
    public void createFile(String filename, String context){
        try{
            if(!filename.equals("admin")){
                FileOutputStream fos =  openFileOutput(filename, Context.MODE_PRIVATE);
                String mesaj= noteTxt.getText().toString();
                fos.write(mesaj.getBytes());
                fos.close();
                noteName.setText("");
                noteTxt.setText("");
                setAdminFile(filename);
                Toast.makeText(this, this.getFilesDir() +  "  -  "+ filename, Toast.LENGTH_LONG).show();
            }else{
                FileOutputStream fos;
                if(context.equals("")){
                    fos=  openFileOutput(filename, Context.MODE_APPEND);
                }else{
                    fos =  openFileOutput(filename, Context.MODE_PRIVATE);
                }
                //ilk oluşurken ""
                //sonradan güncelleme için içindeki tüm string :)
                fos.write(context.getBytes());
                fos.close();
            }

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
            noteName.setText(filename);
            //burda tamamını okut belki enter basılıdır.
            noteTxt.setText(read.readLine());
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setAdminFile(String filename){
        try{
            FileInputStream fis = openFileInput("admin");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader read =new BufferedReader(isr);
            String tmp;
            Boolean flag=true;
            while((tmp=read.readLine())!=null && flag){
                if(filename.equals(tmp)){
                    flag=false;
                }
            }
            fis.close();
            if(flag){
                FileOutputStream fos = openFileOutput("admin", Context.MODE_APPEND);
                fos.write((filename+"\n").getBytes());
                arr.add(filename);
                fos.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void delete(String filename){
        File dir = getFilesDir();
        File file = new File(dir, filename);
        boolean deleted = file.delete();
        //dosya silindi
        arr.remove(filename);

        try{
            FileInputStream fis = openFileInput("admin");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader read =new BufferedReader(isr);
            String tmp, context="";
            while((tmp=read.readLine())!=null){
                if(!tmp.equals(filename)){
                    context=context+tmp+"\n";
                }
            }
            createFile("admin", context);
            fis.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), filename+" - silindi", Toast.LENGTH_LONG).show();
        noteName.setText("");
        noteName.setEnabled(true);
        noteTxt.setText("");
        noteSpin.setAdapter(arrayAdapter);
    }

}
