package com.camuyen.quanlybug.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.adapter.PeopleAdapter;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.User;
import com.camuyen.quanlybug.profile.ProfileActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class PeopleFragment extends Fragment {
    ImageView imgToProfile;
    RecyclerView listPeople;
    DBQuanLyBug database;
    PeopleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        getWidget(view);
        addAction(view);
        return view;
    }
    private void getWidget(View view) {
        listPeople = view.findViewById(R.id.listPeople);
        listPeople.setLayoutManager(new LinearLayoutManager(getActivity()));
        database = new DBQuanLyBug();

        database.getUsers(new DBQuanLyBug.UCallBack() {
            @Override
            public void onULoaded(List<User> users) {
                adapter = new PeopleAdapter(users);
                listPeople.setLayoutManager(new LinearLayoutManager(getActivity()));
                listPeople.setAdapter(adapter);
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }
    private void addAction(View view){

    }
}