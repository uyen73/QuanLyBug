package com.camuyen.quanlybug.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.adapter.CommentAdapter;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.BugStatus;
import com.camuyen.quanlybug.model.Bugs;
import com.camuyen.quanlybug.model.Comments;
import com.camuyen.quanlybug.model.Devices;
import com.camuyen.quanlybug.model.User;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetailBugActivity extends AppCompatActivity {
    RecyclerView recycleViewComment;
    TextView txtMoTaBug, txtChiTietMoTaBug, txtAssignment, txtTrangThai, text;
    ImageView imgBackDetailBug, imgSendComment;
    String maBug = "";
    DBQuanLyBug database;
    CommentAdapter commentAdapter;
    EditText edtComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bug);
        Intent intent = getIntent();
        maBug = intent.getStringExtra("maBug");
        database = new DBQuanLyBug();
        getWidget();
        addAction();
    }

    private void getWidget() {
        txtMoTaBug = findViewById(R.id.txtMoTaBug);
        txtChiTietMoTaBug = findViewById(R.id.txtChiTietMoTaBug);
        txtAssignment = findViewById(R.id.txtAssignment);
        txtTrangThai = findViewById(R.id.txtTrangThai);
        imgBackDetailBug = findViewById(R.id.imgBackDetailBug);
        recycleViewComment = findViewById(R.id.recycleViewComment);
        edtComment = findViewById(R.id.edtComment);
        imgSendComment = findViewById(R.id.imgSendComment);

        text = findViewById(R.id.text);
        database.getBugsInfo(new DBQuanLyBug.BugsCallBack() {
            @Override
            public void onBugsLoaded(List<Bugs> bugs) {
                Bugs bug = new Bugs();
                for (Bugs a : bugs) {
                    if (a.getMaBug().equals(maBug)) {
                        bug = a;
                        break;
                    }
                }
                txtChiTietMoTaBug.setText(bug.getMoTaLoi());
                txtAssignment.setText(bug.getDevFix());
                txtTrangThai.setText(bug.getTrangThai());

                BugStatus bugStatus = new BugStatus();
                List<BugStatus> bugStatusList = bugStatus.getBugStatusList();
                for (BugStatus a : bugStatusList) {
                    if (a.getStatus().equals(bug.getTrangThai())) {
                        txtTrangThai.setTextColor(Color.parseColor(a.getText()));
                        txtTrangThai.setBackgroundColor(Color.parseColor(a.getBackground()));
                        break;
                    }
                }

            }

            @Override
            public void onError(Exception e) {

            }
        });
        database.getComments(new DBQuanLyBug.CommentCallBack() {
            @Override
            public void onBugsLoaded(List<Comments> comments) {
                if (comments.size() == 0) {
                    text.setText("Comment: Chưa có comment nào");
                }
                commentAdapter = new CommentAdapter(comments);
                recycleViewComment.setLayoutManager(new LinearLayoutManager(DetailBugActivity.this));
                recycleViewComment.setAdapter(commentAdapter);
            }

            @Override
            public void onError(Exception e) {

            }
        }, maBug);
        txtMoTaBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDescription();
            }
        });
        imgBackDetailBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });


    }

    private void addAction() {
        imgSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noiDung = edtComment.getText().toString();

                if (!noiDung.isEmpty()) {
                    database.getComments(new DBQuanLyBug.CommentCallBack() {
                        @Override
                        public void onBugsLoaded(List<Comments> comments) {
                            int size = comments.size() + 1;
                            String maComment = "CMT" + database.convertMa(size);
                            database.getUserInfor(new DBQuanLyBug.UserCallback() {
                                @Override
                                public void onUserLoaded(User user) {
                                    String anhDaiDien = "Ảnh đại diện";
                                    database.addNewComment(maBug, maComment, new Comments(user.getMaNhanVien(), noiDung, anhDaiDien));
                                    resetData();
                                    database.getBugsInfo(new DBQuanLyBug.BugsCallBack() {
                                        @Override
                                        public void onBugsLoaded(List<Bugs> bugs) {
                                            for (Bugs a : bugs) {
                                                if (a.getMaBug().equals(maBug)) {
                                                    sendNotification(a.getMaNhanVien());
                                                }
                                            }

                                        }

                                        @Override
                                        public void onError(Exception e) {

                                        }
                                    });

                                }
                            });
                            edtComment.setText("");

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    }, maBug);

                }
            }
        });
    }

    private void resetData() {
        database.getComments(new DBQuanLyBug.CommentCallBack() {
            @Override
            public void onBugsLoaded(List<Comments> comments) {
                if (comments.size() == 0) {
                    text.setText("Comment: Chưa có comment nào");
                }
                commentAdapter = new CommentAdapter(comments);
                recycleViewComment.setLayoutManager(new LinearLayoutManager(DetailBugActivity.this));
                recycleViewComment.setAdapter(commentAdapter);
            }

            @Override
            public void onError(Exception e) {

            }
        }, maBug);
    }

    private void toggleDescription() {
        if (txtChiTietMoTaBug.getVisibility() == View.VISIBLE) {
            animateViewOut(txtChiTietMoTaBug);
        } else {
            animateViewIn(txtChiTietMoTaBug);
        }
    }

    private void animateViewIn(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animation = new TranslateAnimation(0, 0, -view.getHeight(), 0);
        animation.setDuration(300);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        view.startAnimation(animation);
    }

    private void animateViewOut(final View view) {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -view.getHeight());
        animation.setDuration(300);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(animation);
    }
    private void sendNotification(String ngNhan) {
        database.getUserInfor(new DBQuanLyBug.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                try {
                    database.getDevices(new DBQuanLyBug.DeviceCallBack() {
                        @Override
                        public void onBugsLoaded(List<Devices> devices) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                JSONObject notificationObj = new JSONObject();
                                notificationObj.put("title", "Có comment mới tại bug bạn đang fix.");
                                notificationObj.put("body",  user.getTen() + " vừa thêm 1 comment.");

                                jsonObject.put("notification", notificationObj);
                                for (Devices a : devices){
                                    if (a.getMaNhanVien().equals(ngNhan)){
                                        jsonObject.put("to", a.getToken());
                                        break;
                                    }
                                }
                                callAPI(jsonObject);
                            }catch (Exception e){

                            }

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });

                }catch (Exception e){

                }


            }
        });

    }
    public void callAPI(JSONObject jsonObject) {
        MediaType JSON = MediaType.get("application/json");

        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer AAAAgaYXjzo:APA91bEWIBDwGNGMMOCIuP9WpT_OxQ3czOIOvbcn-54BlXVDZ-SkcHgophq_k35fTxaIRm3tTouqDezTAozynV9qa2qB_S7FtpO6uyd3S6f0mDAm8UhRKuDkK2KDLu7Y-yLM4OYDvk10")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }
}