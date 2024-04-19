package com.camuyen.quanlybug;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.camuyen.quanlybug.adapter.ViewPagerAdapter;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.User;
import com.camuyen.quanlybug.profile.ProfileActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class MainActivity extends AppCompatActivity {

    MeowBottomNavigation bottomNavigation;
    ViewPager2 viewPager;
    public TextView txtName, txtTitle;
    ImageView imgToProfile;
    DBQuanLyBug database;

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
        bottomNavigation = findViewById(R.id.bottomNav);
        viewPager = findViewById(R.id.viewPager2);
        txtName = findViewById(R.id.txtName);
        imgToProfile = findViewById(R.id.imgToProfile);
        txtTitle = findViewById(R.id.txtTitle);

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
    }

    private void setupViewPager() {
        viewPager.setAdapter(new ViewPagerAdapter(this));
        bottomNavigation.show(0, true);
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Toast.makeText(MainActivity.this, item.getId(), Toast.LENGTH_SHORT).show();
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

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNavigation.show(position, true);
            }
        });


    }
}