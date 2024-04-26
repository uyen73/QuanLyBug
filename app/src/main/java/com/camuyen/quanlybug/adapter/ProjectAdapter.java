package com.camuyen.quanlybug.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.model.Project;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
    List<Project> list;

    public ProjectAdapter(List<Project> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_projects, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.ViewHolder holder, int position) {
        Project a = list.get(position);
        holder.txtTimeStart.setText(a.getNgayBatDau());
        holder.txtNamePM.setText(a.getTenQuanLy());
        holder.txtNameProject.setText(a.getTenDuAn());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNamePM, txtNameProject, txtTimeStart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNamePM = itemView.findViewById(R.id.txtNamePM);
            txtNameProject = itemView.findViewById(R.id.txtNameProject);
            txtTimeStart = itemView.findViewById(R.id.txtTimeStart);


        }
    }
}
