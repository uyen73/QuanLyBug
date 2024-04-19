package com.camuyen.quanlybug.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.profile.ProfileActivity;


public class JobFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        getWidget(view);
        addAction(view);

        return view;
    }

    private void getWidget(View view) {


    }
    private void addAction(View view){

    }
}