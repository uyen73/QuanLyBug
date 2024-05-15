package com.camuyen.quanlybug.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.login.LoginActivity;
import com.camuyen.quanlybug.model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CreateAccountActivity extends AppCompatActivity {
    ImageView imgBackProfile;
    EditText edtHoTen, edtSDT, edtGmail, edtMatKhau;
    Spinner spinnerChucVu;
    DBQuanLyBug database;
    CardView btnCreateAccount;
    String[] chucVu = {"Quản lý", "Dev", "Tester"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        database = new DBQuanLyBug();
        getWidget();
        addAction();
    }

    private void getWidget() {
        imgBackProfile = findViewById(R.id.imgBackProfile);
        edtHoTen = findViewById(R.id.edtHoTen);
        spinnerChucVu = findViewById(R.id.spinnerChucVu);
        edtSDT = findViewById(R.id.edtSDT);
        edtGmail = findViewById(R.id.edtGmail);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, chucVu);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChucVu.setAdapter(adapter);
    }

    private void addAction() {
        imgBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getUserInfor(new DBQuanLyBug.UserCallback() {
                    @Override
                    public void onUserLoaded(User a) {
                        if (a.getMaNhanVien().startsWith("QL")){
                            User user = getUser();
                            String chucVu = user.getChucVu();
                            String maNhanVien = "";
                            database.getUsers(new DBQuanLyBug.UCallBack() {
                                @Override
                                public void onULoaded(List<User> users) {
                                    List<String> list = new ArrayList<>();
                                    for (User user : users) {
                                        if (user.getChucVu().equals(chucVu)) {
                                            list.add(user.getMaNhanVien());
                                        }
                                    }
                                    if (chucVu.equals("Quản lý")){
                                        int id = list.size() + 1;
                                        String maNhanVien = "QL" + String.format("%03d", id);
                                        user.setMaNhanVien(maNhanVien);
                                        database.createAccount(user);
                                    } else if (chucVu.equals("Dev")) {
                                        int id = list.size() + 1;
                                        String maNhanVien = "DEV" + String.format("%03d", id);
                                        user.setMaNhanVien(maNhanVien);
                                        database.createAccount(user);
                                    } else if (chucVu.equals("Tester")) {
                                        int id = list.size() + 1;
                                        String maNhanVien = "TEST" + String.format("%03d", id);
                                        user.setMaNhanVien(maNhanVien);
                                        database.createAccount(user);
                                    }


                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });

                            Toast.makeText(CreateAccountActivity.this, "Tạo tài khoản thành công, cần đăng nhập lại để reset ", Toast.LENGTH_SHORT).show();

                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(CreateAccountActivity.this, "Bạn không có quyền ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
    private User getUser(){
        String uid = "temp";
        String hoTen = edtHoTen.getText().toString();
        String[] parts = hoTen.split("\\s+");
        String ten = parts[parts.length - 1];

        String chucVu = spinnerChucVu.getSelectedItem().toString();
        String soDienThoai = edtSDT.getText().toString();
        String gmail = edtGmail.getText().toString();
        String matKhau = edtMatKhau.getText().toString();

        return new User(uid, "", ten, hoTen, chucVu, soDienThoai, gmail, matKhau);
    }
}