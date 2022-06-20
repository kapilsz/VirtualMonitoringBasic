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
import com.example.myapp.ui.home.home_VPA_ethylene;
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

public class HomeEthylene extends AppCompatActivity implements SensorEventListener {

    private  TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private home_VPA_ethylene adapter;

    private String[] titles = new String[]{"Live", "Weekly", "Monthly", "Yearly"};

    private static final String TAG = "homeEthylene";
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
        setContentView(R.layout.activity_home_ethylene);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Temperature (Demo - Accelerometer)");

        refresh_temp = findViewById(R.id.refresh_temp);
        refresh_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        //alert database
        appDatabase = Room.databaseBuilder(this, AppDatabase.class,"db_contacts").allowMainThreadQueries().build();
        contactsDAO = appDatabase.getContactDAO();
        history_temp = findViewById(R.id.history_alerts);
        history_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeEthylene.this, ShowAll.class);
                startActivity(intent);
            }
        });

        //GaugeMeter
        speedView = findViewById(R.id.metergauge1);
        speedView.clearAnimation();
        speedView.setUnit(" °C");
        speedView.setUnitTextColor(Color.DKGRAY);
        speedView.setMaxSpeed(30);
        speedView.setSpeedTextColor(Color.DKGRAY);
        speedView.setSpeedTextSize(70f);
        speedView.setUnitTextSize(70f);
        speedView.setStartDegree(180);

        speedView.getSections().clear();
        speedView.addSections(new Section(0f, 1f, Color.parseColor("#A0C25A"), speedView.dpTOpx(30f)));

        //tablayout
        viewPager2 = findViewById(R.id.viewpager_homeethylene);
        tabLayout = findViewById(R.id.tabLayout_home_ethylene);
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new home_VPA_ethylene(fragmentManager,getLifecycle());
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

        // sensor values
        /*xValue = (TextView) findViewById(R.id.xValue);
        yValue = (TextView) findViewById(R.id.yValue);
        zValue = (TextView) findViewById(R.id.zValue);
        wValue = (TextView) findViewById(R.id.wValue);*/

        //  home_temp_value = (TextView) findViewById(R.id.home_temp_value_live);


        // initializing sensormanager to get values/data
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for(int i=0; i<sensors.size(); i++){
            Log.d(TAG, "onCreate: Sensor "+ i + ": " + sensors.get(i).toString());
        }

        if (mAccelerometer != null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        mChart = (LineChart) findViewById(R.id.chart1);

        // enable description text
        mChart.getDescription().setEnabled(false);      //true

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        // add empty data
        mChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(25f);
        leftAxis.setAxisMinimum(5f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        mChart.getAxisLeft().setDrawGridLines(true);
        mChart.getXAxis().setDrawGridLines(true);
        mChart.setDrawBorders(true);

        feedMultiple();

    }

    private class DatabaseOperation extends AsyncTask<Void,Void,Boolean> {


        @Override
        protected Boolean doInBackground(Void... voids) {

            Contacts contact = new Contacts();
            contact.setFirstname("Red Alert : Temperature Above 20°C ".trim());
            contact.setLastname(currentTime.toString().trim());
            contact.setPhoneNumber(String.valueOf(System.currentTimeMillis()));
            contact.setCreateDate(new Date());
            if(speedView.getSpeed()>10){
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
                Toast.makeText(HomeEthylene.this,"Inserted",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(HomeEthylene.this,"Invalid Input",Toast.LENGTH_SHORT).show();
        }
    }

    ArrayList<Integer> colors = new ArrayList<>();

    private void addEntry(SensorEvent event) {

        LineData data = mChart.getData();
// timer
        Date dateNow = new Date();   // in milliseconds
        long diff = dateNow.getTime() - lastDate.getTime();
        long diffSeconds = diff / 100 % 60;

        if (diffSeconds>4) {   // just change this to your desired interval
            lastDate = new Date();   // get the time now

            if (data != null) {

                ILineDataSet set = data.getDataSetByIndex(0);
                // set.addEntry(...); // can be called as well

                if (set == null) {
                    set = createSet();

                    data.addDataSet(set);

                }
                //   data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 80) + 10f), 0);
                data.addEntry(new Entry(set.getEntryCount(), event.values[1] + 11), 0);
                data.notifyDataChanged();


                speedView.speedTo((float) (event.values[1] + 11));               //Gauge Meter View Value

                /*float temp_val = (float) (Math.round((event.values[1]*100.0))/100.0) + 11f;   // round off to 2 decimal
                float co2_val = (float) (Math.round(event.values[0])) + 540f;
                float humidity_val = (float) (Math.round((event.values[2]*10.0))/100.0) + 98.9f;
                float ethylene_val = (float) (Math.round((event.values[2]))/100.0) + 0.3f;


                xValue.setText("Temp        : "+ temp_val + " °C");
                yValue.setText("CO2          : "+ co2_val + " ppm");
                zValue.setText("Humidity : "+ humidity_val + " %");
                wValue.setText("Ethylene  : "+ ethylene_val + " ppm");*/

// Gauge Color Codes

                if (speedView.getSpeed()>15.0 || speedView.getSpeed() < 20.2){
                    speedView.getSections().clear();
                    speedView.addSections(new Section(0f, 1f, Color.YELLOW, speedView.dpTOpx(30f)));
                }
                if (speedView.getSpeed()>=20.2){
                    speedView.getSections().clear();
                    speedView.addSections(new Section(0f, 1f, Color.RED, speedView.dpTOpx(30f)));
                }
                if (speedView.getSpeed()<15){
                    speedView.getSections().clear();
                    speedView.addSections(new Section(0f, 1f, Color.parseColor("#A0C25A"), speedView.dpTOpx(30f)));
                }

//pop up msgs
                if (speedView.getSpeed() > 20.2) {

                    DatabaseOperation databaseOperation = new DatabaseOperation();
                    databaseOperation.execute();

                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeEthylene.this);

                    builder.setCancelable(true);
                    builder.setTitle("Alert \uD83D\uDD14");
                    builder.setMessage("This is Alert 1: Tempearture above 20 °C");

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                }

                // let the chart know it's data has changed
                mChart.notifyDataSetChanged();

                // limit the number of visible entries
                mChart.setVisibleXRangeMaximum(30);                                     // x count
                // mChart.setVisibleYRange(30, AxisDependency.LEFT);

                // move to the latest entry
                mChart.moveViewToX(data.getEntryCount());

            }

        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Temperature");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.BLUE);

        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        //set.setDrawFilled(true);
        //  set.setFillColor(Color.BLACK);

        return set;
    }

    private void feedMultiple() {

        if (thread != null){
            thread.interrupt();
        }

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true){
                    plotData = true;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (thread != null) {
            thread.interrupt();
        }
        mSensorManager.unregisterListener(this);

    }
    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        if(plotData){
            addEntry(event);
            plotData = true;
        }

        Log.d(TAG, "onSensorChanged: X:"+ event.values[0] + "Y:"+ event.values[1]+ "Z:"+ event.values[2]);

/*        xValue.setText("xValue: "+ event.values[0]);

    // templive.setText((int) (event.values[0] + 10));
        yValue.setText("yValue: "+ event.values[1]);
        zValue.setText("zValue: "+ event.values[2]);
     //   home_temp_value.setText(" "+ event.values[1] +10 +"°C");*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onDestroy() {
        mSensorManager.unregisterListener(HomeEthylene.this);
        thread.interrupt();
        super.onDestroy();
    }
}