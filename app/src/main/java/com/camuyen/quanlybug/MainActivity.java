package com.camuyen.quanlybug;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.camuyen.quanlybug.adapter.ViewPagerAdapter;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class MainActivity extends AppCompatActivity {

    MeowBottomNavigation bottomNavigation;
    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNav);
        viewPager = findViewById(R.id.viewPager2);

        setupViewPager();
        setupBottomNavigation();

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

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case 0:
                        viewPager.setCurrentItem(0);
                        break;
                    case 1:
                        viewPager.setCurrentItem(1);
                        break;
                    case 2:
                        viewPager.setCurrentItem(2);
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