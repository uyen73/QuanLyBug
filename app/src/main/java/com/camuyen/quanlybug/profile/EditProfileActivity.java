package com.camuyen.quanlybug.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.User;

public class EditProfileActivity extends AppCompatActivity {
    CardView btnUpdateProfile;
    EditText edtHoTen, edtChucVu, edtSDT, edtEmail;
    DBQuanLyBug database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        database = new DBQuanLyBug();
        getWidget();
        addAction();

    }
    private void getWidget() {
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        edtHoTen = findViewById(R.id.edtHoTen);
        edtChucVu = findViewById(R.id.edtChucVu);
        edtSDT = findViewById(R.id.edtSDT);
        edtEmail = findViewById(R.id.edtEmail);

        database.getUserInfor(new DBQuanLyBug.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                edtHoTen.setText(user.getHoTen());
                edtChucVu.setText(user.getChucVu());
                edtSDT.setText(user.getSoDienThoai());
                edtEmail.setText(user.getGmail());
            }
        });
    }
    private void addAction() {
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBlank()) {
                    database.getUserInfor(new DBQuanLyBug.UserCallback() {
                        @Override
                        public void onUserLoaded(User user) {
                            user.setHoTen(edtHoTen.getText().toString());
                            user.setChucVu(edtChucVu.getText().toString());
                            user.setSoDienThoai(edtSDT.getText().toString());
                            user.setGmail(edtEmail.getText().toString());

                            String hoTen = edtHoTen.getText().toString();
                            String[] parts = hoTen.split("\\s+");
                            String ten = parts[parts.length - 1];
                            user.setTen(ten);

                            database.updateUser(user);
                        }
                    });
                }
                finish();
            }
        });



    }
    public boolean checkBlank() {
        if (edtHoTen.getText().toString().isEmpty() || edtChucVu.getText().toString().isEmpty() || edtSDT.getText().toString().isEmpty() || edtEmail.getText().toString().isEmpty()) {
            return false;
        }
        else return true;
    }



}