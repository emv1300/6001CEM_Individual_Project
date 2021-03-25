package com.example.a6001cem_artapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a6001cem_artapp.adapters_and_classes.commentAdapter;
import com.example.a6001cem_artapp.adapters_and_classes.commentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DailyChallengePostDetails extends AppCompatActivity {

    private String myUserName, postTitle, postDescription, postTimestamp,  userName, postLikes, postID, postUserPfp, postPic,numComment, position;
    private boolean processComment = false, processLike = false;
    private long lastClickTime = 0;
    private DatabaseReference postLikedRef, postsRefDataSet;

    private ImageView userPicPostIV, picPostIV;
    private TextView numCommentsTV, postTitleTV, postDescriptionTV, postTimestampTV,  userNameTV, postLikesTV, postCommentTV;
    private EditText commentET;
    private ImageButton addComment;
    private Button likeBT;
    private LinearLayout postLayout;

    List<commentModel> comments;
    commentAdapter commentAdapter;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_challenge_post_details);

        postLikedRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        postsRefDataSet = FirebaseDatabase.getInstance().getReference().child("Posts");

        Intent intent = getIntent();

        userPicPostIV = findViewById(R.id.pFpIVDetailedPost);
        picPostIV = findViewById(R.id.postImageDetailedPost);

        postTitleTV = findViewById(R.id.postTitleTVDetailedPost);
        postDescriptionTV = findViewById(R.id.postDescriptionTVDetailedPost);
        postTimestampTV = findViewById(R.id.postTimeTVDetailedPost);
        userNameTV = findViewById(R.id.userNamePostTVDetailedPost);
        postLikesTV = findViewById(R.id.likesTVDetailedPost);

        addComment = findViewById(R.id.addCommentBT);
        likeBT = findViewById(R.id.likeBTDetailedPost);
        commentET = findViewById(R.id.commentEditText);
        numCommentsTV = findViewById(R.id.commentNumTVDetailedPost);

        postLayout = findViewById(R.id.userProfileLayout);
        recyclerView = findViewById(R.id.recyclerViewComments);

        postID = intent.getStringExtra("CommentPostID");
        position = intent.getStringExtra("commentPosition");

        loadInfo(postID);

        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userName");
        tempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myUserName = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postPic.isEmpty()){

                    Intent intent = new Intent(DailyChallengePostDetails.this, DailyChallengeMain.class);
                    startActivity(intent);
                    finish();

                }else {
                    if (SystemClock.elapsedRealtime() - lastClickTime < 700){
                        return;
                    }
                    lastClickTime = SystemClock.elapsedRealtime();
                    //commentPost();
                }
            }
        });

        likeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (postPic.isEmpty()){
                    Intent intent = new Intent(DailyChallengePostDetails.this, DailyChallengeMain.class);
                    startActivity(intent);
                    finish();

                }else{
                    if (SystemClock.elapsedRealtime() - lastClickTime < 700){
                        return;
                    }
                    lastClickTime = SystemClock.elapsedRealtime();


                    processLike = true;

                    DatabaseReference postsRefDataGet = FirebaseDatabase.getInstance().getReference().child("Likes");
                    postsRefDataGet.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //PostModel snapshotS = snapshot.getValue(PostModel.class);
                            //int postLikesNum = Integer.parseInt(snapshotS.getPostLikes());

                            //int postLikesNum = (int) snapshot.getChildrenCount();
                            if(processLike) {
                                processLike = false;
                                if (snapshot.child(postID).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                                    postLikedRef.child(postID).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                    DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference().child("Likes");
                                    tempRef.child(postID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int postLikesNum = (int) snapshot.getChildrenCount();
                                            postsRefDataSet.child(postID).child("postLikes").setValue("" + (postLikesNum));

                                            likeBT.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_off, 0, 0, 0);
                                            likeBT.setText("Like");
                                            postLikesTV.setText("" + (postLikesNum) + " Likes");
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                } else {

                                    postLikedRef.child(postID).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("Liked");

                                    DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference().child("Likes");
                                    tempRef.child(postID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int postLikesNum = (int) snapshot.getChildrenCount();
                                            postsRefDataSet.child(postID).child("postLikes").setValue("" + (postLikesNum));

                                            likeBT.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_on, 0, 0, 0);
                                            likeBT.setText("Liked");
                                            postLikesTV.setText("" + (postLikesNum) + " Likes");
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        }@Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }}
        });

    }

    private void loadPostComments(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(linearLayoutManager);

        comments = new ArrayList<>();

        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference("Posts").child(postID).child("postComments");
        tempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    commentModel commentModel = ds.getValue(commentModel.class);

                    comments.add(commentModel);
                    commentAdapter = new commentAdapter(getApplicationContext(), comments, FirebaseAuth.getInstance().getUid(), postID);
                    recyclerView.setAdapter(commentAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setLikes( String pKey) {
        postLikedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(pKey).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    likeBT.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_on, 0,0,0);
                    likeBT.setText("Liked");

                }else{
                    likeBT.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_off, 0,0,0);
                    likeBT.setText("Like");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadInfo(String commentPostID) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        Query editPostSearch = reference.orderByChild("postID").equalTo(commentPostID);
        editPostSearch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    postTitle = ds.child("postTitle").getValue().toString();
                    postDescription = ds.child("postDescription").getValue().toString();
                    postPic = ds.child("postImage").getValue().toString();
                    //postTimestamp,  userName, postLikes, postID, userPfp, postPic, likes
                    postTimestamp = ds.child("postTimestamp").getValue().toString();
                    userName = ds.child("userName").getValue().toString();
                    postLikes = ds.child("postLikes").getValue().toString();
                    postUserPfp = ds.child("userPfp").getValue().toString();
                    numComment = ds.child("postCommentsNum").getValue().toString();
                    //String commentCount = ds.child("postComment").getValue().toString();

                    postTitleTV.setText(postTitle);
                    postDescriptionTV.setText(postDescription);
                    Picasso.get().load(postPic).into(picPostIV);
                    Picasso.get().load(postUserPfp).into(userPicPostIV);



                    postTimestampTV.setText(postTimestamp);
                    userNameTV.setText(userName);
                    postLikesTV.setText(postLikes+" Likes");

                    numCommentsTV.setText(numComment+" Comments");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();

    }
}