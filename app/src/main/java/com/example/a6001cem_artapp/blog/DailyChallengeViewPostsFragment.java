package com.example.a6001cem_artapp.blog;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.a6001cem_artapp.R;
import com.example.a6001cem_artapp.adapters_and_classes.postModel;
import com.example.a6001cem_artapp.adapters_and_classes.postAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DailyChallengeViewPostsFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<postModel> postList;
    private postAdapter postAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public DailyChallengeViewPostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_challenge_view_posts, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipePostsRefresh);
        recyclerView = view.findViewById(R.id.postsRV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        postList = new ArrayList<>();

        loadAllPosts();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAllPosts();
                if(swipeRefreshLayout.isRefreshing()) {

                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        return view;}

    private void loadAllPosts() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Posts");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){

                    if (ds!=null){
                    postModel postModel = ds.getValue(postModel.class);
                    String numberOfReports = postModel.getReportsNum();
                    if (ds.child("postImage").getValue() != null && numberOfReports != null && Integer.parseInt(numberOfReports)<3) {
                        postList.add(postModel);
                    }}
                    postAdapter = new postAdapter(getActivity(), postList);
                    recyclerView.setAdapter(postAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void searchOwnPosts(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Posts");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("postImage").getValue()!=null){
                        postModel postModel = ds.getValue(postModel.class);
                        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        if(postModel.getUserID().contains(userID))
                        { postList.add(postModel);}}
                    postAdapter = new postAdapter(getActivity(), postList);
                    recyclerView.setAdapter(postAdapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void searchSpecificPosts(final String sQ) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Posts");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("postImage").getValue()!=null) {
                        postModel postModel = ds.getValue(postModel.class);
                        String numberOfReports = ds.child("reportsNum").getValue().toString();
                        if (Integer.parseInt(numberOfReports)< 2 && (postModel.getPostTitle().toLowerCase().contains(sQ.toLowerCase())
                                || postModel.getPostDescription().toLowerCase().contains(sQ.toLowerCase()) || postModel.getUserName().toLowerCase().contains(sQ.toLowerCase()))) {
                            postList.add(postModel);
                        }
                    }
                    postAdapter = new postAdapter(getActivity(), postList);
                    recyclerView.setAdapter(postAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostLikedPosts(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Posts");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if(ds.child("postImage").getValue()!=null) {
                        postModel postModel = ds.getValue(postModel.class);
                        String numberOfReports = ds.child("reportsNum").getValue().toString();
                        if (Integer.parseInt(numberOfReports)< 2 && Integer.parseInt(postModel.getPostLikes())>5) {
                            postList.add(postModel);
                        }
                    }
                    postAdapter = new postAdapter(getActivity(), postList);
                    recyclerView.setAdapter(postAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    @SuppressLint("RestrictedApi")
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.menu_view_posts, menu);
        MenuItem item1 = menu.findItem(R.id.search_posts);
        SearchView searchView = (SearchView) item1.getActionView();
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //called when the user presses search bt
                if (!TextUtils.isEmpty(query)){
                    searchSpecificPosts(query);
                }else{
                    loadAllPosts();
                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (!TextUtils.isEmpty(newText)){
                    searchSpecificPosts(newText);
                }else{
                    loadAllPosts();
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.seeOwnPostsItem) {
            searchOwnPosts();
        }
        if (id == R.id.seeMostLikedPostItem){
            mostLikedPosts();
        }
        return super.onOptionsItemSelected(item);
    }
}
/*postModel.getPostLikes().contains("25") || postModel.getPostLikes().contains("26")
                                || postModel.getPostLikes().contains("27") || postModel.getPostLikes().contains("28") ||
                                postModel.getPostLikes().contains("29") || postModel.getPostLikes().contains("30")*/