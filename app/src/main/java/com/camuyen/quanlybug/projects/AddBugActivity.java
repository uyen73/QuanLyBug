package com.camuyen.quanlybug.projects;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.camuyen.quanlybug.MainActivity;
import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.Bugs;
import com.camuyen.quanlybug.model.Devices;
import com.camuyen.quanlybug.model.Project;
import com.camuyen.quanlybug.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddBugActivity extends AppCompatActivity {
    ImageView imgBackProfile, imgShowCalender;
    EditText edtTenLoi, edtMucDoNghiemTrong, edtDeadline, edtMoTaLoi, edtSoBuoc, edtKetQuaMongMuon;
    CardView btnAddBug;
    DBQuanLyBug database;
    Calendar calendar;
    LinearLayout linearCacBuoc;
    Button btnOK;
    Spinner spinnerDev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bug);
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
        btnAddBug = findViewById(R.id.btnAddBug);
        btnOK = findViewById(R.id.btnOK);
        edtSoBuoc = findViewById(R.id.edtSoBuoc);
        linearCacBuoc = findViewById(R.id.linearCacBuoc);
        spinnerDev = findViewById(R.id.spinnerDev);

        database = new DBQuanLyBug();

        database.getUsers(new DBQuanLyBug.UCallBack() {
            @Override
            public void onULoaded(List<User> users) {
                // Tạo Adapter cho Spinner
                ArrayAdapter<User> adapter = new ArrayAdapter<>(AddBugActivity.this, android.R.layout.simple_spinner_item, users);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Gắn Adapter với Spinner
                spinnerDev.setAdapter(adapter);
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
        imgShowCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        btnAddBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bugs bug;
                if (checkBlank()){
                    bug = getBugs();
                    database.getBugsInfo(new DBQuanLyBug.BugsCallBack() {
                        @Override
                        public void onBugsLoaded(List<Bugs> bugs) {
                            int size = bugs.size() + 1;
                            String id = "BUG" + convertSoBug(size);

                            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            String maDuAn = preferences.getString("maDuAn", "");

                            bug.setMaBug(id);
                            bug.setMaDuAn(maDuAn);
                            bug.setTrangThai("Fix");
                            Date date = Calendar.getInstance().getTime();
                            bug.setNgayXuatHien(date);

                            database.addNewBug(id, bug);
                            System.out.println(database.convertToString(date));
                            String ngNhan = bug.getMaNhanVien();
                            database.getUserInfor(new DBQuanLyBug.UserCallback() {
                                @Override
                                public void onUserLoaded(User user) {
                                    String ngGui = user.getMaNhanVien();
                                    sendNotification(ngGui, ngNhan);
                                }
                            });

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                    Toast.makeText(AddBugActivity.this, "Thành công", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddBugActivity.this, MainActivity.class);
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
                    EditText editText = new EditText(AddBugActivity.this);
                    editText.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    editText.setHint("Bước " + (i + 1));
                    linearCacBuoc.addView(editText);
                }
            }
        });

    }

    private void sendNotification(String ngGui, String ngNhan) {
        database.getUserInfor(new DBQuanLyBug.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                try {
                    database.getDevices(new DBQuanLyBug.DeviceCallBack() {
                        @Override
                        public void onBugsLoaded(List<Devices> devices) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                JSONObject notificationObj = new JSONObject();
                                notificationObj.put("title", "Bug mới cần bạn fix");
                                notificationObj.put("body", user.getHoTen() + " vừa thêm 1 bug cho bạn");

                                jsonObject.put("notification", notificationObj);
                                for (Devices a : devices){
                                    if (a.getMaNhanVien().equals(ngNhan)){
                                        jsonObject.put("to", a.getToken());
                                        break;
                                    }
                                }
                                callAPI(jsonObject);
                            }catch (Exception e){

                            }

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });

                }catch (Exception e){

                }


            }
        });

    }
    public void callAPI(JSONObject jsonObject){
        MediaType JSON = MediaType.get("application/json");

        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer AAAAgaYXjzo:APA91bEWIBDwGNGMMOCIuP9WpT_OxQ3czOIOvbcn-54BlXVDZ-SkcHgophq_k35fTxaIRm3tTouqDezTAozynV9qa2qB_S7FtpO6uyd3S6f0mDAm8UhRKuDkK2KDLu7Y-yLM4OYDvk10")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });

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
        // Lặp qua tất cả các EditText trong LinearLayout
        for (int i = 0; i < linearCacBuoc.getChildCount(); i++) {
            View view = linearCacBuoc.getChildAt(i);
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                String value = editText.getText().toString();
                if(i + 1 == linearCacBuoc.getChildCount()){
                    cacBuoc += "Bước " + (i+1) + ": " + value;
                }else{
                    cacBuoc += "Bước " + (i+1) + ": " + value + " | ";
                }
                System.out.println(cacBuoc);
            }
        }
        User selectedUser = (User)  spinnerDev.getSelectedItem();
        if (selectedUser != null){
            nameDev = selectedUser.getHoTen();
            maNhanVien = selectedUser.getMaNhanVien();
        }
        return new Bugs("maBug", tenLoi, moTaLoi, "anh", cacBuoc,
                ketQuaMongMuon, database.convertToDate(deadline), "trangthai",
                nameDev, mucDoNghiemTrong, "maVande", "maDuAn", maNhanVien,
                database.convertToDate(deadline));
    }
}