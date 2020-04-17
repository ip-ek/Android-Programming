package com.ipk.mobilodev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


public class PersonActivity extends AppCompatActivity {

    RecyclerView.LayoutManager layoutManager;

    RecyclerView recyclerView;
    PersonAdapter adapter;
    Person person= new Person();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        recyclerView = (RecyclerView) findViewById(R.id.person_rcy);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PersonAdapter(person.getData(), this);
        recyclerView.setAdapter(adapter);
    }
}
