package com.example.project.LocationOwner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.example.project.user.userLogin;
import com.google.android.material.textfield.TextInputLayout;

public class userProfileFragment extends Fragment {

    TextView fullname, email,setting;
    TextInputLayout age, gender, mobile, skills;
    ImageView back_button,settinga;
    Button button;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

//        button = view.findViewById(R.id.button5);

        settinga = view.findViewById(R.id.imageView3);
        back_button = view.findViewById(R.id.imageView16);
//        age = view.findViewById(R.id.textView3);
//        gender = view.findViewById(R.id.textView0);
//        mobile = view.findViewById(R.id.textView22);
//        skills = view.findViewById(R.id.textView8);

        // Uncomment the following line once you have implemented the 'gettingdata()' method
//        gettingdata();

//        Button btnLogout = view.findViewById(R.id.button5);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),userDashboard.class);
                startActivity(i);

            }
        });
        settinga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), user_setting.class);
                startActivity(intent);

            }
        });

        return view;
    }
}