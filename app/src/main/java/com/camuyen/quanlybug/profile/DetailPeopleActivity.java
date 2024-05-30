package com.camuyen.quanlybug.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.adapter.BugAdapter;
import com.camuyen.quanlybug.adapter.ProjectAdapter;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.Bugs;
import com.camuyen.quanlybug.model.Project;
import com.camuyen.quanlybug.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailPeopleActivity extends AppCompatActivity {
    RecyclerView recycleviewPeople;
    BugAdapter adapter;
    ProjectAdapter projectAdapter;
    ImageView imgBackProfile, imgProfile, imgEditImageProfile, imgFilterBugPeople;
    TextView txtEmailAndPhoneNumber, txtName;
    FirebaseAuth auth;
    DBQuanLyBug database;
    Spinner spnProjectPeople;

    String maNV = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_people);
        Intent intent = getIntent();
        maNV = intent.getStringExtra("maNhanVien");
        getWidget();
        addAction();
    }
    private void getWidget() {
        imgBackProfile = findViewById(R.id.imgBackProfile);
        imgProfile = findViewById(R.id.imgProfile);
        txtEmailAndPhoneNumber = findViewById(R.id.txtEmailAndPhoneNumber);
        txtName = findViewById(R.id.txtName);
        imgEditImageProfile = findViewById(R.id.imgEditImageProfile);
        recycleviewPeople = findViewById(R.id.recycleviewPeople);
        imgFilterBugPeople = findViewById(R.id.imgFilterBugPeople);
        spnProjectPeople = findViewById(R.id.spnProjectPeople);
        auth = FirebaseAuth.getInstance();
        database = new DBQuanLyBug();
        database.getUserInfor(new DBQuanLyBug.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                if (maNV.startsWith("TEST") || maNV.startsWith("DEV")){
                    imgFilterBugPeople.setVisibility(View.VISIBLE);
                    spnProjectPeople.setVisibility(View.VISIBLE);
                }
            }
        });
        database.getProjectsInfo(new DBQuanLyBug.ProjectsCallBack() {
            @Override
            public void onProjectsLoaded(List<Project> projects) {
                ArrayAdapter<Project> spnAdapter = new ArrayAdapter<>(DetailPeopleActivity.this, android.R.layout.simple_spinner_item, projects);
                spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnProjectPeople.setAdapter(spnAdapter);
            }

            @Override
            public void onError(Exception e) {

            }
        });




    }


    private void addAction(){
        imgBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        database.getUsers(new DBQuanLyBug.UCallBack() {
            @Override
            public void onULoaded(List<User> users) {
                User a = new User();
                for (User user : users){
                    if (user.getMaNhanVien().equals(maNV)){
                        a = user;
                        break;
                    }
                }
                String emailandphone = a.getGmail() + " | " + a.getSoDienThoai();
                txtEmailAndPhoneNumber.setText(emailandphone);
                String name = a.getHoTen();
                txtName.setText(name);

                String path = "Images/" + a.getUid() + ".jpg";
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(path);
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imgProfile);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        imgProfile.setImageResource(R.mipmap.ic_launcher);
                    }
                });

            }

            @Override
            public void onError(Exception e) {

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
        if (maNV.startsWith("QL")){
            imgFilterBugPeople.setVisibility(View.GONE);
            database.getProjectsInfo(new DBQuanLyBug.ProjectsCallBack() {
                @Override
                public void onProjectsLoaded(List<Project> projects) {
                    List<Project> projectList = new ArrayList<>();
                    for (Project project : projects){
                        if (project.getMaQuanLy().equals(maNV)){
                            projectList.add(project);
                        }
                    }
                    projectAdapter = new ProjectAdapter(projectList, DetailPeopleActivity.this);
                    recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                    recycleviewPeople.setAdapter(projectAdapter);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }else {
            database.getBugsInfo(new DBQuanLyBug.BugsCallBack() {
                @Override
                public void onBugsLoaded(List<Bugs> bugs) {
                    Project project = (Project) spnProjectPeople.getSelectedItem();
                    if (maNV.startsWith("TEST")){
                        List<Bugs> bugsList = new ArrayList<>();
                        for (Bugs a : bugs){
                            if (a.getMaQuanLy().equals(maNV) && project.getMaDuAn().equals(a.getMaDuAn())){
                                bugsList.add(a);
                            }
                        }
                        adapter = new BugAdapter(bugsList,DetailPeopleActivity.this);
                        recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                        recycleviewPeople.setAdapter(adapter);
                    } else if (maNV.startsWith("DEV")) {
                        List<Bugs> bugsList = new ArrayList<>();
                        for (Bugs a : bugs){
                            if (a.getMaNhanVien().equals(maNV) && project.getMaDuAn().equals(a.getMaDuAn())){
                                bugsList.add(a);
                            }
                        }
                        adapter = new BugAdapter(bugsList,DetailPeopleActivity.this);
                        recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                        recycleviewPeople.setAdapter(adapter);
                    }
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }

        imgFilterBugPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, maNV);
            }
        });
        spnProjectPeople.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Project project = (Project) parent.getItemAtPosition(position);
                String maDuAn = project.getMaDuAn();
                database.getBugsInfo(new DBQuanLyBug.BugsCallBack() {
                    @Override
                    public void onBugsLoaded(List<Bugs> bugs) {
                        if (maNV.startsWith("DEV")){
                            List<Bugs> listSPN = new ArrayList<>();
                            for (Bugs a : bugs){
                                if (a.getMaDuAn().equals(maDuAn) && a.getMaNhanVien().equals(maNV)){
                                    listSPN.add(a);
                                }
                            }
                            adapter = new BugAdapter(listSPN, DetailPeopleActivity.this);
                            recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                            recycleviewPeople.setAdapter(adapter);
                        } else if (maNV.startsWith("TEST")){
                            List<Bugs> listSPN = new ArrayList<>();
                            for (Bugs a : bugs){
                                if (a.getMaDuAn().equals(maDuAn) && a.getMaQuanLy().equals(maNV)){
                                    listSPN.add(a);
                                }
                            }
                            adapter = new BugAdapter(listSPN, DetailPeopleActivity.this);
                            recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                            recycleviewPeople.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                        database.getUsers(new DBQuanLyBug.UCallBack() {
                            @Override
                            public void onULoaded(List<User> users) {
                                User a = new User();
                                for (User user : users){
                                    if (user.getMaNhanVien().equals(maNV)){
                                        a = user;
                                        break;
                                    }
                                }
                                // Tạo tên file cho ảnh dựa trên ID của người dùng
                                String userId = a.getUid();
                                String fileName = userId + ".jpg";

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
    private void showPopupMenu(View v, String maNV) {
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_people_filter, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.mnuNew){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            Project project = (Project) spnProjectPeople.getSelectedItem();
                            if (maNV.startsWith("TEST")){
                                List<Bugs> bugsList = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaQuanLy().equals(maNV) && project.getMaDuAn().equals(a.getMaDuAn())){
                                        bugsList.add(a);
                                    }
                                }
                                adapter = new BugAdapter(bugsList,DetailPeopleActivity.this);
                                recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                                recycleviewPeople.setAdapter(adapter);
                            } else if (maNV.startsWith("DEV")) {
                                List<Bugs> bugsList = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaNhanVien().equals(maNV) && project.getMaDuAn().equals(a.getMaDuAn())){
                                        bugsList.add(a);
                                    }
                                }
                                adapter = new BugAdapter(bugsList,DetailPeopleActivity.this);
                                recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                                recycleviewPeople.setAdapter(adapter);
                            }

                        }
                    }, "New");
                }
                if (item.getItemId() == R.id.mnuOpen){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            Project project = (Project) spnProjectPeople.getSelectedItem();
                            if (maNV.startsWith("TEST")){
                                List<Bugs> bugsList = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaQuanLy().equals(maNV) && project.getMaDuAn().equals(a.getMaDuAn())){
                                        bugsList.add(a);
                                    }
                                }
                                adapter = new BugAdapter(bugsList,DetailPeopleActivity.this);
                                recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                                recycleviewPeople.setAdapter(adapter);
                            } else if (maNV.startsWith("DEV")) {
                                List<Bugs> bugsList = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaNhanVien().equals(maNV) && project.getMaDuAn().equals(a.getMaDuAn())){
                                        bugsList.add(a);
                                    }
                                }
                                adapter = new BugAdapter(bugsList,DetailPeopleActivity.this);
                                recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                                recycleviewPeople.setAdapter(adapter);
                            }

                        }
                    }, "Open");
                }
                if (item.getItemId() == R.id.mnuFix){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            Project project = (Project) spnProjectPeople.getSelectedItem();
                            if (maNV.startsWith("TEST")){
                                List<Bugs> bugsList = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaQuanLy().equals(maNV) && project.getMaDuAn().equals(a.getMaDuAn())){
                                        bugsList.add(a);
                                    }
                                }
                                adapter = new BugAdapter(bugsList,DetailPeopleActivity.this);
                                recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                                recycleviewPeople.setAdapter(adapter);
                            } else if (maNV.startsWith("DEV")) {
                                List<Bugs> bugsList = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaNhanVien().equals(maNV) && project.getMaDuAn().equals(a.getMaDuAn())){
                                        bugsList.add(a);
                                    }
                                }
                                adapter = new BugAdapter(bugsList,DetailPeopleActivity.this);
                                recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                                recycleviewPeople.setAdapter(adapter);
                            }

                        }
                    }, "Fix");
                }
                if (item.getItemId() == R.id.mnuPending){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            Project project = (Project) spnProjectPeople.getSelectedItem();
                            if (maNV.startsWith("TEST")){
                                List<Bugs> bugsList = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaQuanLy().equals(maNV) && project.getMaDuAn().equals(a.getMaDuAn())){
                                        bugsList.add(a);
                                    }
                                }
                                adapter = new BugAdapter(bugsList,DetailPeopleActivity.this);
                                recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                                recycleviewPeople.setAdapter(adapter);
                            } else if (maNV.startsWith("DEV")) {
                                List<Bugs> bugsList = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaNhanVien().equals(maNV) && project.getMaDuAn().equals(a.getMaDuAn())){
                                        bugsList.add(a);
                                    }
                                }
                                adapter = new BugAdapter(bugsList,DetailPeopleActivity.this);
                                recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                                recycleviewPeople.setAdapter(adapter);
                            }

                        }
                    }, "Pending");
                }
                if (item.getItemId() == R.id.mnuReopen){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            Project project = (Project) spnProjectPeople.getSelectedItem();
                            if (maNV.startsWith("TEST")){
                                List<Bugs> bugsList = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaQuanLy().equals(maNV) && project.getMaDuAn().equals(a.getMaDuAn())){
                                        bugsList.add(a);
                                    }
                                }
                                adapter = new BugAdapter(bugsList,DetailPeopleActivity.this);
                                recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                                recycleviewPeople.setAdapter(adapter);
                            } else if (maNV.startsWith("DEV")) {
                                List<Bugs> bugsList = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaNhanVien().equals(maNV) && project.getMaDuAn().equals(a.getMaDuAn())){
                                        bugsList.add(a);
                                    }
                                }
                                adapter = new BugAdapter(bugsList,DetailPeopleActivity.this);
                                recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                                recycleviewPeople.setAdapter(adapter);
                            }

                        }
                    }, "Reopen");
                }
                if (item.getItemId() == R.id.mnuClose){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            Project project = (Project) spnProjectPeople.getSelectedItem();
                            if (maNV.startsWith("TEST")){
                                List<Bugs> bugsList = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaQuanLy().equals(maNV) && project.getMaDuAn().equals(a.getMaDuAn())){
                                        bugsList.add(a);
                                    }
                                }
                                adapter = new BugAdapter(bugsList,DetailPeopleActivity.this);
                                recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                                recycleviewPeople.setAdapter(adapter);
                            } else if (maNV.startsWith("DEV")) {
                                List<Bugs> bugsList = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaNhanVien().equals(maNV) && project.getMaDuAn().equals(a.getMaDuAn())){
                                        bugsList.add(a);
                                    }
                                }
                                adapter = new BugAdapter(bugsList,DetailPeopleActivity.this);
                                recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                                recycleviewPeople.setAdapter(adapter);
                            }

                        }
                    }, "Close");
                }
                if (item.getItemId() == R.id.mnuRejected){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            Project project = (Project) spnProjectPeople.getSelectedItem();
                            if (maNV.startsWith("TEST")){
                                List<Bugs> bugsList = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaQuanLy().equals(maNV) && project.getMaDuAn().equals(a.getMaDuAn())){
                                        bugsList.add(a);
                                    }
                                }
                                adapter = new BugAdapter(bugsList,DetailPeopleActivity.this);
                                recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                                recycleviewPeople.setAdapter(adapter);
                            } else if (maNV.startsWith("DEV")) {
                                List<Bugs> bugsList = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaNhanVien().equals(maNV) && project.getMaDuAn().equals(a.getMaDuAn())){
                                        bugsList.add(a);
                                    }
                                }
                                adapter = new BugAdapter(bugsList,DetailPeopleActivity.this);
                                recycleviewPeople.setLayoutManager(new LinearLayoutManager(DetailPeopleActivity.this));
                                recycleviewPeople.setAdapter(adapter);
                            }

                        }
                    }, "Rejected");
                }
                return true;
            }
        });

        popupMenu.show();
    }
}