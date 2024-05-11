package com.camuyen.quanlybug.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.camuyen.quanlybug.MainActivity;
import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.fragment.DetailProjectFragment;
import com.camuyen.quanlybug.model.BugStatus;
import com.camuyen.quanlybug.model.Bugs;
import com.camuyen.quanlybug.model.User;
import com.camuyen.quanlybug.projects.RepairBugActivity;
import com.camuyen.quanlybug.projects.RepairProjectActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BugAdapter extends RecyclerView.Adapter<BugAdapter.ViewHolder> {
    List<Bugs> list = new ArrayList<>();
    Context context;
    DBQuanLyBug database;
    public BugAdapter(List<Bugs> list, Context context) {
        this.list = list;
        this.context = context;
        database = new DBQuanLyBug();
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

        String tienDo = bug.getTrangThai();
        BugStatus bugStatus = new BugStatus();
        List<BugStatus> bugStatusList = bugStatus.getBugStatusList();
//
//        bugStatusList.add(new BugStatus("New", "#fed0ca", "#8f202a"));
//        bugStatusList.add(new BugStatus("Open", "#fed0ca", "#4f7398"));
//        bugStatusList.add(new BugStatus("Fix", "#d3edbc", "#3f6c3f"));
//        bugStatusList.add(new BugStatus("Pending", "#18a034", "#c0f8cf"));
//        bugStatusList.add(new BugStatus("Reopen", "#f14747", "#ffe1e1"));
//        bugStatusList.add(new BugStatus("Close", "#8d8586", "#f9f8f1"));
//        bugStatusList.add(new BugStatus("Rejected", "#e6e6e6", "#645d63"));
        for (BugStatus a : bugStatusList) {
            if (tienDo.equals(a.getStatus())){
                holder.txtTienDo.setTextColor(Color.parseColor(a.getText()));
                holder.cardviewTienDo.setCardBackgroundColor(Color.parseColor(a.getBackground()));
            }
        }
//        if (tienDo.equals("Fix")){
//            holder.txtTienDo.setTextColor(Color.parseColor("#333333"));
//            holder.cardviewTienDo.setCardBackgroundColor(Color.parseColor("#F2F2F2"));
//        }else if (tienDo.equals("Close")) {
//            holder.txtTienDo.setTextColor(Color.parseColor("#008000"));
//            holder.cardviewTienDo.setCardBackgroundColor(Color.parseColor("#DFF0D8"));
//        }

        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, bug.getMaBug());
            }
        });

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
        ImageView imgMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenBug = itemView.findViewById(R.id.txtTenBug);
            txtTienDo = itemView.findViewById(R.id.txtTienDo);
            txtTenDev = itemView.findViewById(R.id.txtTenDev);
            txtDeadline = itemView.findViewById(R.id.txtDeadline);
            txtThoiGianTao = itemView.findViewById(R.id.txtThoiGianTao);
            cardviewTienDo = itemView.findViewById(R.id.cardviewTienDo);

            imgMore = itemView.findViewById(R.id.imgMoreBug);
        }
    }
    private void showPopupMenu(View v, String maBug) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_bug, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.mnuSuaBug){
                    Intent intent = new Intent(context, RepairBugActivity.class);
                    intent.putExtra("maBug", maBug);
                    context.startActivity(intent);
                    System.out.println(maBug);
                }
                if (item.getItemId() == R.id.mnuXoaBug){
                    deleteDocument(maBug);
                }
                return true;
            }
        });

        popupMenu.show();
    }
    public void deleteDocument(String maBug) {
        // Hiển thị AlertDialog để xác nhận việc xóa
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa tài liệu này?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Thực hiện xóa tài liệu
                database.deleteDocument(context, "bugs", maBug);
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                ((MainActivity)context).viewPager.setCurrentItem(0);
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}
