package com.camuyen.quanlybug.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.model.Issue;

import java.util.ArrayList;
import java.util.List;

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.ViewHolder> {
    List<Issue> list;
    public IssueAdapter(List<Issue> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public IssueAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_issue, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueAdapter.ViewHolder holder, int position) {
        Issue issue = list.get(position);
        String tenVanDe = issue.getTenVanDe();
        String tenDev = issue.getDev();
        String tenTest = issue.getTest();
        String deadline = issue.getNgayBatDau();

        holder.txtTenTest.setText(tenTest);
        holder.txtDeadline.setText(deadline);
        holder.txtTenDev.setText(tenDev);
        holder.txtTenVanDe.setText(tenVanDe);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenVanDe, txtTenDev, txtTenTest, txtDeadline;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDeadline = itemView.findViewById(R.id.txtDeadline);
            txtTenDev = itemView.findViewById(R.id.txtTenDev);
            txtTenVanDe = itemView.findViewById(R.id.txtTenVanDe);
            txtTenTest = itemView.findViewById(R.id.txtTenTest);


        }
    }
}
