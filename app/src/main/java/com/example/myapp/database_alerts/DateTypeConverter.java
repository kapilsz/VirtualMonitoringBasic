package com.example.myapp.database_alerts;


import androidx.room.TypeConverter;

import java.util.Date;

public class DateTypeConverter {

    @TypeConverter
    public long convertDateToLong(Date date){
        return date.getTime();
    }

    @TypeConverter
    public Date convertLongToDate(Long date){
        return new Date(date);
    }

}
