package com.camuyen.quanlybug.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.model.Bugs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BugAdapter extends RecyclerView.Adapter<BugAdapter.ViewHolder> {
    List<Bugs> list = new ArrayList<>();

    public BugAdapter(List<Bugs> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public BugAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bug, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BugAdapter.ViewHolder holder, int position) {
        Bugs bug = list.get(position);
        holder.txtTenBug.setText(bug.getTenBug());
        holder.txtTienDo.setText(bug.getTrangThai());
        holder.txtTenDev.setText(bug.getDevFix());

        Date deadline = bug.getDeadline();
        holder.txtDeadline.setText(convertToString(deadline));

        Date thoiGianTao = bug.getNgayXuatHien();
        holder.txtThoiGianTao.setText(convertToString(thoiGianTao));

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
    public int getItemCount() {
        if (!list.isEmpty()){
            return list.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenBug, txtTienDo, txtTenDev, txtDeadline, txtThoiGianTao;
        CardView cardviewTienDo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenBug = itemView.findViewById(R.id.txtTenBug);
            txtTienDo = itemView.findViewById(R.id.txtTienDo);
            txtTenDev = itemView.findViewById(R.id.txtTenDev);
            txtDeadline = itemView.findViewById(R.id.txtDeadline);
            txtThoiGianTao = itemView.findViewById(R.id.txtThoiGianTao);
            cardviewTienDo = itemView.findViewById(R.id.cardviewTienDo);
        }
    }
}
