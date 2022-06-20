package com.example.myapp.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myapp.R;
import com.example.myapp.ui.share.share;


public class profile extends Fragment {


    public static share newInstance() {
        return new share();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);

        return v;
    }

}