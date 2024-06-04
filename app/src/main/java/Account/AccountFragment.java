package Account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.project.MainActivity;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {
    private TextView textViewUsername, textViewValue, textViewDescriptionValue, textViewClassValue;
    private ImageView imageViewProfile; // ImageView for user's profile picture

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Initialize TextViews
        textViewUsername = view.findViewById(R.id.textViewUsernameValue);
        textViewValue = view.findViewById(R.id.textViewValue);
        textViewClassValue = view.findViewById(R.id.textViewClassValue);
        textViewDescriptionValue = view.findViewById(R.id.textViewDescriptionValue);
        imageViewProfile = view.findViewById(R.id.imageViewProfile); // Link to ImageView

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // Initialize Firebase Database
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());

            // Retrieve data from Firebase Realtime Database
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Get user data
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String course1 = dataSnapshot.child("course1").getValue(String.class);
                    String course2 = dataSnapshot.child("course2").getValue(String.class);
                    String classValue = dataSnapshot.child("selectedClass").getValue(String.class);

                    // Set data to TextViews
                    textViewUsername.setText(username);
                    textViewValue.setText(course1);
                    textViewDescriptionValue.setText(course2);
                    textViewClassValue.setText(classValue);

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
                    // Handle database error
                }
            });
        }

        Button buttonChangeProfilePicture = view.findViewById(R.id.buttonChangeProfilePicture);
        buttonChangeProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to Activity/EditActivity
                Intent intent = new Intent(getActivity(), EditActivity.class);
                // Start the intent to move to the next screen
                startActivity(intent);
            }
        });

        // Initialize the logout button
        Button buttonLogout = view.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return view;
    }

    // Method to log out the user
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        // Redirect the user to the MainActivity
        startActivity(new Intent(getActivity(), MainActivity.class));
        requireActivity().finish();
    }
}
