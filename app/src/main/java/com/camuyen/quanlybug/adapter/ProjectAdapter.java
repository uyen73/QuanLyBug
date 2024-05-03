package com.camuyen.quanlybug.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.model.Project;
import com.camuyen.quanlybug.projects.OpenProjectActivity;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
    List<Project> list;
    Context context;

    public ProjectAdapter(List<Project> list, Context context) {
        this.list = list;
        this.context = context;
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
        String maDuAn = a.getMaDuAn();
        holder.cardviewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OpenProjectActivity.class);
                intent.putExtra("maDuAn", maDuAn);
                Toast.makeText(context, maDuAn, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNamePM, txtNameProject, txtTimeStart;
        CardView cardviewProject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNamePM = itemView.findViewById(R.id.txtNamePM);
            txtNameProject = itemView.findViewById(R.id.txtNameProject);
            txtTimeStart = itemView.findViewById(R.id.txtTimeStart);
            cardviewProject = itemView.findViewById(R.id.cardviewProject);


        }

    }
}
