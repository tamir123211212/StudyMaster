package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText Etemail, Etpass;
    private Button Btsignin, Btcreate;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in, move to HomeActivity
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Optional: close this activity to prevent going back to it when pressing back button
        }

        // Initialize EditText and Button
        Etemail = findViewById(R.id.Etemail);
        Btcreate = findViewById(R.id.Btcreate);
        Etpass = findViewById(R.id.Etpass);
        Btsignin = findViewById(R.id.Btsignin);

        Btcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Signup.class);
                startActivity(i);
            }
        });

        // Set click listener for the sign in button
        Btsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Etemail.getText().toString().trim();
                String password = Etpass.getText().toString().trim();

                if (email.isEmpty()) {
                    Etemail.setError("Email is required");
                    Etemail.requestFocus();
                } else if (password.isEmpty()) {
                    Etpass.setError("Password is required");
                    Etpass.requestFocus();
                } else {
                    signInUser(email, password);
                }
            }
        });
    }

    // Method to sign in user
    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();
                            // Move to HomeActivity upon successful sign in
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish(); // Close this activity to prevent user from coming back to it with back button
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
