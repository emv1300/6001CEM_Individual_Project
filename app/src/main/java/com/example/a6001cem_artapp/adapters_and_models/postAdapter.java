package com.example.a6001cem_artapp.adapters_and_models;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a6001cem_artapp.social_media.DailyChallengePostDetails;
import com.example.a6001cem_artapp.social_media.DailyChallengeUploadPost;
import com.example.a6001cem_artapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
    private long lastClickTime = 0;

    public postAdapter(Context context, List<postModel> postList) {
        this.context = context;
        this.postList = postList;
        authUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        postLikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        postsRefDataSet = FirebaseDatabase.getInstance().getReference().child("Posts");
        lastClickTime = SystemClock.elapsedRealtime();
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
        if (pDescr.length()>200){
        pDescr = pDescr.substring(0,Math.min(pDescr.length(), 200));
        pDescr = pDescr+"...";}
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


                DatabaseReference quickRef = FirebaseDatabase.getInstance().getReference("Posts").child(pId);

                processLike = true;
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

        holder.moreBT.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                showMorePostOptions(holder.moreBT, uId, authUserID, pImage,pId, position);
            }
        });

        holder.commentBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, DailyChallengePostDetails.class);
                DatabaseReference tempCheckRef = FirebaseDatabase.getInstance().getReference();
                tempCheckRef.child("Posts").child(pId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue()!=null){
                            intent.putExtra("CommentPostID", pId);
                            intent.putExtra("commentPosition", position+"");
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        return;
                    }
                });
            }
        });
        holder.postImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, DailyChallengePostDetails.class);
                DatabaseReference tempCheckRef = FirebaseDatabase.getInstance().getReference();
                tempCheckRef.child("Posts").child(pId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue()!=null){
                            intent.putExtra("CommentPostID", pId);
                            intent.putExtra("commentPosition", position+"");
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        return;
                    }
                });

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showMorePostOptions(ImageButton moreBT, String uid, String authUserID, String pImage,String pID, Integer position) {
        //creates menu for posts, with option to delete, edit and report a post
        PopupMenu popupMenu = new PopupMenu(context, moreBT, Gravity.END);

        if (authUserID.equals(uid)){
            popupMenu.getMenu().add(Menu.NONE, 0, 0,"Delete");
            popupMenu.getMenu().add(Menu.NONE, 1,0,"Edit");
        }
        popupMenu.getMenu().add(Menu.NONE,2, 0, "Details");
        popupMenu.getMenu().add(Menu.NONE,3, 0, "Report post!");


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == 0){
                    deletePost(pID, pImage);
                }
                if (id == 1){
                    Intent intent= new Intent(context, DailyChallengeUploadPost.class);
                    DatabaseReference tempCheckRef = FirebaseDatabase.getInstance().getReference();
                    tempCheckRef.child("Posts").child(pID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue()!=null){

                                intent.putExtra("keyEdit", "editPost");
                                intent.putExtra("editPostID", pID);
                                context.startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            return;
                        }
                    });

                }
                if (id == 2){
                    Intent intent= new Intent(context, DailyChallengePostDetails.class);
                    DatabaseReference tempCheckRef = FirebaseDatabase.getInstance().getReference();
                    tempCheckRef.child("Posts").child(pID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue()!=null){
                                intent.putExtra("CommentPostID", pID);
                                intent.putExtra("commentPosition", position+"");
                                context.startActivity(intent);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            return;
                        }
                    });/*
                    intent.putExtra("CommentPostID", pID);
                    intent.putExtra("commentPosition", position+"");
                    context.startActivity(intent);*/
                }
                if (id == 3){
                    processReport = true;
                    reportPost(pID,position);
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void deletePost(String pID, String pImage){


        StorageReference picRef = FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
        DatabaseReference delRef = FirebaseDatabase.getInstance().getReference("Posts");

        DatabaseReference delLikes = FirebaseDatabase.getInstance().getReference("Likes");

        delRef.child(pID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot!=null) {snapshot.getRef().removeValue();}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        delLikes.child(pID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot!=null) {snapshot.getRef().removeValue();}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        picRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    delRef.child("Posts").child(pID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot!=null) {snapshot.getRef().removeValue();}
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        /*
        picRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Query delQuery = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("postID").equalTo(pID);
                        delQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot ds: snapshot.getChildren() ){
                                    ds.getRef().removeValue();
                                    delLikes.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            snapshot.child(pID).getRef().removeValue();

                                            delRepRef.child("Reports").child(pID).removeValue()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(context, "Deleted Successfully!", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError  error) {
                                Toast.makeText(context, "Something went wrong with updating the database", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Something went wrong with updating the cloud storage", Toast.LENGTH_LONG).show();
            }
        });
*/


    }

    private void reportPost(String pID, int position){
        DatabaseReference quickRef = FirebaseDatabase.getInstance().getReference("Posts").child(pID);

        quickRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("postImage").getValue()!= null){
                    final String postIdReported = postList.get(position).getPostID();
                    DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference().child("Reports");
                    reportsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(processReport) {
                                processReport = false;
                                if (snapshot.child(postIdReported).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    Toast.makeText(context, "you already reported this post!",Toast.LENGTH_LONG).show();
                                } else {

                                    reportsRef.child(postIdReported).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("Reported");

                                    DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference().child("Reports");
                                    tempRef.child(postIdReported).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int postReportsNum = (int) snapshot.getChildrenCount();
                                            postsRefDataSet.child(postIdReported).child("reportsNum").setValue("" + (postReportsNum));
                                            Toast.makeText(context, "post reported!",Toast.LENGTH_LONG).show();
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
