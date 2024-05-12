package com.camuyen.quanlybug.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.adapter.BugAdapter;
import com.camuyen.quanlybug.adapter.JobAdapter;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.Bugs;
import com.camuyen.quanlybug.model.Jobs;
import com.camuyen.quanlybug.model.User;
import com.camuyen.quanlybug.projects.RepairBugActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class JobFragment extends Fragment {
    RecyclerView listJobs;
    DBQuanLyBug database;
    BugAdapter adapter;
    ImageView imgFilter;

    List<Bugs> buglist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        database = new DBQuanLyBug();
        getWidget(view);
        addAction(view);
        capNhatList();
        return view;
    }

    private void getWidget(View view) {
        listJobs = view.findViewById(R.id.listJobs);
        database = new DBQuanLyBug();
        imgFilter = view.findViewById(R.id.imgFilter);


    }
    private void addAction(View view){
        imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getUserInfor(new DBQuanLyBug.UserCallback() {
                    @Override
                    public void onUserLoaded(User user) {
                        showPopupMenu(v, user.getMaNhanVien());
                    }
                });

            }
        });
    }
    public void capNhatList(){
        adapter = new BugAdapter(buglist, getActivity());
        listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
        listJobs.setAdapter(adapter);
        database.getBugsInfo(new DBQuanLyBug.BugsCallBack() {
            @Override
            public void onBugsLoaded(List<Bugs> bugs) {
                database.getUserInfor(new DBQuanLyBug.UserCallback() {
                    @Override
                    public void onUserLoaded(User user) {
                        List<Bugs> buglist = new ArrayList<>();
                        String maNV = user.getMaNhanVien();
                        for (Bugs a : bugs){
                            if (a.getMaNhanVien().equals(maNV)){
                                buglist.add(a);
                            }
                        }
                        adapter = new BugAdapter(buglist, getActivity());
                        listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                        listJobs.setAdapter(adapter);

                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    private void showPopupMenu(View v, String maNV) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
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
                                if (a.getMaNhanVien().equals(maNV)){
                                    bugsList.add(a);
                                }
                            }
                            adapter = new BugAdapter(bugsList, getActivity());
                            listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                            listJobs.setAdapter(adapter);

                        }
                    }, "New");
                }
                if (item.getItemId() == R.id.mnuOpen){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            List<Bugs> bugsList = new ArrayList<>();
                            for (Bugs a : bugs){
                                if (a.getMaNhanVien().equals(maNV)){
                                    bugsList.add(a);
                                }
                            }
                            adapter = new BugAdapter(bugsList, getActivity());
                            listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                            listJobs.setAdapter(adapter);

                        }
                    }, "Open");
                }
                if (item.getItemId() == R.id.mnuFix){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            List<Bugs> bugsList = new ArrayList<>();
                            for (Bugs a : bugs){
                                if (a.getMaNhanVien().equals(maNV)){
                                    bugsList.add(a);
                                }
                            }
                            adapter = new BugAdapter(bugsList, getActivity());
                            listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                            listJobs.setAdapter(adapter);

                        }
                    }, "Fix");
                }
                if (item.getItemId() == R.id.mnuPending){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            List<Bugs> bugsList = new ArrayList<>();
                            for (Bugs a : bugs){
                                if (a.getMaNhanVien().equals(maNV)){
                                    bugsList.add(a);
                                }
                            }
                            adapter = new BugAdapter(bugsList, getActivity());
                            listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                            listJobs.setAdapter(adapter);

                        }
                    }, "Pending");
                }
                if (item.getItemId() == R.id.mnuReopen){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            List<Bugs> bugsList = new ArrayList<>();
                            for (Bugs a : bugs){
                                if (a.getMaNhanVien().equals(maNV)){
                                    bugsList.add(a);
                                }
                            }
                            adapter = new BugAdapter(bugsList, getActivity());
                            listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                            listJobs.setAdapter(adapter);

                        }
                    }, "Reopen");
                }
                if (item.getItemId() == R.id.mnuClose){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            List<Bugs> bugsList = new ArrayList<>();
                            for (Bugs a : bugs){
                                if (a.getMaNhanVien().equals(maNV)){
                                    bugsList.add(a);
                                }
                            }
                            adapter = new BugAdapter(bugsList, getActivity());
                            listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                            listJobs.setAdapter(adapter);

                        }
                    }, "Close");
                }
                if (item.getItemId() == R.id.mnuRejected){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            List<Bugs> bugsList = new ArrayList<>();
                            for (Bugs a : bugs){
                                if (a.getMaNhanVien().equals(maNV)){
                                    bugsList.add(a);
                                }
                            }
                            adapter = new BugAdapter(bugsList, getActivity());
                            listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                            listJobs.setAdapter(adapter);

                        }
                    }, "Rejected");
                }
                return true;
            }
        });

        popupMenu.show();
    }
}