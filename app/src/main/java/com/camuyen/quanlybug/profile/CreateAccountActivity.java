package com.camuyen.quanlybug.profile;

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

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.login.LoginActivity;
import com.camuyen.quanlybug.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {
    ImageView imgBackProfile;
    EditText edtHoTen, edtChucVu, edtSDT, edtGmail, edtMatKhau, edtMaNhanVien;
    DBQuanLyBug database;
    CardView btnCreateAccount;
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
        edtChucVu = findViewById(R.id.edtChucVu);
        edtSDT = findViewById(R.id.edtSDT);
        edtGmail = findViewById(R.id.edtGmail);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtMaNhanVien = findViewById(R.id.edtMaNhanVien);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
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
                            database.createAccount(user);
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
        String maNhanVien = edtMaNhanVien.getText().toString();

        String hoTen = edtHoTen.getText().toString();
        String[] parts = hoTen.split("\\s+");
        String ten = parts[parts.length - 1];

        String chucVu = edtChucVu.getText().toString();
        String soDienThoai = edtSDT.getText().toString();
        String gmail = edtGmail.getText().toString();
        String matKhau = edtMatKhau.getText().toString();
        return new User(uid, maNhanVien, ten, hoTen, chucVu, soDienThoai, gmail, matKhau);
    }
}