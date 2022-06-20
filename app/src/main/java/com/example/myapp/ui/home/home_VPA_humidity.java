package com.example.myapp.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapp.HomeHumidityLive;
import com.example.myapp.HomeHumidityMonthly;
import com.example.myapp.HomeHumidityWeekly;
import com.example.myapp.HomeHumidityYearly;

import java.util.ArrayList;

public class home_VPA_humidity extends FragmentStateAdapter {

    public home_VPA_humidity(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeHumidityLive();
            case 1:
                return new HomeHumidityWeekly();
            case 2:
                return new HomeHumidityMonthly();
            case 3:
                return new HomeHumidityYearly();
        }
        return new HomeHumidityLive();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

