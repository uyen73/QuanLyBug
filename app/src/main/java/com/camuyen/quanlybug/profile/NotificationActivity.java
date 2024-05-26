package com.camuyen.quanlybug.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.adapter.NotificationAdapter;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.Devices;
import com.camuyen.quanlybug.model.NotificationItem;
import com.camuyen.quanlybug.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = "NotificationActivity";
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    DBQuanLyBug database;
    ImageView imgBackNotification, imgMenuNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        database = new DBQuanLyBug();
        recyclerView = findViewById(R.id.recycleViewNotification);
        imgMenuNotification = findViewById(R.id.imgMenuNotification);
        imgBackNotification = findViewById(R.id.imgBackNotification);
        updateList();
        imgBackNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        imgMenuNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_notification, popupMenu.getMenu());

        // Định nghĩa sự kiện khi một mục menu được chọn
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.mnuDanhDauDaDoc) {
                    database.getNotifications(new DBQuanLyBug.NotificationCallback() {
                        @Override
                        public void onNotificationLoaded(List<NotificationItem> notification) {
                            adapter.updateNotificationReadStatus(notification);
                            updateList();
                        }
                    });
                    return true;
                } else if (itemId == R.id.mnuXoaTatCa) {
                    adapter.deleteAllNotification();

                    return true;
                }
                // Xử lý các menu item khác nếu cần
                return false;
            }
        });

        // Hiển thị Popup Menu
        popupMenu.show();
    }

    private void updateList(){
        database.getNotifications(new DBQuanLyBug.NotificationCallback() {
            @Override
            public void onNotificationLoaded(List<NotificationItem> notificationList) {
                database.getUserInfor(new DBQuanLyBug.UserCallback() {
                    @Override
                    public void onUserLoaded(User user) {
                        database.getDevices(new DBQuanLyBug.DeviceCallBack() {
                            @Override
                            public void onDeviceLoaded(List<Devices> devices) {
                                String token = "";
                                for (Devices device : devices) {
                                    if (device.getMaNhanVien().equals(user.getMaNhanVien())) {
                                        token = device.getToken();
                                        break;
                                    }
                                }
                                List<NotificationItem> list = new ArrayList<>();
                                for (NotificationItem item : notificationList) {
                                    if (item.getToken().equals(token)) {
                                        list.add(item);
                                    }
                                }
                                recyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
                                adapter = new NotificationAdapter(list, NotificationActivity.this);
                                recyclerView.setAdapter(adapter);

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });

                    }
                });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }
}