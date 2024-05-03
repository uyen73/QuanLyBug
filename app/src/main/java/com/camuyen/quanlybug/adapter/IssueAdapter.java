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
    List<Issue> list = new ArrayList<>();
    public IssueAdapter(List<Issue> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public IssueAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueAdapter.ViewHolder holder, int position) {
        Issue issue = list.get(position);
        holder.txtNameIssue.setText(issue.getTenVanDe());
        holder.txtDescribe.setText(issue.getMoTa());
        holder.txtStatus.setText(issue.getTienDoTest());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtNameIssue, txtStatus, txtDescribe;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);



        }
    }
}
