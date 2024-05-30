package com.camuyen.quanlybug.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.camuyen.quanlybug.R;
import com.camuyen.quanlybug.firebase.DBQuanLyBug;
import com.camuyen.quanlybug.model.Bugs;
import com.camuyen.quanlybug.model.Comments;
import com.camuyen.quanlybug.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    List<Comments> list;
    Context context;
    DBQuanLyBug dbQuanLyBug;

    public CommentAdapter(List<Comments> list, Context context) {
        this.list = list;
        this.context = context;
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
        dbQuanLyBug.getUserInfor(new DBQuanLyBug.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                if (user.getMaNhanVien().startsWith("QL")){
                    holder.imgMoreComment.setVisibility(View.VISIBLE);
                } else {
                    holder.imgMoreComment.setVisibility(View.GONE);
                }
            }
        });
        dbQuanLyBug.getUsers(new DBQuanLyBug.UCallBack() {
            @Override
            public void onULoaded(List<User> users) {
                for(User user : users){
                    if(user.getMaNhanVien().equals(comments.getMaNhanVien())){
                        holder.txtNameComment.setText(user.getHoTen());

                        String path = "Images/" + user.getUid() + ".jpg";
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(path);
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(holder.imgComment);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

        holder.txtComment.setText(comments.getNoiDung());
        holder.imgMoreComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, comments, holder.getAbsoluteAdapterPosition());
                System.out.println("Kiêểm tra khi click: " + comments.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgComment, imgMoreComment;
        TextView txtNameComment, txtComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgComment = itemView.findViewById(R.id.imgComment);
            txtNameComment = itemView.findViewById(R.id.txtNameComment);
            txtComment = itemView.findViewById(R.id.txtComment);
            imgMoreComment = itemView.findViewById(R.id.imgMoreComment);
        }
    }
    private void showPopupMenu(View v, Comments comment, int position){
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_comment, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.mnuSuaComment){
                    getComments(comment.getMaComment(), v.getContext(), position);
                    System.out.println("Position: " + comment.getMaComment());
                }
                if (item.getItemId() == R.id.mnuXoaComment){
                    Toast.makeText(v.getContext(), "Delete", Toast.LENGTH_SHORT).show();
                    deleteComment(comment.getMaComment(), v.getContext());
                    list.remove(position);
                    notifyDataSetChanged();
                }
                return true;
            }
        });

        popupMenu.show();
    }
    public String convertSoComment(int number) {
        return String.format("%03d", number);
    }
    public void getComments(String maComment, Context context, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String maBug = preferences.getString("maBugComment", "");
        System.out.println(maBug);
        System.out.println(maComment);
        CollectionReference ref = db.collection("bugs").document(maBug).collection("comments");
        ref.document(maComment).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Comments comments = document.toObject(Comments.class);
                        if (comments != null) {
                            showEditDialog(comments, position);
                        } else {
                            Log.d("GetComments", "No comments found");
                        }
                    } else {
                        Log.d("GetComments", "No such document");
                    }
                } else {
                    Log.e("GetComments", "Task failed with ", task.getException());
                }
            }
        });
    }
    public void deleteComment(String maComment, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String maBug = preferences.getString("maBugComment", "");
        Log.d("DeleteComment", "maBug: " + maBug);

        CollectionReference ref = db.collection("bugs").document(maBug).collection("comments");
        ref.document(maComment).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("DeleteComment", "Comment deleted successfully");
                } else {
                    Log.e("DeleteComment", "Error deleting comment: ", task.getException());
                }
            }
        });
    }
    public void updateComment(Comments comments, Context context){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String maBug = preferences.getString("maBugComment", "");
        Log.d("UpdateComment", "maBug: " + maBug);

        CollectionReference ref = db.collection("bugs").document(maBug).collection("comments");
        ref.document(comments.getMaComment()).update("noiDung", comments.getNoiDung()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Comment updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("UpdateComment", "Error updating comment: ", task.getException());
                }
            }
        });

    }
    public void showEditDialog(Comments comments, int position){
        // Inflate the dialog layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_info, null);

        // Get reference to the TextInputEditText in the dialog
        final TextInputEditText editTextContent = dialogView.findViewById(R.id.editTextContent);
        editTextContent.setText(comments.getNoiDung()); // Hiển thị nội dung hiện tại

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView).setTitle("Edit Content").setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Lưu nội dung mới
                        String contentEdit = editTextContent.getText().toString();
                        comments.setNoiDung(contentEdit);
                        // Thực hiện các hành động khác cần thiết
                        updateComment(comments, context);
                        list.get(position).setNoiDung(contentEdit);
                        notifyDataSetChanged();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Đóng dialog mà không làm gì
                        dialog.dismiss();
                    }
                });
        // Hiển thị dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
