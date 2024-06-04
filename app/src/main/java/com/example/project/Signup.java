package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button buttonSignup;
    private EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignup = findViewById(R.id.buttonSignup);
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // ההרשמה הצליחה, עכשיו ניתן להוסיף למסד הנתונים שלך את המשתמש
                                    Toast.makeText(Signup.this, "ההרשמה הצליחה.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), InfoActivity.class);
                                    startActivity(i);
                                    String userId = mAuth.getCurrentUser().getUid();
                                    // אתה יכול לשמור אותו במסד הנתונים של Firebase או להשתמש בו כרצונך
                                } else {
                                    // ההרשמה נכשלה
                                    Toast.makeText(Signup.this, "ההרשמה נכשלה. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
