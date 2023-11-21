package com.example.project.LocationOwner;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.project.R;
import com.example.project.organization.Vacancy;
import com.example.project.organization.vacancyAdapter;
import com.example.project.userDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userManageFragment extends Fragment {
    private DatabaseReference usersReference;
    private DatabaseReference vacanciesReference;
    private RecyclerView recyclerView;
    private vacancyAdapter adapter;

    private ImageView backArrow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_manage, container, false);

        // Find the ImageView with ID backArrow in the inflated view
        backArrow = view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),userDashboard.class);
                startActivity(intent);
            }
        });


        return view;
    }
}
