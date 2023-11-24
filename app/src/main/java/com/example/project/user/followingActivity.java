package com.example.project.user;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project.R;
import com.example.project.adapter.followersAdapter;
import com.example.project.adapter.followingAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class followingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private followingAdapter adapter;
    private List<String> followingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        recyclerView = findViewById(R.id.followingrecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        followingList = new ArrayList<>();
        adapter = new followingAdapter(followingList);
        recyclerView.setAdapter(adapter);

        fetchFollowing();
    }

    private void fetchFollowing() {
        // Your logic to fetch followers and update the followersList
        // (This logic might be similar to what you have in the followersAdapter constructor)
    }
}
