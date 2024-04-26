package com.camuyen.quanlybug.firebase;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.camuyen.quanlybug.model.Issue;
import com.camuyen.quanlybug.model.Project;
import com.camuyen.quanlybug.model.User;
import com.google.android.gms.common.moduleinstall.internal.ApiFeatureRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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


import java.util.ArrayList;
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
    public void getProjectsInfo(ProjectsCallBack callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference projectsRef = db.collection("projects");
        projectsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Project> projects = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String maDuAn = document.getString("maDuAn");
                        String tenDuAn = document.getString("tenDuAn");
                        String maNhanVien = document.getString("maNhanVien");
                        String moTa = document.getString("moTa");
                        String ngayBatDau = document.getString("ngayBatDau");
                        String tenQuanLy = document.getString("tenQuanLy");
                        Project project = new Project(maDuAn, maNhanVien, tenQuanLy, tenDuAn, moTa, ngayBatDau);
                        projects.add(project);
                    }
                    callback.onProjectsLoaded(projects);
                } else {
                    callback.onError(task.getException());
                }
            }
        });
    }
    public interface ProjectsCallBack {
        void onProjectsLoaded(List<Project> projects);
        void onError(Exception e);
    }

    public void getIssuesInfo(IssueCallBack callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference issuesRef = db.collection("issues");
        issuesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Issue> issues = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String maVanDe = document.getString("maVanDe");
                        String tenVanDe = document.getString("tenVanDe");
                        String ngayBatDau = document.getString("ngayBatDau");
                        String moTa = document.getString("moTa");
                        String dev = document.getString("dev");
                        String dueDateDev = document.getString("dueDateDev");
                        String test = document.getString("test");
                        String dueDateTest = document.getString("dueDateTest");
                        String tienDoDev = document.getString("tienDoDev");
                        String tienDoTest = document.getString("tienDoTest");
                        String maNhanVien = document.getString("maNhanVien");
                        String maDuAn = document.getString("maDuAn");

                        Issue issue = new Issue(maVanDe, tenVanDe, ngayBatDau, moTa, dev, dueDateDev, test, dueDateTest, tienDoDev, tienDoTest, maNhanVien, maDuAn);
                        issues.add(issue);

                    }
                    callback.onIssuesLoaded(issues);
                } else {
                    callback.onError(task.getException());
                }
            }

        });
    }
    public interface IssueCallBack {
        void onIssuesLoaded(List<Issue> issues);
        void onError(Exception e);
    }

    public void getUsers(UCallBack callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference issuesRef = db.collection("users");
        issuesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String hoTen = document.getString("hoTen");
                        String maNhanVien = document.getString("maNhanVien");
                        String chucVu = document.getString("chucVu");
                        String soDienThoai = document.getString("soDienThoai");
                        String gmail = document.getString("gmail");
                        String matKhau = document.getString("matKhau");
                        User user = new User(maNhanVien, hoTen, chucVu, soDienThoai, gmail, matKhau);
                        users.add(user);

                    }
                    callback.onULoaded(users);
                } else {
                    callback.onError(task.getException());
                }
            }

        });
    }
    public interface UCallBack {
        void onULoaded(List<User> users);
        void onError(Exception e);
    }







}
