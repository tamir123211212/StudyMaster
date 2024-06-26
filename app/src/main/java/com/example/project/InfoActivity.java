package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InfoActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    Spinner spinnerClass;
    EditText editTextUsername, editTextCourses1, editTextCourses2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        spinnerClass = findViewById(R.id.spinnerClass);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.class_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(adapter);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextCourses1 = findViewById(R.id.editTextCourses1);
        editTextCourses2 = findViewById(R.id.editTextCourses2);

        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {

                String selectedItem = parent.getItemAtPosition(position).toString();

                saveSelectedClass(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String username = editTextUsername.getText().toString();
                String course1 = editTextCourses1.getText().toString();
                String course2 = editTextCourses2.getText().toString();

                // Save user data to Firebase database
                saveUserData(username, course1, course2);

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveSelectedClass(String selectedClass) {
        // Get current user's  ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child("users").child(userId).child("selectedClass").setValue(selectedClass);
    }

    private void saveUserData(String username, String course1, String course2) {
        // Get current user's  ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userRef = databaseReference.child("users").child(userId);
        userRef.child("username").setValue(username);
        userRef.child("course1").setValue(course1);
        userRef.child("course2").setValue(course2);
    }
}
