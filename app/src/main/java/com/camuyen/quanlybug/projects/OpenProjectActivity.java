package com.camuyen.quanlybug.projects;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.Project;

import java.util.ArrayList;
import java.util.List;

public class OpenProjectActivity extends AppCompatActivity {
    TextView txtMoTa, txtNameProject;
    RecyclerView recycleview;
    String maDuAn = "";
    DBQuanLyBug database;
    ImageView imgBackProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new DBQuanLyBug();
        setContentView(R.layout.activity_open_project);
        getWidget();
        addAction();
    }

    private void getWidget() {
        txtMoTa = findViewById(R.id.txtMoTa);
        txtNameProject = findViewById(R.id.txtNameProject);
        recycleview = findViewById(R.id.recycleviewIssues);
        imgBackProfile = findViewById(R.id.imgBackProfile);


        Intent intent = getIntent();
        maDuAn = intent.getStringExtra("maDuAn");
        database.getProjectsInfo(new DBQuanLyBug.ProjectsCallBack() {
            @Override
            public void onProjectsLoaded(List<Project> projects) {
                for (Project a : projects){
                    if (a.getMaDuAn().equals(maDuAn)){
                        txtMoTa.setText(a.getMoTa());
                        txtNameProject.setText(a.getTenDuAn());

                    }
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    private void addAction() {
        imgBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }
}