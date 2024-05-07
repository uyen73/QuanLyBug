package com.camuyen.quanlybug.projects;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.camuyen.quanlybug.MainActivity;
import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.Project;

import java.util.List;

public class AddProjectsActivity extends AppCompatActivity {
    ImageView imgBackProfile;
    CardView btnAddProject;
    EditText edtMaDA, edtTenDA, edtNgayBatDau, edtMoTa, edtMaNV, edtTenQuanLy;
    DBQuanLyBug database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_projects);
        getWidget();
        addAction();
    }

    private void getWidget() {
        imgBackProfile = findViewById(R.id.imgBackProfile);
        btnAddProject = findViewById(R.id.btnAddProject);
        edtMaDA = findViewById(R.id.edtMaDA);
        edtTenDA = findViewById(R.id.edtTenDA);
        edtNgayBatDau = findViewById(R.id.edtNgayBatDau);
        edtMoTa = findViewById(R.id.edtMoTa);
        edtMaNV = findViewById(R.id.edtMaNV);
        edtTenQuanLy = findViewById(R.id.edtTenQuanLy);
        database = new DBQuanLyBug();
    }

    private void addAction() {
        imgBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        btnAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Project project;
                if(checkBlank()){
                    project = getProject();
                    database.getProjectsInfo(new DBQuanLyBug.ProjectsCallBack() {
                        @Override
                        public void onProjectsLoaded(List<Project> projects) {
                            int size = projects.size() + 1;
                            String id = "DA" + size;
                            database.addNewProject(id, project);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                    Toast.makeText(AddProjectsActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddProjectsActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    private boolean checkBlank(){
        String error = "Bạn đang điền thiếu: ";
        String maDA = edtMaDA.getText().toString();
        if (maDA.isEmpty()){
            error += "mã dự án | ";
        }
        String tenDA = edtTenDA.getText().toString();
        if (tenDA.isEmpty()){
            error += "tên dự án | ";
        }
        String tenQuanLy = edtTenQuanLy.getText().toString();
        if (tenQuanLy.isEmpty()){
            error += "tên quản lý | ";
        }
        String ngayBatDau = edtNgayBatDau.getText().toString();
        if (ngayBatDau.isEmpty()){
            error += "ngày bắt đầu | ";
        }
        String moTa = edtMoTa.getText().toString();
        if (moTa.isEmpty()){
            error += "mô tả | ";
        }
        String maNV = edtMaNV.getText().toString();
        if (maNV.isEmpty()){
            error += "mã nhân viên tham gia | ";
        }

        if(error.equals("Bạn đang điền thiếu: ")){
            return true;
        }else {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private Project getProject(){
        String maDA = edtMaDA.getText().toString();
        String tenDA = edtTenDA.getText().toString();
        String tenQuanLy = edtTenQuanLy.getText().toString();
        String ngayBatDau = edtNgayBatDau.getText().toString();
        String moTa = edtMoTa.getText().toString();
        String maNV = edtMaNV.getText().toString();

        return new Project(maDA, maNV, tenQuanLy, tenDA, moTa, database.convertToDate(ngayBatDau), "Processing");
    }
}