package com.example.a6001cem_artapp.adapters_and_classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a6001cem_artapp.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.MyHolder> {

    Context context;
    List<commentModel> comments;
    String authUserID, selectedPostID;

    public commentAdapter(Context context, List<commentModel> comments, String authUserID, String selectedPostID){
        this.context = context;
        this.comments = comments;
        this.authUserID = authUserID;
        this.selectedPostID = selectedPostID;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_individual_comment, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String uid = comments.get(position).getUserId();
        String username = comments.get(position).getUserName();
        String commentId = comments.get(position).getCommentID();
        String timestamp = comments.get(position).getCommentTimeStamp();
        String commentText = comments.get(position).getCommentText();

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String commentTime = DateFormat.format("dd/mm/yyyy hh:mm:ss", calendar).toString();

        holder.usernameTV.setText(username);
        holder.timeStampTV.setText(commentTime);
        holder.commentTV.setText(commentText);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(authUserID.equals(uid)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                    builder.setTitle("Delete");
                    builder.setMessage("Are you sure to delete this content?");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //deleteComment(commentId);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });//show dialog
                    builder.create().show();
                }
                else{
                    Toast.makeText(context, "cannot delete other user's comment", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        TextView usernameTV, commentTV, timeStampTV;

        public MyHolder(@NonNull View itemView){
            super(itemView);
            usernameTV = itemView.findViewById(R.id.usernameCommentTV);
            commentTV = itemView.findViewById(R.id.commentRowTextView);
            timeStampTV = itemView.findViewById(R.id.timeStampCommentTV);



        }
    }
}
