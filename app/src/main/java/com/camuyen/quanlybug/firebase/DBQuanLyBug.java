package com.camuyen.quanlybug.firebase;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.camuyen.quanlybug.model.User;
import com.google.android.gms.common.moduleinstall.internal.ApiFeatureRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class DBQuanLyBug {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    public void getUserInfor(UserCallback callback) {
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        String uid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        usersRef.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String hoTen = documentSnapshot.getString("hoTen");
                    String maNhanVien = documentSnapshot.getString("maNhanVien");
                    String chucVu = documentSnapshot.getString("chucVu");
                    String soDienThoai = documentSnapshot.getString("soDienThoai");
                    String gmail = documentSnapshot.getString("gmail");
                    String matKhau = documentSnapshot.getString("matKhau");
                    User user = new User(maNhanVien, hoTen, chucVu, soDienThoai, gmail, matKhau);
                    callback.onUserLoaded(user);
                } else {
                    Log.d("Lỗi DB", "Không tìm thấy thông tin người dùng cho UID: " + uid);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Lỗi DB", "Lỗi khi truy vấn dữ liệu từ Firestore: ", e);
            }
        });
    }

    public interface UserCallback {
        void onUserLoaded(User user);
    }
    public void setImageProfile(ImageView imgProfile){
        String userId = auth.getCurrentUser().getUid();
        String path = "Images/" + userId + ".jpg";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(path);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imgProfile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
