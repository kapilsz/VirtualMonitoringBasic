package com.example.myapp.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapp.HomeTempLive;
import com.example.myapp.HomeTempMonthly;
import com.example.myapp.HomeTempWeekly;
import com.example.myapp.HomeTempYearly;

import java.util.ArrayList;

public class home_VPA_temp extends FragmentStateAdapter {

    public home_VPA_temp(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeTempLive();
            case 1:
                return new HomeTempWeekly();
            case 2:
                return new HomeTempMonthly();
            case 3:
                return new HomeTempYearly();
        }
        return new HomeTempLive();
    }

    @Override
    public int getItemCount() {

        return 4;
    }
}

