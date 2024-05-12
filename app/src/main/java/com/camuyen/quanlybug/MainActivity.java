package com.camuyen.quanlybug;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.camuyen.quanlybug.adapter.ViewPagerAdapter;
import com.camuyen.quanlybug.fcm.MyApp;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.fragment.DetailProjectFragment;
import com.camuyen.quanlybug.login.LoginActivity;
import com.camuyen.quanlybug.model.User;
import com.camuyen.quanlybug.profile.ProfileActivity;
import com.camuyen.quanlybug.projects.AddBugActivity;
import com.camuyen.quanlybug.projects.AddProjectsActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    MeowBottomNavigation bottomNavigation;
    public ViewPager2 viewPager;
    public TextView txtName, txtTitle;
    ImageView imgToProfile, imgDrawer;
    DBQuanLyBug database;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWidget();
        setupViewPager();
        setupBottomNavigation();
        addAction();
        requestNotificationPermission();
        updateTokenUser();

    }

    private void updateTokenUser() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                database.getUserInfor(new DBQuanLyBug.UserCallback() {
                    @Override
                    public void onUserLoaded(User user) {
                        database.getDevicesID(s, user.getMaNhanVien());
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void requestNotificationPermission() {
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Yêu cầu quyền thông báo");
            builder.setMessage("Ứng dụng cần quyền truy cập thông báo để hoạt động đúng. Vui lòng cấp quyền cho ứng dụng.");
            builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Chuyển người dùng đến màn hình cài đặt để cấp quyền thông báo
                    Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
    }


    private void getWidget(){
        drawerLayout = findViewById(R.id.drawerLayout);
        imgDrawer = findViewById(R.id.imgDrawer);
        bottomNavigation = findViewById(R.id.bottomNav);
        viewPager = findViewById(R.id.viewPager2);
        txtName = findViewById(R.id.txtName);
        imgToProfile = findViewById(R.id.imgToProfile);
        txtTitle = findViewById(R.id.txtTitle);

        navigationView = findViewById(R.id.drawerMenu);

        database = new DBQuanLyBug();

        txtTitle.setText("Home page");
        database.setImageProfile(imgToProfile);
        database.getUserInfor(new DBQuanLyBug.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                txtName.setText(user.getTen());
            }
        });
    }
    private void addAction(){

        imgToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(false);
                int id = menuItem.getItemId();
                if (id == R.id.nav_logout){
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    // Đăng xuất người dùng
                    mAuth.signOut();

                    // Chuyển người dùng đến màn hình đăng nhập
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Kết thúc hoạt động hiện tại
                } else if (id == R.id.nav_add_project) {
                    database.getUserInfor(new DBQuanLyBug.UserCallback() {
                        @Override
                        public void onUserLoaded(User user) {
                            String chucVu = user.getMaNhanVien().substring(0, 2);
                            if (chucVu.equals("QL")){
                                Intent intent = new Intent(MainActivity.this, AddProjectsActivity.class);
                                startActivity(intent);
                                drawerLayout.closeDrawers();
                            }else {
                                Toast.makeText(MainActivity.this, "Bạn không có quyền để thêm project", Toast.LENGTH_SHORT).show();
                            }
                            
                        }
                    });
                    
                } else if (id == R.id.nav_add_bug) {
                    Intent intent = new Intent(MainActivity.this, AddBugActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawers();
                }
                return true;
            }
        });

    }

    private void setupViewPager() {
        viewPager.setAdapter(new ViewPagerAdapter(this));
        bottomNavigation.show(1, true);
    }

    private void setupBottomNavigation() {
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_nav_1));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_nav_2));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_nav_3));
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case 1:
                        viewPager.setCurrentItem(1);
                        txtTitle.setText("Home page");
                        break;
                    case 2:
                        viewPager.setCurrentItem(2);
                        txtTitle.setText("Your works");
                        break;
                    case 3:
                        viewPager.setCurrentItem(3);
                        txtTitle.setText("People");
                        break;
                }
            }
        });
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case 1:
                        viewPager.setCurrentItem(1);
                        txtTitle.setText("Home page");
                        break;
                    case 2:
                        viewPager.setCurrentItem(2);
                        txtTitle.setText("Your works");
                        break;
                    case 3:
                        viewPager.setCurrentItem(3);
                        txtTitle.setText("People");
                        break;
                }
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNavigation.show(position, true);
            }
        });


    }
    public void switchToDetailProjectFragment() {
        viewPager.setCurrentItem(0, false);
    }




}