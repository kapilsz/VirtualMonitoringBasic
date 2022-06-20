package com.example.myapp;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapp.database_alerts.AppDatabase;
import com.example.myapp.database_alerts.Contacts;
import com.example.myapp.database_alerts.ContactsDAO;
import com.example.myapp.database_alerts.ShowAll;

import java.util.Date;

public class MainActivity3 extends AppCompatActivity {

    private AppDatabase appDatabase;
    private EditText first,last,number;
    private Button insert,showall;
    private ContactsDAO contactsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        appDatabase = Room.databaseBuilder(this,AppDatabase.class,"db_contacts").allowMainThreadQueries().build();

        first = findViewById(R.id.first);
        last = findViewById(R.id.last);
        number = findViewById(R.id.number);
        insert = findViewById(R.id.button);
        showall = findViewById(R.id.button2);

        contactsDAO = appDatabase.getContactDAO();

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseOperation databaseOperation = new DatabaseOperation();
                databaseOperation.execute();


            }
        });

        showall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity3.this, ShowAll.class);
                startActivity(intent);

            }
        });

    }

    private class DatabaseOperation extends AsyncTask<Void,Void,Boolean> {


        @Override
        protected Boolean doInBackground(Void... voids) {

            Contacts contact = new Contacts();
            contact.setFirstname(first.getText().toString().trim());
            contact.setLastname(last.getText().toString().trim());
            contact.setPhoneNumber(number.getText().toString().trim());
            contact.setCreateDate(new Date());
            if(contactsDAO.getContactswithPhoneNumber(number.getText().toString().trim())==null){
                contactsDAO.insert(contact);
                return true;
            }
            else
                return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //super.onPostExecute(aBoolean);
            if(aBoolean==true)
                Toast.makeText(MainActivity3.this,"Inserted",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainActivity3.this,"Invalid Input",Toast.LENGTH_SHORT).show();
        }
    }
}
