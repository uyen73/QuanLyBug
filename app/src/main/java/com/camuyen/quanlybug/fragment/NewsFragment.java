package com.camuyen.quanlybug.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.login.LoginActivity;
import com.camuyen.quanlybug.model.User;
import com.google.firebase.auth.FirebaseAuth;


public class NewsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        DBQuanLyBug database = new DBQuanLyBug();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        database.getUserInfor(mAuth, new DBQuanLyBug.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                System.out.println(user.getHoTen());
            }
        });


        return view;
    }
}