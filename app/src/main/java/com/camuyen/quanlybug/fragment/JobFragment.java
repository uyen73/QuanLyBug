package com.camuyen.quanlybug.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.adapter.BugAdapter;
import com.camuyen.quanlybug.adapter.JobAdapter;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.Bugs;
import com.camuyen.quanlybug.model.Jobs;
import com.camuyen.quanlybug.model.User;
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


    }
    private void addAction(View view){

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
}