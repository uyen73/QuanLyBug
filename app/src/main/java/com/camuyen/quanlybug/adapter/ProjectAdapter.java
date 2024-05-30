package com.camuyen.quanlybug.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.camuyen.quanlybug.fragment.JobFragment;
import com.camuyen.quanlybug.model.Bugs;
import com.camuyen.quanlybug.model.Project;
import com.camuyen.quanlybug.model.User;
import com.camuyen.quanlybug.projects.RepairProjectActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import kotlinx.coroutines.Job;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
    List<Project> list;
    Context context;
    DBQuanLyBug database;
    public ProjectAdapter(List<Project> list, Context context) {
        this.list = list;
        this.context = context;
        database = new DBQuanLyBug();
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
        database.getUserInfor(new DBQuanLyBug.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                String chucVu = user.getMaNhanVien().substring(0, 2);
                System.out.println(chucVu);
                if (chucVu.equals("QL")){
                    holder.imgMore.setVisibility(View.VISIBLE);
                }

            }
        });
        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, a.getMaDuAn(), a.getMaQuanLy());
            }
        });
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
    private void showPopupMenu(View v, String maDuAn, String maQuanLy) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_project, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.mnuSuaProject){
                    Intent intent = new Intent(context, RepairProjectActivity.class);
                    intent.putExtra("maDuAn", maDuAn);
                    intent.putExtra("maQuanLy", maQuanLy);
                    context.startActivity(intent);
                }
                if (item.getItemId() == R.id.mnuXoaProject){
                    deleteDocument(maDuAn);
                    database.getBugsInfo(new DBQuanLyBug.BugsCallBack() {
                        @Override
                        public void onBugsLoaded(List<Bugs> bugs) {
                            for (Bugs bug : bugs) {
                                if (bug.getMaDuAn().equals(maDuAn)) {
                                    database.deleteDocument(context, "bugs", bug.getMaBug());
                                }
                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }
                return true;
            }
        });

        popupMenu.show();
    }

    public void deleteDocument(String maDuAn) {
        // Hiển thị AlertDialog để xác nhận việc xóa
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa tài liệu này?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Thực hiện xóa tài liệu
                database.deleteDocument(context,"projects", maDuAn);
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNameProject, txtTimeStart, txtTienDo;
        CardView cardviewProject, cardviewTienDoProject;
        ImageView imgMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardviewTienDoProject = itemView.findViewById(R.id.cardviewTienDoProject);
            txtNameProject = itemView.findViewById(R.id.txtNameProject);
            txtTimeStart = itemView.findViewById(R.id.txtTimeStart);
            cardviewProject = itemView.findViewById(R.id.cardviewProject);
            txtTienDo = itemView.findViewById(R.id.txtTienDo);
            imgMore = itemView.findViewById(R.id.imgMore);
        }


    }
}
