package com.example.myapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.myapp.ui.share.share;
import com.github.anastr.speedviewlib.SpeedView;
import com.github.anastr.speedviewlib.components.Section;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeTempLive extends Fragment {

    private TextView max_temp, avg_temp, min_temp;

    private SpeedView speedView;

    private LineChart mchart;
    private LineDataSet set1;
    private LineData data;
    private ArrayList<ILineDataSet> dataSets;
    private float compare, u, max, avg, min1, max1 = 0f;
    private float u1 = 1f;
    private float min = 3000f;
    private float compare2 = 3001f;
    private int change = 0;
    private Activity activity;

    private Boolean endloop = true;

    private final String  node = "Temperature";
    private final String path = "http://kapilsz.pythonanywhere.com/";
    //private final String path = "http://10.117.156.30:5000/";

    public static share newInstance() {
        return new share();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        activity = getActivity();
        View v = inflater.inflate(R.layout.fragment_home_temp_live, container, false);
//gauge_meter
        speedView = v.findViewById(R.id.metergauge_temp);
        speedView.setCenterCircleRadius(40);
        speedView.clearAnimation();
        speedView.setUnit(" °C");
        speedView.setUnitTextColor(Color.DKGRAY);
        speedView.setMaxSpeed(30);
        speedView.setSpeedTextColor(Color.DKGRAY);
        speedView.setSpeedTextSize(60f);
        speedView.setUnitTextSize(60f);
        speedView.setStartDegree(180);

        speedView.getSections().clear();
        speedView.addSections(new Section(0f, 1f, Color.parseColor("#A0C25A"), speedView.dpTOpx(30f)));

//data statics
        max_temp = v.findViewById(R.id.max_temp);
        avg_temp = v.findViewById(R.id.avg_temp);
        min_temp = v.findViewById(R.id.min_temp);

//linechart
        mchart = v.findViewById(R.id.linechart_temp);
        mchart.setDragEnabled(true);
        mchart.setScaleEnabled(false);
        mchart.getAxisRight().setEnabled(false);
        //YAxis yAxis =mchart.getAxisLeft();
        //yAxis.setAxisMaximum(25);
        //yAxis.setAxisMinimum(5);
        mchart.getXAxis().setEnabled(false);
        mchart.setDrawBorders(true);
        mchart.setDescription(null);

        ArrayList<Entry> yvalues = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Do your task
                String dummy = "dii";
                RequestBody formbody = new FormBody.Builder().add("sample", dummy).build();
                Request request = new Request.Builder().url(path + node).post(formbody).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   // Toast.makeText(v.getContext(), "server down", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }}
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String g = response.body().string();

                        if (max < compare) {
                            max1 = compare;
                            max = compare;
                        }
                        if (min > compare2){
                            min1 = compare2;
                            min = compare2;
                        }
                        if (u1 !=1) {
                            avg = Math.round(100*(avg * (u1-2) + compare) / (u1-1))/100f;
                        }
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void run() {
                                    float u = Float.valueOf(g);
                                    compare = u;
                                    compare2 = compare;
                                    yvalues.add(new Entry(change, u));
                                    change = change + 1;
                                    u1 +=1;
                                    LineDataSet set1 = new LineDataSet(yvalues, "Temperature °C");
                                    // set1.setFillAlpha(110);
                                    set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                                    set1.setCubicIntensity(0.2f);
                                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                                    dataSets.add(set1);
                                    mchart.moveViewToX(500);
                                    LineData data = new LineData(dataSets);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        set1.setColors(Color.LTGRAY);
                                    }
                                    mchart.setScaleEnabled(true);
                                    mchart.setPinchZoom(true);
                                    mchart.setVisibleXRangeMaximum(25);
                                    mchart.getCenterOfView();
                                    mchart.moveViewTo(change, u, YAxis.AxisDependency.LEFT);
                                    mchart.notifyDataSetChanged();
                                    mchart.invalidate();
                                    set1.setLineWidth(2f);
                                    set1.setValueTextSize(0);
                                    set1.setDrawCircles(false);
                                    mchart.setData(data);
                                    min_temp.setText("MIN : "+ min1   + " °C");
                                    avg_temp.setText("AVG : "+ avg   + " °C");
                                    max_temp.setText("MAX : "+ max1   + " °C");

                                    speedView.speedTo(compare);

                                    if (speedView.getSpeed()>15 || speedView.getSpeed() < 20){
                                        speedView.getSections().clear();
                                        set1.setColors(Color.argb(23, 152f, 251f, 152f));
                                        speedView.addSections(new Section(0f, 1f, Color.argb(23, 152f, 251f, 152f), speedView.dpTOpx(30f)));
                                    }
                                    if (speedView.getSpeed()>=20.1){
                                        speedView.getSections().clear();
                                        set1.setColors(Color.RED);
                                        speedView.addSections(new Section(0f, 1f, Color.RED, speedView.dpTOpx(30f)));
                                    }
                                    if (speedView.getSpeed()<=15){
                                        speedView.getSections().clear();
                                        set1.setColors(Color.parseColor("#A0C25A"));
                                        speedView.addSections(new Section(0f, 1f, Color.parseColor("#A0C25A"), speedView.dpTOpx(30f)));
                                    }
                                }
                            });
                            response.close();
                        }
                    }
                });

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }, 0, 1500);
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        Thread.interrupted();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Thread.interrupted();
    }
}


