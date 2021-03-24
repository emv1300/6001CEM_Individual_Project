package com.example.a6001cem_artapp.adapters_and_classes;

import android.content.Context;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a6001cem_artapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class postAdapter extends RecyclerView.Adapter<postAdapter.MyHolder> {

    private Context context;
    private List<postModel> postList;
    private String authUserID;
    private DatabaseReference postLikesRef, postsRefDataSet;
    private boolean processLike = false, processReport = false;
    private long lastClickTime = 0, lastClickTimeReport = 0;

    public postAdapter(Context context, List<postModel> postList) {
        this.context = context;
        this.postList = postList;
        authUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        postLikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        postsRefDataSet = FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_individual_post, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String pId = postList.get(position).getPostID();

        String pTitle = postList.get(position).getPostTitle();

        String pDescr = postList.get(position).getPostDescription();

        String pImage = postList.get(position).getPostImage();

        String pTimeStamp = postList.get(position).getPostTimestamp();

        String uId = postList.get(position).getUserID();

        String uPfp = postList.get(position).getUserPfp();

        String uName = postList.get(position).getUserName();

        String postLikes = postList.get(position).getPostLikes();

        String commentsNum = postList.get(position).getPostCommentsNum();

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime = DateFormat.format("dd/mm/yyyy hh:mm:ss", calendar).toString();

        setLikes(holder, pId);

        holder.userNameTV.setText(uName);
        holder.postTitleTV.setText(pTitle);
        holder.postDescriptionTV.setText(pDescr);
        holder.postTimeTV.setText(pTime);
        holder.postLikesTV.setText(postLikes+" Likes");
        holder.commentsCountTV.setText(commentsNum+" Comments");

        try{
            Picasso.get().load(uPfp).placeholder(R.drawable.ic_launcher_foreground).into(holder.userPfpIV);
        }catch (Exception e){

        }

        try{
            Picasso.get().load(pImage).placeholder(R.drawable.ic_launcher_foreground).into(holder.postImageIV);
        }catch (Exception e){

        }

        holder.likeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                    processLike = true;
                    return;
                }else {
                    lastClickTime = SystemClock.elapsedRealtime();
                }
                DatabaseReference quickRef = FirebaseDatabase.getInstance().getReference("Posts").child(pId);

                quickRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("postImage").getValue()!= null){

                            final String postIdLiked = postList.get(position).getPostID();
                            DatabaseReference postsRefDataGet = FirebaseDatabase.getInstance().getReference().child("Likes");
                            postsRefDataGet.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(processLike) {
                                        processLike = false;
                                        if (snapshot.child(postIdLiked).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                                            postLikesRef.child(postIdLiked).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                            DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference().child("Likes");
                                            tempRef.child(postIdLiked).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    int postLikesNum = (int) snapshot.getChildrenCount();
                                                    postsRefDataSet.child(postIdLiked).child("postLikes").setValue("" + (postLikesNum));

                                                    holder.likeBT.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_off, 0, 0, 0);
                                                    holder.likeBT.setText("Like");
                                                    holder.postLikesTV.setText("" + (postLikesNum) + " Likes");
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        } else {

                                            postLikesRef.child(postIdLiked).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("Liked");

                                            DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference().child("Likes");
                                            tempRef.child(postIdLiked).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    int postLikesNum = (int) snapshot.getChildrenCount();
                                                    postsRefDataSet.child(postIdLiked).child("postLikes").setValue("" + (postLikesNum));

                                                    holder.likeBT.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_on, 0, 0, 0);
                                                    holder.likeBT.setText("Liked");
                                                    holder.postLikesTV.setText("" + (postLikesNum) + " Likes");
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }else{
                            Toast.makeText(context, "post was deleted!", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

    }

    private void setLikes(MyHolder holder, String pKey) {
        postLikesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(pKey).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    holder.likeBT.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_on, 0,0,0);
                    holder.likeBT.setText("Liked");

                }else{
                    holder.likeBT.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_off, 0,0,0);
                    holder.likeBT.setText("Like");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView userPfpIV, postImageIV;
        TextView userNameTV, postTimeTV, postTitleTV, postDescriptionTV, postLikesTV, commentsCountTV;
        ImageButton moreBT;
        Button likeBT,commentBT;

        public MyHolder(@NonNull View itemView){
            super((itemView));

            userPfpIV = itemView.findViewById(R.id.pFpIV);
            postImageIV = itemView.findViewById(R.id.postImage);
            userNameTV = itemView.findViewById(R.id.userNamePostTV);
            postTimeTV = itemView.findViewById(R.id.postTimeTV);
            postTitleTV = itemView.findViewById(R.id.postTitleTV);
            postDescriptionTV = itemView.findViewById(R.id.postDescriptionTV);
            postLikesTV = itemView.findViewById(R.id.likesTV);
            moreBT = itemView.findViewById(R.id.moreBT);
            likeBT = itemView.findViewById(R.id.likeBT);
            commentBT = itemView.findViewById(R.id.commentBT);
            commentsCountTV = itemView.findViewById(R.id.commentNumTV);
        }
    }
}
