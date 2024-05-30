package com.camuyen.quanlybug.bugs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.camuyen.quanlybug.MainActivity;
import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.Bugs;
import com.camuyen.quanlybug.model.Devices;
import com.camuyen.quanlybug.model.NotificationItem;
import com.camuyen.quanlybug.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddBugActivity extends AppCompatActivity {
    ImageView imgBackProfile, imgShowCalender, imgAnhBug, imgMoAnh;
    EditText edtTenLoi, edtMucDoNghiemTrong, edtDeadline, edtMoTaLoi, edtSoBuoc, edtKetQuaMongMuon;
    CardView btnAddBug;
    DBQuanLyBug database;
    Calendar calendar;
    LinearLayout linearCacBuoc;
    Button btnOK;
    Spinner spinnerDev, spinnerQLAdd;

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
        spinnerQLAdd = findViewById(R.id.spinnerQLAdd);
        imgAnhBug = findViewById(R.id.imgAnhBug);
        imgMoAnh = findViewById(R.id.imgMoAnh);
        database = new DBQuanLyBug();

        database.getUsers(new DBQuanLyBug.UCallBack() {
            @Override
            public void onULoaded(List<User> users) {
                List<User> qlList = new ArrayList<>();
                List<User> listDev = new ArrayList<>();
                for (User a : users){
                    if (a.getChucVu().equals("Dev")){
                        listDev.add(a);
                    }
                    String chucVu = a.getMaNhanVien().substring(0, 2);
                    if (a.getChucVu().equals("Tester")) {
                        qlList.add(a);
                    }
                    if (chucVu.equals("QL")) {
                        qlList.add(a);
                    }
                }
                // Tạo Adapter cho Spinner
                ArrayAdapter<User> adapter = new ArrayAdapter<>(AddBugActivity.this, android.R.layout.simple_spinner_item, listDev);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Gắn Adapter với Spinner
                spinnerDev.setAdapter(adapter);

                // Tạo Adapter cho Spinner
                ArrayAdapter<User> adapterQL = new ArrayAdapter<>(AddBugActivity.this, android.R.layout.simple_spinner_item, qlList);
                adapterQL.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Gắn Adapter với Spinner
                spinnerQLAdd.setAdapter(adapterQL);
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
                            int m = bugs.size() - 1;
                            String str = bugs.get(m).getMaBug();
                            // Lấy ra phần số từ chuỗi ID
                            String numberPart = str.substring(3);
                            // Chuyển đổi sang số và tăng giá trị lên 1
                            int number = Integer.parseInt(numberPart) + 1;
                            // Chuyển lại về dạng chuỗi và thêm vào "CMT"
                            String id = String.format("CMT%03d", number);


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
                    EditText editText = getEditText(i);
                    linearCacBuoc.addView(editText);
                }
            }
        });
        imgMoAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở Intent để chọn ảnh từ thư viện
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageLauncher.launch(intent);
            }
        });

    }

    @NonNull
    private EditText getEditText(int i) {
        EditText editText = new EditText(AddBugActivity.this);


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

    private void sendNotification(String ngGui, String ngNhan) {
        database.getUserInfor(new DBQuanLyBug.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                try {
                    database.getDevices(new DBQuanLyBug.DeviceCallBack() {
                        @Override
                        public void onDeviceLoaded(List<Devices> devices) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                JSONObject notificationObj = new JSONObject();
                                String title = "Bug mới cần bạn fix";
                                String body = user.getTen() + " vừa thêm 1 bug cho bạn.";
                                notificationObj.put("title", title);
                                notificationObj.put("body", body);

                                jsonObject.put("notification", notificationObj);
                                for (Devices a : devices){
                                    if (a.getMaNhanVien().equals(ngNhan)){
                                        jsonObject.put("to", a.getToken());
                                        database.getNotifications(new DBQuanLyBug.NotificationCallback() {
                                            @Override
                                            public void onNotificationLoaded(List<NotificationItem> notification) {
                                                int size = notification.size() + 1;
                                                String maNotification = "NTF" + database.convertMa(size);
                                                database.getBugsInfo(new DBQuanLyBug.BugsCallBack() {
                                                    @Override
                                                    public void onBugsLoaded(List<Bugs> bugs) {
                                                        int size = bugs.size() + 1;
                                                        String id = "BUG" + convertSoBug(size);
                                                        database.addNewNotification(maNotification, new NotificationItem(maNotification, a.getToken(), title, body, false, id));
                                                    }

                                                    @Override
                                                    public void onError(Exception e) {

                                                    }
                                                });

                                            }
                                        });
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
        String maQuanLy = "";
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
            nameDev = selectedUser.getTen();
            maNhanVien = selectedUser.getMaNhanVien();
        }
        User quanLy = (User) spinnerQLAdd.getSelectedItem();
        if (quanLy != null){
            maQuanLy = quanLy.getMaNhanVien();
        }
        return new Bugs("maBug", maQuanLy, tenLoi, moTaLoi, "anh", cacBuoc,
                ketQuaMongMuon, database.convertToDate(deadline), "trangthai",
                nameDev, mucDoNghiemTrong, "maVande", "maDuAn", maNhanVien,
                database.convertToDate(deadline));
    }
    ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // Lấy URI của ảnh từ Intent
                        Uri imageUri = result.getData().getData();
                        database.getBugsInfo(new DBQuanLyBug.BugsCallBack() {
                            @Override
                            public void onBugsLoaded(List<Bugs> bugs) {
                                int size = bugs.size() + 1;
                                String id = "BUG" + convertSoBug(size);
                                String fileName = id + ".jpg";

                                // Thực hiện upload ảnh lên Firebase Storage
                                uploadImage(imageUri, fileName);

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });

                    }
                }
            });

    private void uploadImage(Uri imageUri, String fileName) {
        // Tham chiếu đến thư mục trong Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Bugs/" + fileName);

        // Upload ảnh lên Firebase Storage
        UploadTask uploadTask = storageRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Ảnh đã được upload thành công, lấy URL của ảnh
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                // Hiển thị ảnh trong ImageView
                Picasso.get().load(imageUrl).into(imgAnhBug);
                imgAnhBug.setVisibility(View.VISIBLE);
                imgMoAnh.setVisibility(View.GONE);
            });
        }).addOnFailureListener(exception -> {
            // Xảy ra lỗi khi upload ảnh
            Toast.makeText(this, "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}