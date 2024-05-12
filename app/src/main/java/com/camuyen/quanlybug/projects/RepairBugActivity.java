package com.camuyen.quanlybug.projects;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.camuyen.quanlybug.model.BugStatus;
import com.camuyen.quanlybug.model.Bugs;
import com.camuyen.quanlybug.model.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RepairBugActivity extends AppCompatActivity {
    ImageView imgBackProfile, imgShowCalender;
    EditText edtTenLoi, edtMucDoNghiemTrong, edtDeadline, edtMoTaLoi, edtSoBuoc, edtKetQuaMongMuon;
    CardView btnSuaBug;
    DBQuanLyBug database;
    Calendar calendar;
    LinearLayout linearCacBuoc;
    Button btnOK;
    Spinner spinnerDev, spinnerTrangThai, spinnerQL;
    String maBug = "";
    List<BugStatus> bugStatusList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_bug);
        database = new DBQuanLyBug();
        addColorStatus();
        Intent intent = getIntent();
        maBug = intent.getStringExtra("maBug");
        getWidget();
        addAction();


    }
    private void getWidget() {
        imgBackProfile = findViewById(R.id.imgBackProfile);
        imgShowCalender = findViewById(R.id.imgShowCalender);
        edtTenLoi = findViewById(R.id.edtTenLoi);
        edtMucDoNghiemTrong = findViewById(R.id.edtMucDoNghiemTrong);
        edtKetQuaMongMuon = findViewById(R.id.edtKetQuaMongMuon);
        edtDeadline = findViewById(R.id.edtDeadline);
        edtMoTaLoi = findViewById(R.id.edtMoTaLoi);
        btnSuaBug = findViewById(R.id.btnSuaBug);
        btnOK = findViewById(R.id.btnOK);
        edtSoBuoc = findViewById(R.id.edtSoBuoc);
        linearCacBuoc = findViewById(R.id.linearCacBuoc);
        spinnerDev = findViewById(R.id.spinnerDev);
        spinnerTrangThai = findViewById(R.id.spinnerTrangThai);
        spinnerQL = findViewById(R.id.spinnerQL);



        // Tạo Adapter cho Spinner
        ArrayAdapter<BugStatus> adapter = new ArrayAdapter<>(RepairBugActivity.this, android.R.layout.simple_spinner_item, bugStatusList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Gắn Adapter với Spinner
        spinnerTrangThai.setAdapter(adapter);




        database.getUsers(new DBQuanLyBug.UCallBack() {
            @Override
            public void onULoaded(List<User> users) {
                List<User> listDev = new ArrayList<>();
                List<User> qlList = new ArrayList<>();
                for (User user : users) {
                    if (user.getChucVu().equals("Dev")) {
                        listDev.add(user);
                    }
                    String chucVu = user.getMaNhanVien().substring(0, 2);
                    if (user.getChucVu().equals("Tester")) {
                        qlList.add(user);
                    }
                    if (chucVu.equals("QL")) {
                        qlList.add(user);
                    }
                }
                // Tạo Adapter cho Spinner
                ArrayAdapter<User> adapter = new ArrayAdapter<>(RepairBugActivity.this, android.R.layout.simple_spinner_item, listDev);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Gắn Adapter với Spinner
                spinnerDev.setAdapter(adapter);

                // Tạo Adapter cho Spinner
                ArrayAdapter<User> adapterQL = new ArrayAdapter<>(RepairBugActivity.this, android.R.layout.simple_spinner_item, qlList);
                adapterQL.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Gắn Adapter với Spinner
                spinnerQL.setAdapter(adapterQL);


                database.getBugsInfo(new DBQuanLyBug.BugsCallBack() {
                    @Override
                    public void onBugsLoaded(List<Bugs> bugs) {
                        Bugs bug = new Bugs();
                        for (Bugs a : bugs) {
                            if (a.getMaBug().equals(maBug)){
                                bug = a;
                                break;
                            }
                        }
                        for (int i = 0; i < spinnerDev.getCount(); i++) {
                            User currentUser = (User) spinnerDev.getItemAtPosition(i);
                            String maNhanVien = currentUser.getMaNhanVien();
                            if (maNhanVien.equals(bug.getMaNhanVien())) {
                                System.out.println(maNhanVien);
                                System.out.println(bug.getMaNhanVien());
                                spinnerDev.setSelection(i);
                                break;
                            }
                        }
                        for (int i = 0; i < spinnerQL.getCount(); i++) {
                            User currentUser = (User) spinnerQL.getItemAtPosition(i);
                            String maNhanVien = currentUser.getMaNhanVien();
                            if (maNhanVien.equals(bug.getMaQuanLy())) {
                                System.out.println(maNhanVien);
                                System.out.println(bug.getMaQuanLy());
                                spinnerQL.setSelection(i);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

            }

            @Override
            public void onError(Exception e) {

            }
        });
        database.getBugsInfo(new DBQuanLyBug.BugsCallBack() {
            @Override
            public void onBugsLoaded(List<Bugs> bugs) {
                Bugs bug = new Bugs();
                for (Bugs a : bugs) {
                    if (a.getMaBug().equals(maBug)){
                        bug = a;
                        break;
                    }
                }
                edtTenLoi.setText(bug.getTenBug());
                edtMucDoNghiemTrong.setText(bug.getMucDoNghiemTrong());
                edtKetQuaMongMuon.setText(bug.getKetQuaMongMuon());

                edtDeadline.setText(database.convertToString(bug.getDeadline()));
                edtMoTaLoi.setText(bug.getMoTaLoi());

                String[] cacBuoc = bug.getCacBuoc().split(" \\| ");
                String soBuoc = String.valueOf(cacBuoc.length);
                edtSoBuoc.setText(soBuoc);
                // Lấy số lượng dòng từ EditText
                String numberOfRowsString = edtSoBuoc.getText().toString();
                int numberOfRows = Integer.parseInt(numberOfRowsString);

                // Xóa tất cả các EditText cũ trong LinearLayout
                linearCacBuoc.removeAllViews();

                // Tạo và thêm các EditText vào LinearLayout
                for (int i = 0; i < numberOfRows; i++) {
                    EditText editText = new EditText(RepairBugActivity.this);
                    editText.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    editText.setHint("Bước " + (i + 1));
                    linearCacBuoc.addView(editText);
                    editText.setText(cacBuoc[i]);
                }
                for (int i = 0; i < spinnerTrangThai.getCount(); i++) {
                    BugStatus currentBugStatus = (BugStatus) spinnerTrangThai.getItemAtPosition(i);

                    String status = currentBugStatus.getStatus();
                    if (status.equals(bug.getTrangThai())) {
                        System.out.println(status);
                        System.out.println(bug.getTrangThai());
                        spinnerTrangThai.setSelection(i);
                        break;
                    }
                }


            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    private void addColorStatus() {
        bugStatusList.add(new BugStatus("New", "#fed0ca", "#8f202a"));
        bugStatusList.add(new BugStatus("Open", "#fed0ca", "#4f7398"));
        bugStatusList.add(new BugStatus("Fix", "#d3edbc", "#3f6c3f"));
        bugStatusList.add(new BugStatus("Pending", "#18a034", "#c0f8cf"));
        bugStatusList.add(new BugStatus("Reopen", "#f14747", "#ffe1e1"));
        bugStatusList.add(new BugStatus("Close", "#8d8586", "#f9f8f1"));
        bugStatusList.add(new BugStatus("Rejected", "#e6e6e6", "#645d63"));
    }

    private void addAction() {
        imgBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        imgShowCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        btnSuaBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bugs bug;
                if (checkBlank()){
                    bug = getBugs();
                    database.getBugsInfo(new DBQuanLyBug.BugsCallBack() {
                        @Override
                        public void onBugsLoaded(List<Bugs> bugs) {
                            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            String maDuAn = preferences.getString("maDuAn", "");

                            bug.setMaBug(maBug);
                            bug.setMaDuAn(maDuAn);

                            Date date = Calendar.getInstance().getTime();
                            bug.setNgayXuatHien(date);
                            System.out.println(maBug);
                            database.updateBug(maBug, bug);
                            System.out.println(database.convertToString(date));

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                    database.getBugsInfo(new DBQuanLyBug.BugsCallBack() {
                        @Override
                        public void onBugsLoaded(List<Bugs> bugs) {
                            Intent intent = new Intent();
                            Gson gson = new Gson();
                            String updatedBugListJson = gson.toJson(bugs);
                            intent.putExtra("updatedBugListJson", updatedBugListJson);
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });


                    Toast.makeText(RepairBugActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RepairBugActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy số lượng dòng từ EditText
                String numberOfRowsString = edtSoBuoc.getText().toString();
                int numberOfRows = Integer.parseInt(numberOfRowsString);

                // Xóa tất cả các EditText cũ trong LinearLayout
                linearCacBuoc.removeAllViews();

                // Tạo và thêm các EditText vào LinearLayout
                for (int i = 0; i < numberOfRows; i++) {
                    EditText editText = getEditText(i);
                    linearCacBuoc.addView(editText);

                }
            }
        });


    }
    private EditText getEditText(int i) {
        EditText editText = new EditText(RepairBugActivity.this);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,  // chiều rộng
                LinearLayout.LayoutParams.WRAP_CONTENT, 60// chiều cao
        );

        int marginInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()); // Chuyển dp sang px
        int paddingInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()); // Chuyển dp sang px
        layoutParams.setMargins(0, 0, 0, marginInDp);

        editText.setLayoutParams(layoutParams);
        editText.setHint("Bước " + (i + 1));
        editText.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp);
        editText.setBackgroundResource(R.drawable.custom_edit_text_rounded);
        return editText;
    }
    public String convertSoBug(int number) {
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
                        edtDeadline.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }
    private boolean checkBlank(){
        String error = "Bạn đang điền thiếu: ";
        String maDA = edtTenLoi.getText().toString();
        if (maDA.isEmpty()){
            error += "tên lỗi | ";
        }
        String tenDA = edtMucDoNghiemTrong.getText().toString();
        if (tenDA.isEmpty()){
            error += "mức độ nghiêm trọng | ";
        }
        String ketQuaMongMuon = edtKetQuaMongMuon.getText().toString();
        if (ketQuaMongMuon.isEmpty()){
            error += "dev fix | ";
        }
        String ngayBatDau = edtDeadline.getText().toString();
        if (ngayBatDau.isEmpty()){
            error += "deadline | ";
        }
        String moTa = edtMoTaLoi.getText().toString();
        if (moTa.isEmpty()){
            error += "mô tả lỗi| ";
        }

        if(error.equals("Bạn đang điền thiếu: ")){
            return true;
        }else {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private Bugs getBugs(){
        String tenLoi = edtTenLoi.getText().toString();
        String mucDoNghiemTrong = edtMucDoNghiemTrong.getText().toString();
        String ketQuaMongMuon = edtKetQuaMongMuon.getText().toString();
        String deadline = edtDeadline.getText().toString();
        String moTaLoi = edtMoTaLoi.getText().toString();
        String cacBuoc = "";
        String nameDev = "";
        String maNhanVien = "";
        String trangThai = "";
        String maQuanLy = "";
        // Lặp qua tất cả các EditText trong LinearLayout
        for (int i = 0; i < linearCacBuoc.getChildCount(); i++) {
            View view = linearCacBuoc.getChildAt(i);
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                String value = editText.getText().toString();
                if(i + 1 == linearCacBuoc.getChildCount()){
                    cacBuoc += value;
                }else{
                    cacBuoc += value + " | ";
                }
                System.out.println(cacBuoc);
            }
        }
        User selectedUser = (User)  spinnerDev.getSelectedItem();
        if (selectedUser != null){
            nameDev = selectedUser.getTen();
            maNhanVien = selectedUser.getMaNhanVien();
        }

        BugStatus bugStatus = (BugStatus)  spinnerTrangThai.getSelectedItem();
        if (bugStatus != null){
            trangThai = bugStatus.getStatus();
        }
        User quanLy = (User) spinnerQL.getSelectedItem();
        if (quanLy != null){
            maQuanLy = quanLy.getMaNhanVien();
        }

        return new Bugs("maBug", maQuanLy, tenLoi, moTaLoi, "anh", cacBuoc,
                ketQuaMongMuon, database.convertToDate(deadline), trangThai,
                nameDev, mucDoNghiemTrong, "maVande", "maDuAn", maNhanVien,
                database.convertToDate(deadline));
    }
}