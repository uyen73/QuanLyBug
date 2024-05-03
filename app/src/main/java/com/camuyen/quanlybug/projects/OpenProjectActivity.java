package com.camuyen.quanlybug.projects;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.adapter.IssueAdapter;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.Issue;
import com.camuyen.quanlybug.model.Project;

import java.util.ArrayList;
import java.util.List;

public class OpenProjectActivity extends AppCompatActivity {
    TextView txtMoTa, txtNameProject;
    RecyclerView recycleview;
    String maDuAn = "";
    DBQuanLyBug database;
    IssueAdapter adapter;
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
        database.getIssuesInfo(new DBQuanLyBug.IssueCallBack() {
            @Override
            public void onIssuesLoaded(List<Issue> issues) {
                List<Issue> list = new ArrayList<>();

                for (Issue a : issues){
                    if (a.getMaDuAn().equals(maDuAn)){
                        list.add(a);

                    }
                }
                adapter = new IssueAdapter(list, OpenProjectActivity.this);
                recycleview.setLayoutManager(new LinearLayoutManager(OpenProjectActivity.this));
                recycleview.setAdapter(adapter);

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