package com.example.myapp.ui.home;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.myapp.HomeCo2;
import com.example.myapp.HomeEthylene;
import com.example.myapp.HomeHumidity;
import com.example.myapp.HomeTemp;
import com.example.myapp.R;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class home extends Fragment {

    private TextView server_txt;

    private TextView live_temp;
    private TextView live_co2;
    private TextView live_humidity;
    private TextView live_ethylene;

    Activity context;
    private Activity activity;
    private CardView card_temp;
    private CardView card_co2;
    private CardView card_humidity;
    private CardView card_ethylene;

    private HomeViewModel mViewModel;

    private String path = "http://kapilsz.pythonanywhere.com/";
    //private String path = "http://10.117.156.30:5000/";

    public static com.example.myapp.ui.home.home newInstance() {

        return new com.example.myapp.ui.home.home();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.home_fragment, container, false);
        context = getActivity();
        activity = getActivity();

        server_txt = view.findViewById(R.id.server_txt);

        live_temp = view.findViewById(R.id.home_temp_value_live);
        live_humidity = view.findViewById(R.id.home_humidity_live_val);
        live_co2 = view.findViewById(R.id.home_co2_live_val);
        live_ethylene = view.findViewById(R.id.home_ethylene_live_val);

//okhttp_client
        OkHttpClient okHttpClient = new OkHttpClient();       //temp
        OkHttpClient okHttpClient2 = new OkHttpClient();        //co2
        OkHttpClient okHttpClient3= new OkHttpClient();         //humid
        OkHttpClient okHttpClient4 = new OkHttpClient();        //ethylene



        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // Do your task

                String dummy = "temp";
                String dummy2 = "co2";
                String dummy3 = "humid";
                String dummy4 = "c2h4";
                RequestBody formbody = new FormBody.Builder().add("sample", dummy).build();
                RequestBody formbody2 = new FormBody.Builder().add("sample", dummy2).build();
                RequestBody formbody3 = new FormBody.Builder().add("sample", dummy3).build();
                RequestBody formbody4 = new FormBody.Builder().add("sample", dummy4).build();


                Request request = new Request.Builder().url(path + "Temperature").post(formbody).build();
                Request request2 = new Request.Builder().url(path + "co2ppm").post(formbody2).build();
                Request request3 = new Request.Builder().url(path + "Humidity").post(formbody3).build();
                Request request4 = new Request.Builder().url(path + "Ethylene").post(formbody4).build();


                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                server_txt.setText("Server Down, Please Wait.. \uD83D\uDE2D");

                               // Toast.makeText(view.getContext(), "server down", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }}
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String g = response.body().string();
                        if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void run() {
                                live_temp.setText(g + "°C");
                                server_txt.setText("Server Is Active! \uD83D\uDE0A");
                            }
                        });
                        response.close();
                    }}
                });
                okHttpClient2.newCall(request2).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               // Toast.makeText(view.getContext(), "server down", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }}
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String g2 = response.body().string();

                        if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void run() {
                                live_co2.setText(g2 + "ppm");
                            }
                        });
                        response.close();
                    }}
                });
                okHttpClient3.newCall(request3).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                              //  Toast.makeText(view.getContext(), "server down", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }}
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String g3 = response.body().string();

                        if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void run() {
                                live_humidity.setText(g3 + " %");
                            }
                        });
                        response.close();
                    }}
                });
                okHttpClient4.newCall(request4).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                              //  Toast.makeText(view.getContext(), "server down", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }}
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String g4 = response.body().string();

                        if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void run() {
                                live_ethylene.setText(g4 + "ppm");
                            }
                        });
                        response.close();
                    }}
                });

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);
        return view;
    }

    public void onStart(){
        super.onStart();
        CardView card_temp = (CardView) context.findViewById(R.id.card_temp);
        CardView card_co2 = (CardView) context.findViewById(R.id.card_co2);
        CardView card_humidity = (CardView) context.findViewById(R.id.card_humidity);
        CardView card_ethylene = (CardView) context.findViewById(R.id.card_ethylene);

        card_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(context, HomeTemp.class);
                startActivity(intent1);
            }
        });
        card_humidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(context, HomeHumidity.class);
                startActivity(intent3);
            }
        });
        card_ethylene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(context, HomeEthylene.class);
                startActivity(intent4);
            }
        });
        card_co2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(context, HomeCo2.class);
                startActivity(intent2);
            }
        });

    }
}
