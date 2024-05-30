package com.camuyen.quanlybug.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.adapter.BugAdapter;
import com.camuyen.quanlybug.adapter.ProjectAdapter;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.Bugs;
import com.camuyen.quanlybug.model.Project;
import com.camuyen.quanlybug.model.User;

import java.util.ArrayList;
import java.util.List;


public class JobFragment extends Fragment {
    RecyclerView listJobs, listProject;
    DBQuanLyBug database;
    BugAdapter adapter;
    ProjectAdapter projectAdapter;
    ImageView imgFilter;
    TextView txtTextProject, txtTextBug;
    Spinner spnProject;

    List<Bugs> buglist = new ArrayList<>();
    List<Project> projectlist = new ArrayList<>();
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
        listProject = view.findViewById(R.id.listProject);
        txtTextProject = view.findViewById(R.id.txtTextProject);
        txtTextBug = view.findViewById(R.id.txtTextBug);
        spnProject = view.findViewById(R.id.spnProject);
        database.getUserInfor(new DBQuanLyBug.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                if (user.getMaNhanVien().startsWith("TEST") || user.getMaNhanVien().startsWith("DEV")){
                    imgFilter.setVisibility(View.VISIBLE);
                }
            }
        });
        database.getProjectsInfo(new DBQuanLyBug.ProjectsCallBack() {
            @Override
            public void onProjectsLoaded(List<Project> projects) {
                ArrayAdapter<Project> spnAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, projects);
                spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnProject.setAdapter(spnAdapter);
            }

            @Override
            public void onError(Exception e) {

            }
        });

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

        projectAdapter = new ProjectAdapter(projectlist, getActivity());
        listProject.setLayoutManager(new LinearLayoutManager(getActivity()));
        listProject.setAdapter(projectAdapter);

        database.getBugsInfo(new DBQuanLyBug.BugsCallBack() {
            @Override
            public void onBugsLoaded(List<Bugs> bugs) {
                database.getUserInfor(new DBQuanLyBug.UserCallback() {
                    @Override
                    public void onUserLoaded(User user) {
                        if (user.getMaNhanVien().startsWith("QL")){
                            String maNV = user.getMaNhanVien();
                            database.getProjectsInfo(new DBQuanLyBug.ProjectsCallBack() {
                                @Override
                                public void onProjectsLoaded(List<Project> projects) {
                                    List<Project> projectlist = new ArrayList<>();
                                    for (Project a : projects){
                                        if (a.getMaQuanLy().equals(maNV)){
                                            projectlist.add(a);
                                        }
                                    }
                                    if (!projectlist.isEmpty()){
                                        txtTextProject.setVisibility(View.VISIBLE);
                                    }
                                    projectAdapter = new ProjectAdapter(projectlist, getActivity());
                                    listProject.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    listProject.setAdapter(projectAdapter);
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });


                        } else if (user.getMaNhanVien().startsWith("DEV")) {
                            List<Bugs> buglist = new ArrayList<>();
                            String maNV = user.getMaNhanVien();
                            for (Bugs a : bugs){
                                if (a.getMaNhanVien().equals(maNV)){
                                    buglist.add(a);
                                }
                            }
                            if (!buglist.isEmpty()){
                                txtTextBug.setVisibility(View.VISIBLE);
                            }
                            adapter = new BugAdapter(buglist, getActivity());
                            listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                            listJobs.setAdapter(adapter);
                        } else if (user.getMaNhanVien().startsWith("TEST")) {
                            List<Bugs> buglist = new ArrayList<>();
                            String maNV = user.getMaNhanVien();
                            for (Bugs a : bugs){
                                if (a.getMaQuanLy().equals(maNV)){
                                    buglist.add(a);
                                }
                            }
                            if (!buglist.isEmpty()){
                                txtTextBug.setVisibility(View.VISIBLE);
                            }
                            adapter = new BugAdapter(buglist, getActivity());
                            listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                            listJobs.setAdapter(adapter);
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });

        spnProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Project project = (Project) parent.getItemAtPosition(position);
                String maDuAn = project.getMaDuAn();
                database.getUserInfor(new DBQuanLyBug.UserCallback() {
                    @Override
                    public void onUserLoaded(User user) {
                        database.getBugsInfo(new DBQuanLyBug.BugsCallBack() {
                            @Override
                            public void onBugsLoaded(List<Bugs> bugs) {
                                if (user.getMaNhanVien().startsWith("DEV")){
                                    List<Bugs> listSPN = new ArrayList<>();
                                    for (Bugs a : bugs){
                                        if (a.getMaDuAn().equals(maDuAn) && a.getMaNhanVien().equals(user.getMaNhanVien())){
                                            listSPN.add(a);
                                        }
                                    }
                                    adapter = new BugAdapter(listSPN, getActivity());
                                    listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    listJobs.setAdapter(adapter);
                                } else if (user.getMaNhanVien().startsWith("TEST")){
                                    List<Bugs> listSPN = new ArrayList<>();
                                    for (Bugs a : bugs){
                                        if (a.getMaDuAn().equals(maDuAn) && a.getMaQuanLy().equals(user.getMaNhanVien())){
                                            listSPN.add(a);
                                        }
                                    }
                                    adapter = new BugAdapter(listSPN, getActivity());
                                    listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    listJobs.setAdapter(adapter);
                                }

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void showPopupMenu(View v, String maNV) {
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_job, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.mnuReset){
                    database.getBugsInfo(new DBQuanLyBug.BugsCallBack() {
                        @Override
                        public void onBugsLoaded(List<Bugs> bugs) {
                            if (maNV.startsWith("TEST")){
                                List<Bugs> listBug = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaQuanLy().equals(maNV)){
                                        listBug.add(a);
                                    }
                                }
                                adapter = new BugAdapter(listBug, getActivity());
                                listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                listJobs.setAdapter(adapter);
                            } else if (maNV.startsWith("DEV")) {
                                List<Bugs> listBug = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaNhanVien().equals(maNV)){
                                        listBug.add(a);
                                    }
                                }
                                adapter = new BugAdapter(listBug, getActivity());
                                listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                listJobs.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
                if (item.getItemId() == R.id.mnuNew){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            if (maNV.startsWith("TEST")){
                                List<Bugs> listBug = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaQuanLy().equals(maNV)){
                                        listBug.add(a);
                                    }
                                }
                                adapter = new BugAdapter(listBug, getActivity());
                                listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                listJobs.setAdapter(adapter);
                            } else if (maNV.startsWith("DEV")) {
                                List<Bugs> listBug = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaNhanVien().equals(maNV)){
                                        listBug.add(a);
                                    }
                                }
                                adapter = new BugAdapter(listBug, getActivity());
                                listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                listJobs.setAdapter(adapter);
                            }

                        }
                    }, "New");
                }
                if (item.getItemId() == R.id.mnuOpen){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            if (maNV.startsWith("TEST")){
                                List<Bugs> listBug = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaQuanLy().equals(maNV)){
                                        listBug.add(a);
                                    }
                                }
                                adapter = new BugAdapter(listBug, getActivity());
                                listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                listJobs.setAdapter(adapter);
                            } else if (maNV.startsWith("DEV")) {
                                List<Bugs> listBug = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaNhanVien().equals(maNV)){
                                        listBug.add(a);
                                    }
                                }
                                adapter = new BugAdapter(listBug, getActivity());
                                listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                listJobs.setAdapter(adapter);
                            }

                        }
                    }, "Open");
                }
                if (item.getItemId() == R.id.mnuFix){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            if (maNV.startsWith("TEST")){
                                List<Bugs> listBug = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaQuanLy().equals(maNV)){
                                        listBug.add(a);
                                    }
                                }
                                adapter = new BugAdapter(listBug, getActivity());
                                listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                listJobs.setAdapter(adapter);
                            } else if (maNV.startsWith("DEV")) {
                                List<Bugs> listBug = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaNhanVien().equals(maNV)){
                                        listBug.add(a);
                                    }
                                }
                                adapter = new BugAdapter(listBug, getActivity());
                                listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                listJobs.setAdapter(adapter);
                            }

                        }
                    }, "Fix");
                }
                if (item.getItemId() == R.id.mnuPending){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            if (maNV.startsWith("TEST")){
                                List<Bugs> listBug = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaQuanLy().equals(maNV)){
                                        listBug.add(a);
                                    }
                                }
                                adapter = new BugAdapter(listBug, getActivity());
                                listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                listJobs.setAdapter(adapter);
                            } else if (maNV.startsWith("DEV")) {
                                List<Bugs> listBug = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaNhanVien().equals(maNV)){
                                        listBug.add(a);
                                    }
                                }
                                adapter = new BugAdapter(listBug, getActivity());
                                listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                listJobs.setAdapter(adapter);
                            }

                        }
                    }, "Pending");
                }
                if (item.getItemId() == R.id.mnuReopen){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            if (maNV.startsWith("TEST")){
                                List<Bugs> listBug = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaQuanLy().equals(maNV)){
                                        listBug.add(a);
                                    }
                                }
                                adapter = new BugAdapter(listBug, getActivity());
                                listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                listJobs.setAdapter(adapter);
                            } else if (maNV.startsWith("DEV")) {
                                List<Bugs> listBug = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaNhanVien().equals(maNV)){
                                        listBug.add(a);
                                    }
                                }
                                adapter = new BugAdapter(listBug, getActivity());
                                listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                listJobs.setAdapter(adapter);
                            }

                        }
                    }, "Reopen");
                }
                if (item.getItemId() == R.id.mnuClose){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            if (maNV.startsWith("TEST")){
                                List<Bugs> listBug = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaQuanLy().equals(maNV)){
                                        listBug.add(a);
                                    }
                                }
                                adapter = new BugAdapter(listBug, getActivity());
                                listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                listJobs.setAdapter(adapter);
                            } else if (maNV.startsWith("DEV")) {
                                List<Bugs> listBug = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaNhanVien().equals(maNV)){
                                        listBug.add(a);
                                    }
                                }
                                adapter = new BugAdapter(listBug, getActivity());
                                listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                listJobs.setAdapter(adapter);
                            }

                        }
                    }, "Close");
                }
                if (item.getItemId() == R.id.mnuRejected){
                    database.getBugFiler(new DBQuanLyBug.BugFilerCallBack() {
                        @Override
                        public void onBugsFilterLoaded(List<Bugs> bugs) {
                            if (maNV.startsWith("TEST")){
                                List<Bugs> listBug = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaQuanLy().equals(maNV)){
                                        listBug.add(a);
                                    }
                                }
                                adapter = new BugAdapter(listBug, getActivity());
                                listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                listJobs.setAdapter(adapter);
                            } else if (maNV.startsWith("DEV")) {
                                List<Bugs> listBug = new ArrayList<>();
                                for (Bugs a : bugs){
                                    if (a.getMaNhanVien().equals(maNV)){
                                        listBug.add(a);
                                    }
                                }
                                adapter = new BugAdapter(listBug, getActivity());
                                listJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                                listJobs.setAdapter(adapter);
                            }

                        }
                    }, "Rejected");
                }
                return true;
            }
        });
        popupMenu.show();
    }
}