package com.example.project.organization;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project.R;
import com.example.project.adapter.followersAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class followersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private followersAdapter adapter;
    private List<String> followersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        followersList = new ArrayList<>();
        adapter = new followersAdapter(followersList);
        recyclerView.setAdapter(adapter);

        fetchFollowers();
    }

    private void fetchFollowers() {
        // Your logic to fetch followers and update the followersList
        // (This logic might be similar to what you have in the followersAdapter constructor)
    }
}
