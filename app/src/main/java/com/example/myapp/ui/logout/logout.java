package com.example.myapp.ui.logout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapp.MainActivity;
import com.example.myapp.R;
import com.example.myapp.databinding.LogoutFragmentBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class logout extends Fragment {
    private LogoutFragmentBinding binding;

    private static final String TAG = "StreamActivity";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        LogoutViewModel logoutViewModel =
                new ViewModelProvider(this).get (LogoutViewModel.class);
        binding = LogoutFragmentBinding.inflate (inflater, container, false);
        View root = binding.getRoot ();
        Intent i = new Intent (binding.getRoot ().getContext (), MainActivity.class);
        startActivity (i);

        return root;

}
}