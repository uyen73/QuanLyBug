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
import com.camuyen.quanlybug.adapter.IssueAdapter;
import com.camuyen.quanlybug.adapter.JobAdapter;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.Issue;
import com.camuyen.quanlybug.model.Jobs;
import com.camuyen.quanlybug.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;


public class JobFragment extends Fragment {
    RecyclerView listJobs;
    DBQuanLyBug database;
    JobAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        getWidget(view);
        addAction(view);

        return view;
    }

    private void getWidget(View view) {
        listJobs = view.findViewById(R.id.listJobs);
        database = new DBQuanLyBug();
        database.getJobsInfo(new DBQuanLyBug.JobsCallBack() {
            @Override
            public void onIssuesLoaded(List<Jobs> jobs) {
                adapter = new JobAdapter(jobs);
                listJobs.setLayoutManager(new LinearLayoutManager(getContext()));
                listJobs.setAdapter(adapter);
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }
    private void addAction(View view){

    }
}