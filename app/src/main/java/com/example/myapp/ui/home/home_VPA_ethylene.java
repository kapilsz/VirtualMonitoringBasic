package com.example.myapp.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapp.HomeEthyleneLive;
import com.example.myapp.HomeEthyleneMonthly;
import com.example.myapp.HomeEthyleneYearly;
import com.example.myapp.HomeEthyleneWeekly;


import java.util.ArrayList;

public class home_VPA_ethylene extends FragmentStateAdapter {

    public home_VPA_ethylene(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeEthyleneLive();
            case 1:
                return new HomeEthyleneWeekly();
            case 2:
                return new HomeEthyleneMonthly();
            case 3:
                return new HomeEthyleneYearly();
        }
        return new HomeEthyleneLive();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

