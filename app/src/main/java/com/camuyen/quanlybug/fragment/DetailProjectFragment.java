package com.camuyen.quanlybug.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.adapter.BugAdapter;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.Bugs;
import com.camuyen.quanlybug.model.Project;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class DetailProjectFragment extends Fragment {
    TextView txtMoTa, txtChiTietMoTa;
    RecyclerView recyclerViewBugs;
    BugAdapter adapter;
    DBQuanLyBug database;
    String maDuAn = "";

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    // Xử lý sự kiện khi fragment được hiển thị
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFragmentVisible(FragmentVisibleEvent event) {
        // Kết thúc chính fragment hiện tại
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new FragmentVisibleEvent());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_project, container, false);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        maDuAn = sharedPreferences.getString("maDuAn", ""); // "" là giá trị mặc định nếu không tìm thấy
        System.out.println(maDuAn);
        database = new DBQuanLyBug();
        getWidget(view);
        addAction();

        return view;
    }

    private void getWidget(View view) {
        txtChiTietMoTa = view.findViewById(R.id.txtChiTietMoTa);
        txtMoTa = view.findViewById(R.id.txtMoTa);
        recyclerViewBugs = view.findViewById(R.id.recycleviewBugs);

    }

    private void addAction() {
        database.getProjectsInfo(new DBQuanLyBug.ProjectsCallBack() {
            @Override
            public void onProjectsLoaded(List<Project> projects) {
                Project project = new Project();
                for (Project a : projects) {
                    if(a.getMaDuAn().equals(maDuAn)){
                        project = a;
                        break;
                    }
                }
                txtChiTietMoTa.setText(project.getMoTa());

            }

            @Override
            public void onError(Exception e) {

            }
        });
        txtMoTa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDescription();
            }
        });
        database.getBugsInfo(new DBQuanLyBug.BugsCallBack() {
            @Override
            public void onBugsLoaded(List<Bugs> bugs) {
                List<Bugs> list = new ArrayList<>();
                for (Bugs a : bugs) {
                    if(a.getMaDuAn().equals(maDuAn)){
                        list.add(a);
                    }
                }
                adapter = new BugAdapter(list, getActivity());
                recyclerViewBugs.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewBugs.setAdapter(adapter);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    private void toggleDescription() {
        if (txtChiTietMoTa.getVisibility() == View.VISIBLE) {
            animateViewOut(txtChiTietMoTa);
        } else {
            animateViewIn(txtChiTietMoTa);
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

}