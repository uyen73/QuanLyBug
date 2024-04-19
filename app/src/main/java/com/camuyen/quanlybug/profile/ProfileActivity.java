package com.camuyen.quanlybug.profile;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    ImageView imgBackProfile, imgProfile;
    TextView txtEmailAndPhoneNumber, txtName;
    FirebaseAuth auth;
    DBQuanLyBug database;
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

        auth = FirebaseAuth.getInstance();
        database = new DBQuanLyBug();

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
    }
}