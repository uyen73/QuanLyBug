package com.camuyen.quanlybug.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.Comments;
import com.camuyen.quanlybug.model.User;

import java.util.List;
import java.util.Objects;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    List<Comments> list;
    DBQuanLyBug dbQuanLyBug;
    public CommentAdapter(List<Comments> list) {
        this.list = list;
        dbQuanLyBug = new DBQuanLyBug();
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comments comments = list.get(position);
        dbQuanLyBug.getUsers(new DBQuanLyBug.UCallBack() {
            @Override
            public void onULoaded(List<User> users) {
                for(User user : users){
                    if(user.getMaNhanVien().equals(comments.getMaNhanVien())){
                        holder.txtNameComment.setText(user.getHoTen());
                    }
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

        holder.txtComment.setText(comments.getNoiDung());
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgComment;
        TextView txtNameComment, txtComment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgComment = itemView.findViewById(R.id.imgComment);
            txtNameComment = itemView.findViewById(R.id.txtNameComment);
            txtComment = itemView.findViewById(R.id.txtComment);
        }
    }
}
