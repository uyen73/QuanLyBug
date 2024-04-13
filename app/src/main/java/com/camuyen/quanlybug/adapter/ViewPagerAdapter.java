package com.camuyen.quanlybug.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.camuyen.quanlybug.fragment.JobFragment;
import com.camuyen.quanlybug.fragment.NewsFragment;
import com.camuyen.quanlybug.fragment.PeopleFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new NewsFragment();
            case 1:
                return new JobFragment();
            case 2:
                return new PeopleFragment();
            default:
                return new NewsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Số lượng trang trong ViewPager2 của bạn
    }
}
