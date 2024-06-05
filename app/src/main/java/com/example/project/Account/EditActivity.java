package com.example.project.Account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.HomeActivity;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditActivity extends AppCompatActivity {
    private CircleImageView imageViewProfile;
    private Button buttonCancel, buttonSave;
    private TextView textViewChangeProfileBtn;
    private FirebaseAuth mAuth;
    private Uri imageUri;
    private String myUri = "";
    private UploadTask uploadTask;
    private StorageReference storageProfilePicsRef;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit);

        mAuth = FirebaseAuth.getInstance();
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("profile_pictures");
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());

        // Find views
        imageViewProfile = findViewById(R.id.imageViewProfile);
        textViewChangeProfileBtn = findViewById(R.id.textViewChangeProfileBtn);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonSave = findViewById(R.id.buttonSave);

        // Set OnClickListener for the Cancel button
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditActivity.this, HomeActivity.class));
            }
        });

        // Set OnClickListener for the Save button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfileImage();
            }
        });

        // Set OnClickListener for the Change Profile Picture text
        textViewChangeProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        // Set OnClickListener for the profile image
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        resultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    imageViewProfile.setImageURI(imageUri);
                }
            });

    private void uploadProfileImage() {
        if (imageUri != null) {
            StorageReference fileReference = storageProfilePicsRef.child(mAuth.getCurrentUser().getUid() + "." + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    myUri = downloadUri.toString();

                    // Save the image URL to Firebase Realtime Database under the current user
                    userRef.child("profileImageUrl").setValue(myUri).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditActivity.this, "Profile image uploaded successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditActivity.this, HomeActivity.class));
                            } else {
                                Toast.makeText(EditActivity.this, "Failed to upload profile image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // Handle failure
                    Toast.makeText(EditActivity.this, "Failed to upload profile image", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                // Handle failure
                Toast.makeText(EditActivity.this, "Failed to upload profile image", Toast.LENGTH_SHORT).show();
            });
        } else {
            // Handle no file selected
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }
}
