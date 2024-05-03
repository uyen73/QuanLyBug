package com.camuyen.quanlybug.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.Issue;
import com.camuyen.quanlybug.model.Jobs;
import com.camuyen.quanlybug.model.User;

import java.util.ArrayList;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {
    List<Jobs> list;

    public JobAdapter(List<Jobs> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public JobAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobAdapter.ViewHolder holder, int position) {
        Jobs jobs = list.get(position);
        String maNhanVien = jobs.getMaNhanVien();
        String maVanDe = jobs.getMaVanDe();
        DBQuanLyBug database = new DBQuanLyBug();
        database.getUsers(new DBQuanLyBug.UCallBack() {
            @Override
            public void onULoaded(List<User> users) {
                for (User a : users) {
                    String ma = a.getMaNhanVien();
                    if(maNhanVien.equals(ma)){
                        holder.txtTenThanhVien.setText("Người thực hiện: " + a.getHoTen());
                    }
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        database.getIssuesInfo(new DBQuanLyBug.IssueCallBack() {
            @Override
            public void onIssuesLoaded(List<Issue> issues) {
                for (Issue a : issues) {
                    String vd = a.getMaVanDe();
                    if(maVanDe.equals(vd)){
                        holder.txtMaVanDe.setText(a.getTenVanDe());
                        break;
                    }
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        holder.txtMaVanDe.setText(jobs.getMaVanDe());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtMaVanDe, txtTenThanhVien;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaVanDe = itemView.findViewById(R.id.txtMaVanDe);
            txtTenThanhVien = itemView.findViewById(R.id.txtTenThanhVien);
        }
    }
}
