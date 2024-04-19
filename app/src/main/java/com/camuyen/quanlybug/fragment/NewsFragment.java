package com.camuyen.quanlybug.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.login.LoginActivity;
import com.camuyen.quanlybug.model.User;
import com.camuyen.quanlybug.profile.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;


public class NewsFragment extends Fragment {

    ImageView imgToProfile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        DBQuanLyBug database = new DBQuanLyBug();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        imgToProfile = view.findViewById(R.id.imgToProfile);
        database.getUserInfor(new DBQuanLyBug.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                Toast.makeText(getActivity(), "Xin chào, " + user.getHoTen(), Toast.LENGTH_SHORT).show();
            }
        });
//        imgToProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ProfileActivity.class);
//                startActivity(intent);
//            }
//        });

        return view;
    }
}