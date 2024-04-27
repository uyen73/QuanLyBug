package com.camuyen.quanlybug;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.camuyen.quanlybug.adapter.ViewPagerAdapter;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.User;
import com.camuyen.quanlybug.profile.ProfileActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    MeowBottomNavigation bottomNavigation;
    ViewPager2 viewPager;
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
                txtName.setText(user.getHoTen());
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
                int id = menuItem.getItemId();
                if (id == R.id.nav_me){
                    Toast.makeText(MainActivity.this, "Về chúng tôi", Toast.LENGTH_SHORT).show();
                }else if (id == R.id.nav_logout){
                    Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

    }

    private void setupViewPager() {
        viewPager.setAdapter(new ViewPagerAdapter(this));
        bottomNavigation.show(0, true);
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

            }

        });
    }

    private void setupBottomNavigation() {
        bottomNavigation.add(new MeowBottomNavigation.Model(0, R.drawable.ic_nav_1));
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_nav_2));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_nav_3));
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case 0:
                        viewPager.setCurrentItem(0);
                        txtTitle.setText("Home page");
                        break;
                    case 1:
                        viewPager.setCurrentItem(1);
                        txtTitle.setText("Your works");
                        break;
                    case 2:
                        viewPager.setCurrentItem(2);
                        txtTitle.setText("People");
                        break;
                }
            }
        });
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case 0:
                        viewPager.setCurrentItem(0);
                        txtTitle.setText("Home page");
                        break;
                    case 1:
                        viewPager.setCurrentItem(1);
                        txtTitle.setText("Your works");
                        break;
                    case 2:
                        viewPager.setCurrentItem(2);
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
}