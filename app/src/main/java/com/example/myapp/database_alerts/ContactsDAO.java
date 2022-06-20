package com.example.myapp.database_alerts;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactsDAO {

    @Insert
    public void insert(Contacts... contacts);

    @Update
    public void update(Contacts... contacts);

    @Delete
    public void delete(Contacts contacts);

    @Query("Select * from contact")
    public List<Contacts> getAllContacts();

    @Query("Select * from contact where phoneNumber = :number")
    public Contacts getContactswithPhoneNumber(String number);

}