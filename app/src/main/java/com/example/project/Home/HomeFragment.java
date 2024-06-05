package com.example.project.Home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.Dates.DatesFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private FirebaseAuth mAuth;
    private ImageView imageViewProfile;
    private DatabaseReference mDatabase;
    private TextView TvClass, Tvcourse1, Tvcourse2, TvUsername;
    private Button buttonAddReminder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        TvClass = view.findViewById(R.id.TvClass);
        Tvcourse1 = view.findViewById(R.id.Tvcourse1);
        Tvcourse2 = view.findViewById(R.id.Tvcourse2);
        TvUsername = view.findViewById(R.id.TvUsername);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);



        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // Initialize Firebase Database
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());

            // Retrieve user data
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "User data snapshot: " + dataSnapshot.toString());
                    if (!isAdded()) return; // Check if fragment is still attached

                    // Get user data
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String course1 = dataSnapshot.child("course1").getValue(String.class);
                    String course2 = dataSnapshot.child("course2").getValue(String.class);
                    String classValue = dataSnapshot.child("selectedClass").getValue(String.class);

                    // Set data to TextViews
                    TvUsername.setText(username);
                    Tvcourse1.setText(course1);
                    Tvcourse2.setText(course2);
                    TvClass.setText(classValue);

                    // Set user profile image if available
                    String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        // Load the image into the ImageView
                        Glide.with(requireContext())
                                .load(profileImageUrl)
                                .into(imageViewProfile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "Failed to read user data", databaseError.toException());
                }
            });
        }

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
