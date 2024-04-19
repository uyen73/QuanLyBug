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

public class PeopleFragment extends Fragment {
    ImageView imgToProfile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        getWidget(view);
        addAction(view);
        return view;
    }
    private void getWidget(View view) {
//        imgToProfile = view.findViewById(R.id.imgToProfile);

    }
    private void addAction(View view){
//        imgToProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ProfileActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}