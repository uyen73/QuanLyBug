package com.camuyen.quanlybug.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.adapter.BugAdapter;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.Bugs;
import com.camuyen.quanlybug.model.Project;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetailProjectFragment extends Fragment {
    TextView txtMoTa, txtChiTietMoTa, texxt;
    ImageView imgBugFilter;
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

        database = new DBQuanLyBug();
        getWidget(view);
        addAction();

        return view;
    }

    private void getWidget(View view) {
        txtChiTietMoTa = view.findViewById(R.id.txtChiTietMoTa);
        txtMoTa = view.findViewById(R.id.txtMoTa);
        recyclerViewBugs = view.findViewById(R.id.recycleviewBugs);
        imgBugFilter = view.findViewById(R.id.imgBugFilter);
        texxt = view.findViewById(R.id.texxt);


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
                if (list.isEmpty()){
                    texxt.setText("Các bugs: Chưa có Bug nào");
                }
                adapter = new BugAdapter(list, getActivity());
                recyclerViewBugs.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewBugs.setAdapter(adapter);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        imgBugFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, maDuAn);
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
    private void showPopupMenu(View v, String maDuAn) {
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_bug_filter, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.mnuNew){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            List<Bugs> bugsList = new ArrayList<>();
                            for (Bugs a : bugs){
                                if (a.getMaDuAn().equals(maDuAn)){
                                    bugsList.add(a);
                                }
                            }
                            adapter = new BugAdapter(bugsList, getActivity());
                            recyclerViewBugs.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerViewBugs.setAdapter(adapter);

                        }
                    }, "New");
                }
                if (item.getItemId() == R.id.mnuOpen){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            List<Bugs> bugsList = new ArrayList<>();
                            for (Bugs a : bugs){
                                if (a.getMaDuAn().equals(maDuAn)){
                                    bugsList.add(a);
                                }
                            }
                            adapter = new BugAdapter(bugsList, getActivity());
                            recyclerViewBugs.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerViewBugs.setAdapter(adapter);

                        }
                    }, "Open");
                }
                if (item.getItemId() == R.id.mnuFix){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            List<Bugs> bugsList = new ArrayList<>();
                            for (Bugs a : bugs){
                                if (a.getMaDuAn().equals(maDuAn)){
                                    bugsList.add(a);
                                }
                            }
                            adapter = new BugAdapter(bugsList, getActivity());
                            recyclerViewBugs.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerViewBugs.setAdapter(adapter);

                        }
                    }, "Fix");
                }
                if (item.getItemId() == R.id.mnuPending){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            List<Bugs> bugsList = new ArrayList<>();
                            for (Bugs a : bugs){
                                if (a.getMaDuAn().equals(maDuAn)){
                                    bugsList.add(a);
                                }
                            }
                            adapter = new BugAdapter(bugsList, getActivity());
                            recyclerViewBugs.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerViewBugs.setAdapter(adapter);

                        }
                    }, "Pending");
                }
                if (item.getItemId() == R.id.mnuReopen){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            List<Bugs> bugsList = new ArrayList<>();
                            for (Bugs a : bugs){
                                if (a.getMaDuAn().equals(maDuAn)){
                                    bugsList.add(a);
                                }
                            }
                            adapter = new BugAdapter(bugsList, getActivity());
                            recyclerViewBugs.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerViewBugs.setAdapter(adapter);

                        }
                    }, "Reopen");
                }
                if (item.getItemId() == R.id.mnuClose){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            List<Bugs> bugsList = new ArrayList<>();
                            for (Bugs a : bugs){
                                if (a.getMaDuAn().equals(maDuAn)){
                                    bugsList.add(a);
                                }
                            }
                            adapter = new BugAdapter(bugsList, getActivity());
                            recyclerViewBugs.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerViewBugs.setAdapter(adapter);

                        }
                    }, "Close");
                }
                if (item.getItemId() == R.id.mnuRejected){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            List<Bugs> bugsList = new ArrayList<>();
                            for (Bugs a : bugs){
                                if (a.getMaDuAn().equals(maDuAn)){
                                    bugsList.add(a);
                                }
                            }
                            adapter = new BugAdapter(bugsList, getActivity());
                            recyclerViewBugs.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerViewBugs.setAdapter(adapter);

                        }
                    }, "Rejected");
                }
                return true;
            }
        });

        popupMenu.show();
    }

}