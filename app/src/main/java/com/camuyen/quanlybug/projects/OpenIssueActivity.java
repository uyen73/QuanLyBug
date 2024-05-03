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
import com.camuyen.quanlybug.adapter.BugAdapter;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.Bugs;
import com.camuyen.quanlybug.model.Issue;

import java.util.ArrayList;
import java.util.List;

public class OpenIssueActivity extends AppCompatActivity {
    ImageView imgBackIssue;
    TextView txtNameIssue, txtMoTa, txtTenDev, txtTenTest, txtDueDateDev, txtDueDateTest;
    RecyclerView recyclerView;
    BugAdapter adapter;
    DBQuanLyBug database;
    Issue issueIntent = new Issue();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new DBQuanLyBug();
        setContentView(R.layout.activity_open_issue);

        getWidget();
        addAction();
    }

    private void getWidget() {
        imgBackIssue = findViewById(R.id.imgBackIssue);
        txtNameIssue = findViewById(R.id.txtNameIssue);
        txtMoTa = findViewById(R.id.txtMoTa);
        txtTenDev = findViewById(R.id.txtTenDev);
        txtTenTest = findViewById(R.id.txtTenTest);
        txtDueDateDev = findViewById(R.id.txtDueDateDev);
        txtDueDateTest = findViewById(R.id.txtDueDateTest);
        recyclerView = findViewById(R.id.recycleviewBugs);

        Intent intent = getIntent();
        issueIntent = (Issue) intent.getSerializableExtra("issue");

        assert issueIntent != null;
        txtNameIssue.setText(issueIntent.getTenVanDe());
        txtMoTa.setText(issueIntent.getMoTa());
        txtTenDev.setText(issueIntent.getDev());
        txtTenTest.setText(issueIntent.getTest());
        txtDueDateDev.setText(issueIntent.getDueDateDev());
        txtDueDateTest.setText(issueIntent.getDueDateTest());

        database.getBugsInfo(new DBQuanLyBug.BugsCallBack() {
            @Override
            public void onBugsLoaded(List<Bugs> bugs) {
                List<Bugs> list = new ArrayList<>();
                for (Bugs a : bugs){
                    if(a.getMaVanDe().equals(issueIntent.getMaVanDe())){
                        list.add(a);
                    }
                }
                adapter = new BugAdapter(list);
                recyclerView.setLayoutManager(new LinearLayoutManager(OpenIssueActivity.this));
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    private void addAction() {
        imgBackIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });


    }
}