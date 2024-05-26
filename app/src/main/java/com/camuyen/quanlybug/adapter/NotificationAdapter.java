package com.camuyen.quanlybug.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.fragment.DetailBugActivity;
import com.camuyen.quanlybug.model.NotificationItem;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationItem> notifications;
    private Context context;

    public NotificationAdapter(List<NotificationItem> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationItem notification = notifications.get(position);
        holder.tvTitle.setText(notification.getTitle());
        holder.tvMessage.setText(notification.getMessage());

        if (notification.isRead()) {
            holder.circleStatus.setImageResource(R.drawable.ic_circle_white);
        } else {
            holder.circleStatus.setImageResource(R.drawable.ic_circle_blue);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context.getApplicationContext(), DetailBugActivity.class);
            intent.putExtra("maBug", notification.getMaBug());
            updateNotification(notification);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (notifications == null) {
            return 0;
        }
        else return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvMessage;
        ImageView circleStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            circleStatus = itemView.findViewById(R.id.circleStatus);
        }
    }

    public void updateNotificationReadStatus(List<NotificationItem> notifications) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notificationsRef = db.collection("notifications");
        for (NotificationItem notification : notifications) {
            String id = notification.getId();
            if (id != null) {
                notificationsRef.document(id).update("isRead", true)
                        .addOnSuccessListener(aVoid -> Log.d("OK", "Notification marked as read"))
                        .addOnFailureListener(e -> Log.e("OK", "Error updating notification", e));
            }
        }
    }
    public void updateNotification(NotificationItem notification) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notificationsRef = db.collection("notifications");
        String id = notification.getId();
        if (id != null) {
            notificationsRef.document(id).update("isRead", true)
                    .addOnSuccessListener(aVoid -> Log.d("OK", "Notification marked as read"))
                    .addOnFailureListener(e -> Log.e("OK", "Error updating notification", e));
        }

    }
    public void deleteAllNotification() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("notifications");

        collectionRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                db.collection("notifications").document(document.getId()).delete()
                        .addOnSuccessListener(aVoid -> Log.d("Lỗi DB", "Document successfully deleted"))
                        .addOnFailureListener(e -> Log.w("Lỗi DB", "Error deleting document", e));
            }
        }).addOnFailureListener(e -> Log.w("Lỗi DB", "Error getting documents", e));
        notifications.clear();
        notifyDataSetChanged();
    }
}

