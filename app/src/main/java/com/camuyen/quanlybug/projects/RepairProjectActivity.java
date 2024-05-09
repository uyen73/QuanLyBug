package com.camuyen.quanlybug.projects;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.Calendar;
import java.util.List;

public class RepairProjectActivity extends AppCompatActivity {
    ImageView imgBackProfile;
    CardView btnAddProject;
    EditText edtMaDA, edtTenDA, edtNgayBatDau, edtMoTa, edtTenQuanLy;
    RadioButton radioProcessing, radioDone;
    RadioGroup radioGroup;
    DBQuanLyBug database;
    Calendar calendar;
    ImageView imgShowCalender;
    String maDuAn = "";
    String trangThai = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_project);
        Intent intent = getIntent();
        maDuAn = intent.getStringExtra("maDuAn");
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
        edtTenQuanLy = findViewById(R.id.edtTenQuanLy);
        database = new DBQuanLyBug();
        imgShowCalender = findViewById(R.id.imgShowCalender);

        radioGroup = findViewById(R.id.radioGroup);
        radioProcessing = findViewById(R.id.radioProcessing);
        radioDone = findViewById(R.id.radioDone);
        database.getProjectsInfo(new DBQuanLyBug.ProjectsCallBack() {
            @Override
            public void onProjectsLoaded(List<Project> projects) {
                Project project = new Project();
                for (Project i : projects) {
                    if (i.getMaDuAn().equals(maDuAn)){
                        project = i;
                        break;
                    }
                }
                edtMaDA.setText(project.getMaDuAn());
                edtTenDA.setText(project.getTenDuAn());
                edtTenQuanLy.setText(project.getTenQuanLy());
                edtNgayBatDau.setText(database.convertToString(project.getNgayBatDau()));
                edtMoTa.setText(project.getMoTa());
                if (project.getTrangThai().equals("Done")){
                    radioDone.setChecked(true);
                    trangThai = "Done";
                } else {
                    radioProcessing.setChecked(true);
                    trangThai = "Processing";
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
        btnAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Project project;
                if(checkBlank()){
                    project = getProject();
                    database.addNewProject(maDuAn, project);

                    Toast.makeText(RepairProjectActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RepairProjectActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        imgShowCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioProcessing) {
                    trangThai = "Processing";
                } else if (checkedId == R.id.radioDone) {
                    trangThai = "Done";
                }
            }
        });

    }
    public String convertSoDuAn(int number) {
        return String.format("%03d", number);
    }
    public void showDatePickerDialog(View view) {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = String.format("%02d/%02d/%d", dayOfMonth, (month + 1), year);
                        edtNgayBatDau.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
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

        return new Project(maDA, tenQuanLy, tenDA, moTa, database.convertToDate(ngayBatDau), trangThai);
    }
}