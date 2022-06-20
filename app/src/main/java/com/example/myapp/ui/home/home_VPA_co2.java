package com.example.myapp.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapp.HomeCo2Live;
import com.example.myapp.HomeCo2Monthly;
import com.example.myapp.HomeCo2Weekly;
import com.example.myapp.HomeCo2Yearly;


import java.util.ArrayList;

public class home_VPA_co2 extends FragmentStateAdapter {

    public home_VPA_co2(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeCo2Live();
            case 1:
                return new HomeCo2Weekly();
            case 2:
                return new HomeCo2Monthly();
            case 3:
                return new HomeCo2Yearly();
        }
        return new HomeCo2Live();
    }

    @Override
    public int getItemCount() {

        return 4;
    }
}

