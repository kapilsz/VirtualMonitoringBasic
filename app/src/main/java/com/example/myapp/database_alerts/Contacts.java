package com.example.myapp.database_alerts;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "contact")
public class Contacts {

    private String firstname;
    private String lastname;
    @PrimaryKey
    @NonNull
    private String phoneNumber;
    private Date createDate;

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
