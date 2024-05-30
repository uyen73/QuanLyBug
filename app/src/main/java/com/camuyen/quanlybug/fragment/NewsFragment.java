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
import android.widget.TextView;
import android.widget.Toast;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.adapter.ProjectAdapter;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.login.LoginActivity;
import com.camuyen.quanlybug.model.Project;
import com.camuyen.quanlybug.model.User;
import com.camuyen.quanlybug.profile.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends Fragment {
    RecyclerView recyclerView;
    ProjectAdapter projectAdapter;
    List<Project> list;
    DBQuanLyBug database;
    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new FragmentVisibleEvent());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        database = new DBQuanLyBug();
        getWidget(view);
        addAction(view);
        getData();
        return view;
    }

    private void getWidget(View view) {
        recyclerView = view.findViewById(R.id.listProjects);
        database = new DBQuanLyBug();
        list = new ArrayList<>();
        database.getProjectsInfo(new DBQuanLyBug.ProjectsCallBack() {
            @Override
            public void onProjectsLoaded(List<Project> projects) {
                projectAdapter = new ProjectAdapter(projects, getActivity());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(projectAdapter);
            }

            @Override
            public void onError(Exception e) {

            }

        });

    }

    private void addAction(View view) {

    }
    private void getData() {

    }
}