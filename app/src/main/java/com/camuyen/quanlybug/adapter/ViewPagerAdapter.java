package com.camuyen.quanlybug.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.camuyen.quanlybug.MainActivity;
import com.camuyen.quanlybug.fragment.DetailProjectFragment;
import com.camuyen.quanlybug.fragment.JobFragment;
import com.camuyen.quanlybug.fragment.NewsFragment;
import com.camuyen.quanlybug.fragment.PeopleFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private Fragment[] fragments;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragments = new Fragment[4];
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[position] = new DetailProjectFragment();
                    break;
                case 1:
                    fragments[position] = new NewsFragment();
                    break;
                case 2:
                    fragments[position] = new JobFragment();
                    break;
                case 3:
                    fragments[position] = new PeopleFragment();
                    break;
                default:
                    fragments[position] = new NewsFragment();
                    break;
            }
        }
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public Fragment getFragment(int position) {
        return fragments[position];
    }
}
