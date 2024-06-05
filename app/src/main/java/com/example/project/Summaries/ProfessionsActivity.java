package com.example.project.Summaries;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfessionsActivity extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    private LinearLayout imagesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professions);

        // Add back button
        Button buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(v -> finish());

        String subject = getIntent().getStringExtra("subject");

        // Apply edge-to-edge window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button_upload_summary = findViewById(R.id.button_upload_summary);
        imagesContainer = findViewById(R.id.images_container);
        Spinner spinner = findViewById(R.id.spinner_summaries);
        Button button_user_summaries = findViewById(R.id.button_user_summaries);
        button_user_summaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfessionsActivity.this, UserSummaryActivity.class);
                startActivity(intent);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                imagesContainer.removeAllViews();
                button_upload_summary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProfessionsActivity.this, UploadSummaryActivity.class);
                        startActivity(intent);
                    }
                });

                StorageReference imagesRef = null;
                if ("Citizen".equals(subject)) {
                    switch (position) {
                        case 0:
                            imagesRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/beta1-dcf9e.appspot.com/o/summary_images%2FCitizen%2F%D7%9E%D7%92%D7%99%D7%9C%D7%AA%20%D7%94%D7%A2%D7%A6%D7%9E%D7%90%D7%95%D7%AA.webp?alt=media&token=0b15a674-5fb1-4cd0-8f44-36e1f056c355");
                            break;
                        case 1:
                            imagesRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/beta1-dcf9e.appspot.com/o/summary_images%2FCitizen%2F%D7%90%D7%96%D7%A8%D7%97%D7%95%D7%AA3.jpg?alt=media&token=959c4f89-a467-4429-94ec-57dc6cdd9f41");
                            break;
                        case 2:
                            imagesRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/beta1-dcf9e.appspot.com/o/summary_images%2FCitizen%2F%D7%9E%D7%92%D7%99%D7%9C%D7%AA%20%D7%94%D7%A2%D7%A6%D7%9E%D7%90%D7%95%D7%AA.webp?alt=media&token=0b15a674-5fb1-4cd0-8f44-36e1f056c355");
                            break;
                    }
                } else if ("Math".equals(subject)) {
                    switch (position) {
                        case 0:
                            imagesRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/beta1-dcf9e.appspot.com/o/summary_images%2Fmath%2Fmath.jpeg?alt=media&token=f832c1fd-d339-468e-bf2d-e5faa982ac47");
                            break;
                        case 1:
                            imagesRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/beta1-dcf9e.appspot.com/o/summary_images%2Fmath%2Fmath2.jpeg?alt=media&token=d81f0b96-3383-4eb8-a2a0-de987ea60844");
                            break;
                        case 2:
                            imagesRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/beta1-dcf9e.appspot.com/o/summary_images%2Fmath%2Fmath3.jpeg?alt=media&token=0df47aae-0fe4-4dd3-977f-ef7eca69ade7");
                            break;
                    }
                } else if ("English".equals(subject)) {
                    switch (position) {
                        case 0:
                            imagesRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/beta1-dcf9e.appspot.com/o/summary_images%2Fenglish%2Feng.jpeg?alt=media&token=820fc768-0ce5-402c-8211-2aca6c3ccc39");
                            break;
                        case 1:
                            imagesRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/beta1-dcf9e.appspot.com/o/summary_images%2Fenglish%2Feng2.jpeg?alt=media&token=c83ce921-4eac-4749-b7a2-666c8835b8ae");
                            break;
                        case 2:
                            imagesRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/beta1-dcf9e.appspot.com/o/summary_images%2Fenglish%2Feng3.jpeg?alt=media&token=69fb4a31-9d14-4216-9f36-335fbd1735db");
                            break;
                    }
                }

                if (imagesRef != null) {
                    ImageView imageView = new ImageView(ProfessionsActivity.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            500 // height in pixels, adjust as needed
                    );
                    imageView.setLayoutParams(layoutParams);
                    imagesContainer.addView(imageView);

                    imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Glide.with(ProfessionsActivity.this)
                                .load(uri)
                                .into(imageView);
                        imageView.setOnClickListener(v -> {
                            Intent fullScreenIntent = new Intent(ProfessionsActivity.this, FullScreenImageActivity.class);
                            fullScreenIntent.putExtra("image_url", uri.toString());
                            startActivity(fullScreenIntent);
                        });
                    }).addOnFailureListener(exception -> {
                        // Handle any errors
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
}
