package com.camuyen.quanlybug.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {
    List<User> list = new ArrayList<>();
    DBQuanLyBug database;
    public PeopleAdapter(List<User> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public PeopleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_people, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleAdapter.ViewHolder holder, int position) {
        User a = list.get(position);
        holder.txtNamePeople.setText(a.getHoTen());
        holder.txtPosition.setText(a.getChucVu());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgProfilePeople;
        TextView txtPosition, txtNamePeople;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPosition = itemView.findViewById(R.id.txtPosition);
            txtNamePeople = itemView.findViewById(R.id.txtNamePeople);
            imgProfilePeople = itemView.findViewById(R.id.imgProfilePeople);

        }
    }
}