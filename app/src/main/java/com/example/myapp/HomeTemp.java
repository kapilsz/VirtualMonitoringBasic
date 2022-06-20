package com.example.myapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapp.R;
import com.example.myapp.database_alerts.AppDatabase;
import com.example.myapp.database_alerts.Contacts;
import com.example.myapp.database_alerts.ContactsDAO;
import com.example.myapp.database_alerts.ShowAll;
import com.example.myapp.ui.home.home;
import com.example.myapp.ui.home.home_VPA_temp;
import com.github.anastr.speedviewlib.SpeedView;
import com.github.anastr.speedviewlib.components.Section;
import com.github.anastr.speedviewlib.components.Style;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class HomeTemp extends AppCompatActivity{

    private  TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private home_VPA_temp adapter;

    private String[] titles = new String[]{"Live", "Weekly", "Monthly", "Yearly"};

    private static final String TAG = "homeTemp";
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor sensors;

    public static Date lastDate = new Date();
    private LineChart mChart;
    private Thread thread;
    private boolean plotData = true;

    TextView xValue, yValue, zValue, wValue;
   // TextView templive;
    SpeedView speedView;

    private Button history_temp;
    private AppDatabase appDatabase;
    private ContactsDAO contactsDAO;
    Date currentTime = Calendar.getInstance().getTime();

    private Button refresh_temp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_temp);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Temperature");

        //tablayout
        viewPager2 = findViewById(R.id.viewpager_hometemp);
        tabLayout = findViewById(R.id.tabLayout_home_temp);
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new home_VPA_temp(fragmentManager,getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

}