package com.camuyen.quanlybug.firebase;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.camuyen.quanlybug.model.Bugs;
import com.camuyen.quanlybug.model.Comments;
import com.camuyen.quanlybug.model.Devices;
import com.camuyen.quanlybug.model.Jobs;
import com.camuyen.quanlybug.model.Project;
import com.camuyen.quanlybug.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBQuanLyBug {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    public Date convertToDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String convertToString(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.format(date);
        } else {
            return null;
        }
    }
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
                    String uid = documentSnapshot.getString("uid");
                    String hoTen = documentSnapshot.getString("hoTen");
                    String maNhanVien = documentSnapshot.getString("maNhanVien");
                    String ten = documentSnapshot.getString("ten");
                    String chucVu = documentSnapshot.getString("chucVu");
                    String soDienThoai = documentSnapshot.getString("soDienThoai");
                    String gmail = documentSnapshot.getString("gmail");
                    String matKhau = documentSnapshot.getString("matKhau");
                    User user = new User(uid, maNhanVien, ten, hoTen, chucVu, soDienThoai, gmail, matKhau);
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
    public void setImagePeopleProfile(String userId, ImageView imgProfile){
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
                        String moTa = document.getString("moTa");
                        String ngayBatDau = document.getString("ngayBatDau");
                        String tenQuanLy = document.getString("tenQuanLy");
                        String trangThai = document.getString("trangThai");
                        Project project = new Project(maDuAn, tenQuanLy, tenDuAn, moTa, convertToDate(ngayBatDau), trangThai);
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

    public void getUsers(UCallBack callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference issuesRef = db.collection("users");
        issuesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String uid = document.getString("uid");
                        String hoTen = document.getString("hoTen");
                        String maNhanVien = document.getString("maNhanVien");
                        String ten = document.getString("ten");
                        String chucVu = document.getString("chucVu");
                        String soDienThoai = document.getString("soDienThoai");
                        String gmail = document.getString("gmail");
                        String matKhau = document.getString("matKhau");
                        User user = new User(uid, maNhanVien, ten, hoTen, chucVu, soDienThoai, gmail, matKhau);
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
    public void addNewProject(String documentId, Project project) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference projectsCollectionRef = db.collection("projects");

        // Tạo một tài liệu mới với ID được chỉ định
        Map<String, Object> projectData = new HashMap<>();
        projectData.put("maDuAn", project.getMaDuAn());
        projectData.put("tenQuanLy", project.getTenQuanLy());
        projectData.put("tenDuAn", project.getTenDuAn());
        projectData.put("moTa", project.getMoTa());
        projectData.put("ngayBatDau", convertToString(project.getNgayBatDau()));
        projectData.put("trangThai", project.getTrangThai());

        // Thêm dữ liệu vào Firestore với ID được chỉ định
        DocumentReference documentReference = projectsCollectionRef.document(documentId);
        documentReference.set(projectData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Thành công
                        Log.d("DB", "Document added with ID: " + documentId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Thất bại
                        Log.w("DB", "Error adding document", e);
                    }
                });
    }
    public void addNewBug(String documentId, Bugs bug) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference projectsCollectionRef = db.collection("bugs");

        // Tạo một tài liệu mới với ID được chỉ định
        Map<String, Object> bugData = new HashMap<>();
        bugData.put("maBug", bug.getMaBug());
        bugData.put("tenBug", bug.getTenBug());
        bugData.put("moTaLoi", bug.getMoTaLoi());
        bugData.put("anh", bug.getAnh());
        bugData.put("cacBuoc", bug.getCacBuoc());
        bugData.put("ketQuaMongMuon", bug.getKetQuaMongMuon());
        bugData.put("deadline", convertToString(bug.getDeadline()));
        bugData.put("trangThai", bug.getTrangThai());
        bugData.put("devFix", bug.getDevFix());
        bugData.put("mucDoNghiemTrong", bug.getMucDoNghiemTrong());
        bugData.put("maVanDe", bug.getMaVanDe());
        bugData.put("maDuAn", bug.getMaDuAn());
        bugData.put("maNhanVien", bug.getMaNhanVien());
        bugData.put("ngayXuatHien", convertToString(bug.getNgayXuatHien()));


        // Thêm dữ liệu vào Firestore với ID được chỉ định
        DocumentReference documentReference = projectsCollectionRef.document(documentId);
        documentReference.set(bugData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Thành công
                        Log.d("DB", "Document added with ID: " + documentId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Thất bại
                        Log.w("DB", "Error adding document", e);
                    }
                });
    }
    public void updateProject(String documentID, Project project){
        // Khởi tạo đối tượng Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("projects").document(documentID);

        // Tạo một tài liệu mới với ID được chỉ định
        Map<String, Object> projectData = new HashMap<>();
        projectData.put("maDuAn", project.getMaDuAn());
        projectData.put("tenQuanLy", project.getTenQuanLy());
        projectData.put("tenDuAn", project.getTenDuAn());
        projectData.put("moTa", project.getMoTa());
        projectData.put("ngayBatDau", convertToString(project.getNgayBatDau()));
        projectData.put("trangThai", project.getTrangThai());

        // Sử dụng phương thức update() để cập nhật tài liệu
        docRef.update(projectData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Cập nhật thành công
                        } else {
                            // Xảy ra lỗi khi cập nhật
                        }
                    }
                });
    }
    public void updateBug(String documentId, Bugs bug) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("bugs").document(documentId);

        // Tạo một tài liệu mới với ID được chỉ định
        Map<String, Object> bugData = new HashMap<>();
        bugData.put("maBug", bug.getMaBug());
        bugData.put("tenBug", bug.getTenBug());
        bugData.put("moTaLoi", bug.getMoTaLoi());
        bugData.put("anh", bug.getAnh());
        bugData.put("cacBuoc", bug.getCacBuoc());
        bugData.put("ketQuaMongMuon", bug.getKetQuaMongMuon());
        bugData.put("deadline", convertToString(bug.getDeadline()));
        bugData.put("trangThai", bug.getTrangThai());
        System.out.println("Test" + bug.getTrangThai());
        bugData.put("devFix", bug.getDevFix());
        bugData.put("mucDoNghiemTrong", bug.getMucDoNghiemTrong());
        bugData.put("maVanDe", bug.getMaVanDe());
        bugData.put("maDuAn", bug.getMaDuAn());
        bugData.put("maNhanVien", bug.getMaNhanVien());
        bugData.put("ngayXuatHien", convertToString(bug.getNgayXuatHien()));

        docRef.update(bugData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("Thành công");
                } else {
                    System.out.println("Thất bại");
                }
            }
        });

    }
    public void deleteDocument(Context context, String nameFolder ,String id) {
        // Tham chiếu đến tài liệu bạn muốn xóa
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(nameFolder).document(id);
        // Xóa tài liệu
        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Xóa thành công
                Toast.makeText(context, "Tài liệu đã được xóa", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xảy ra lỗi khi xóa
                Toast.makeText(context, "Không thể xóa tài liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getJobsInfo(JobsCallBack callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("jobs");
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Jobs> jobs = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String maVanDe = document.getString("maVanDe");
                        String maNhanVien = document.getString("maNhanVien");
                        String maCongViec = document.getString("maCongViec");
                        Jobs job = new Jobs(maCongViec, maNhanVien, maVanDe);
                        jobs.add(job);

                    }
                    callback.onIssuesLoaded(jobs);
                } else {
                    callback.onError(task.getException());
                }
            }

        });
    }
    public interface JobsCallBack {
        void onIssuesLoaded(List<Jobs> jobs);
        void onError(Exception e);
    }
    public void getBugsInfo(BugsCallBack callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("bugs");
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Bugs> bugs = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String maBug = document.getString("maBug");
                        String tenBug = document.getString("tenBug");
                        String moTaLoi = document.getString("moTaLoi");
                        String anh = document.getString("anh");
                        String cacBuoc = document.getString("cacBuoc");
                        String ketQuaMongMuon = document.getString("ketQuaMongMuon");
                        String ngayXuatHien = document.getString("ngayXuatHien");
                        String trangThai = document.getString("trangThai");
                        String devFix = document.getString("devFix");
                        String mucDoNghiemTrong = document.getString("mucDoNghiemTrong");
                        String maVanDe = document.getString("maVanDe");
                        String maDuAn = document.getString("maDuAn");
                        String maNhanVien = document.getString("maNhanVien");
                        String deadline = document.getString("deadline");



                        Bugs bug = new Bugs(maBug, tenBug, moTaLoi, anh, cacBuoc, ketQuaMongMuon, convertToDate(ngayXuatHien), trangThai, devFix, mucDoNghiemTrong, maVanDe, maDuAn, maNhanVien, convertToDate(deadline));
                        bugs.add(bug);

                    }
                    callback.onBugsLoaded(bugs);
                } else {
                    callback.onError(task.getException());
                }
            }

        });
    }
    public interface BugsCallBack {
        void onBugsLoaded(List<Bugs> bugs);
        void onError(Exception e);
    }
    public void getDevicesID(String userToken, String maNhanVien){
        // Get a reference to the Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new document in the "devices" collection with the user's ID as the document ID
        String userId = auth.getCurrentUser().getUid(); // Get the user's ID
        String gmail = auth.getCurrentUser().getEmail();
        DocumentReference docRef = db.collection("devices").document(userId);

        // Create a Map object to hold the data
        Map<String, Object> data = new HashMap<>();
        data.put("token", userToken); // Add the user's FCM token to the map
        data.put("gmail", gmail); // Add the user's email to the map
        data.put("maNhanVien", maNhanVien);
        // Set the data to the document in Firestore
        docRef.set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document successfully written
                        Log.d("Lỗi DB", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                        Log.w("Lỗi DB", "Error writing document", e);
                    }
                });

    }
    public void getDevices(DeviceCallBack callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("devices");
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Devices> devices = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String gmail = document.getString("gmail");
                        String token = document.getString("token");
                        String maNhanVien = document.getString("maNhanVien");

                        Devices device = new Devices(gmail, token, maNhanVien);
                        devices.add(device);

                    }
                    callback.onBugsLoaded(devices);
                } else {
                    callback.onError(task.getException());
                }
            }

        });
    }
    public interface DeviceCallBack {
        void onBugsLoaded(List<Devices> devices);
        void onError(Exception e);
    }
    public void getComments(CommentCallBack callback, String maBug) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("bugs").document(maBug).collection("comments");
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Comments> comments = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String maNhanVien = document.getString("maNhanVien");
                        String anhDaiDien = document.getString("anhDaiDien");
                        String noiDung = document.getString("noiDung");

                        Comments comment = new Comments(maNhanVien, noiDung, anhDaiDien);
                        comments.add(comment);

                    }
                    callback.onBugsLoaded(comments);
                } else {
                    callback.onError(task.getException());
                }
            }

        });
    }
    public interface CommentCallBack {
        void onBugsLoaded(List<Comments> comments);
        void onError(Exception e);
    }
    public String convertMa(int number) {
        return String.format("%03d", number);
    }
    public void addNewComment(String maBug, String maComment, Comments comment) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentCollectionRefs = db.collection("bugs").document(maBug).collection("comments");

        // Tạo một tài liệu mới với ID được chỉ định
        Map<String, Object> commentData = new HashMap<>();
        commentData.put("maNhanVien", comment.getMaNhanVien());
        commentData.put("anhDaiDien", comment.getAnhDaiDien());
        commentData.put("noiDung", comment.getNoiDung());

        // Thêm dữ liệu vào Firestore với ID được chỉ định
        DocumentReference documentReference = commentCollectionRefs.document(maComment);
        documentReference.set(commentData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Thành công
                        Log.d("DB", "Document added with ID: " + maComment);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Thất bại
                        Log.w("DB", "Error adding document", e);
                    }
                });
    }
    public void getBugFiler(BugFilerCallBack callback, String trangThai){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("bugs");

        // Lấy dữ liệu có thuộc tính "your_property" có giá trị bằng "desired_value"
        collectionRef.whereEqualTo("trangThai", trangThai)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Bugs> bugs = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                String maBug = document.getString("maBug");
                                String tenBug = document.getString("tenBug");
                                String moTaLoi = document.getString("moTaLoi");
                                String anh = document.getString("anh");
                                String cacBuoc = document.getString("cacBuoc");
                                String ketQuaMongMuon = document.getString("ketQuaMongMuon");
                                String ngayXuatHien = document.getString("ngayXuatHien");
                                String trangThai = document.getString("trangThai");
                                String devFix = document.getString("devFix");
                                String mucDoNghiemTrong = document.getString("mucDoNghiemTrong");
                                String maVanDe = document.getString("maVanDe");
                                String maDuAn = document.getString("maDuAn");
                                String maNhanVien = document.getString("maNhanVien");
                                String deadline = document.getString("deadline");



                                Bugs bug = new Bugs(maBug, tenBug, moTaLoi, anh, cacBuoc, ketQuaMongMuon, convertToDate(ngayXuatHien), trangThai, devFix, mucDoNghiemTrong, maVanDe, maDuAn, maNhanVien, convertToDate(deadline));
                                bugs.add(bug);
                            }
                            callback.onBugsFilterLoaded(bugs);
                        } else {
                            Log.d("Lỗi DB", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public interface BugFilerCallBack {
        void onBugsFilterLoaded(List<Bugs> bugs);
    }
}
