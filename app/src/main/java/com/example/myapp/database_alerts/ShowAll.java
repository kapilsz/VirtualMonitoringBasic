package com.example.myapp.database_alerts;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.myapp.R;

import java.util.List;
import java.util.Objects;

public class ShowAll extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Adapter_items adapter;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Alerts History");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        appDatabase = Room.databaseBuilder(this,AppDatabase.class,"db_contacts").allowMainThreadQueries().build();
        ContactsDAO contactsDAO = appDatabase.getContactDAO();

        List<Contacts> contactsList;
        contactsList = contactsDAO.getAllContacts();
        adapter = new Adapter_items(contactsList);
        recyclerView.setAdapter(adapter);
    }
}
