package com.camuyen.quanlybug.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.camuyen.quanlybug.MainActivity;
import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.fragment.DetailProjectFragment;
import com.camuyen.quanlybug.model.Project;
import com.camuyen.quanlybug.projects.OpenProjectActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    public static String convertToString(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.format(date);
        } else {
            return null;
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Project a = list.get(position);
        Date ngayBatDau = a.getNgayBatDau();
        holder.txtTimeStart.setText(convertToString(ngayBatDau));
        holder.txtNameProject.setText(a.getTenDuAn());
        holder.txtTienDo.setText(a.getTrangThai());


        holder.cardviewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ma = a.getMaDuAn();
                SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("maDuAn", ma);
                editor.apply();

                ((MainActivity) context).switchToDetailProjectFragment();


            }
        });
        String tienDo = a.getTrangThai();
        if (tienDo.equals("Processing")){
            holder.txtTienDo.setTextColor(Color.WHITE);
            holder.cardviewTienDoProject.setCardBackgroundColor(Color.parseColor("#e74c3c"));
        }else if (tienDo.equals("Done")) {
            holder.txtTienDo.setTextColor(Color.BLACK);
            holder.cardviewTienDoProject.setCardBackgroundColor(Color.parseColor("#27ae60"));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNameProject, txtTimeStart, txtTienDo;
        CardView cardviewProject, cardviewTienDoProject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardviewTienDoProject = itemView.findViewById(R.id.cardviewTienDoProject);
            txtNameProject = itemView.findViewById(R.id.txtNameProject);
            txtTimeStart = itemView.findViewById(R.id.txtTimeStart);
            cardviewProject = itemView.findViewById(R.id.cardviewProject);
            txtTienDo = itemView.findViewById(R.id.txtTienDo);

        }


    }
}
