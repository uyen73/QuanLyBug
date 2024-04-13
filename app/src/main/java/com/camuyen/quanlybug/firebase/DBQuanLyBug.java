package com.camuyen.quanlybug.firebase;

import android.util.Log;

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


import java.util.List;
import java.util.concurrent.ExecutionException;

public class DBQuanLyBug {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void getUserInfor(FirebaseAuth auth, UserCallback callback) {
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

}
