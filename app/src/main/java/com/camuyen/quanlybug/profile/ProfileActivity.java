package com.camuyen.quanlybug.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    ImageView imgBackProfile, imgProfile, imgEditImageProfile;
    TextView txtEmailAndPhoneNumber, txtName;
    FirebaseAuth auth;
    DBQuanLyBug database;
    TextView txtEditProfileInformation, txtTaoTaiKhoanMoi, txtThongBao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWidget();
        addAction();
    }

    private void getWidget() {
        imgBackProfile = findViewById(R.id.imgBackProfile);
        imgProfile = findViewById(R.id.imgProfile);
        txtEmailAndPhoneNumber = findViewById(R.id.txtEmailAndPhoneNumber);
        txtName = findViewById(R.id.txtName);
        txtThongBao = findViewById(R.id.txtThongBao);
        imgEditImageProfile = findViewById(R.id.imgEditImageProfile);

        txtEditProfileInformation = findViewById(R.id.txtEditProfileInformation);
        txtTaoTaiKhoanMoi = findViewById(R.id.txtTaoTaiKhoanMoi);

        auth = FirebaseAuth.getInstance();
        database = new DBQuanLyBug();
        txtThongBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, NotificationActivity.class));
            }
        });
    }


    private void addAction(){
        imgBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        database.setImageProfile(imgProfile);
        database.getUserInfor(new DBQuanLyBug.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                String emailandphone = user.getGmail() + " | " + user.getSoDienThoai();
                txtEmailAndPhoneNumber.setText(emailandphone);
                String name = user.getHoTen();
                txtName.setText(name);
            }
        });

        imgEditImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở Intent để chọn ảnh từ thư viện
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageLauncher.launch(intent);
            }
        });
        txtEditProfileInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });
        txtTaoTaiKhoanMoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getUserInfor(new DBQuanLyBug.UserCallback() {
                    @Override
                    public void onUserLoaded(User user) {
                        if (user.getMaNhanVien().startsWith("QL")){
                            startActivity(new Intent(ProfileActivity.this, CreateAccountActivity.class));
                        }else {
                            Toast.makeText(ProfileActivity.this, "Bạn không có quyền tạo tài khoản", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                
            }
        });

    }
    ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // Lấy URI của ảnh từ Intent
                        Uri imageUri = result.getData().getData();

                        // Tạo tên file cho ảnh dựa trên ID của người dùng
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String fileName = userId + ".jpg";

                        // Thực hiện upload ảnh lên Firebase Storage
                        uploadImage(imageUri, fileName);
                    }
                }
            });

    private void uploadImage(Uri imageUri, String fileName) {
        // Tham chiếu đến thư mục trong Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Images/" + fileName);

        // Upload ảnh lên Firebase Storage
        UploadTask uploadTask = storageRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Ảnh đã được upload thành công, lấy URL của ảnh
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                // Hiển thị ảnh trong ImageView
                Picasso.get().load(imageUrl).into(imgProfile);
            });
        }).addOnFailureListener(exception -> {
            // Xảy ra lỗi khi upload ảnh
            Toast.makeText(this, "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}