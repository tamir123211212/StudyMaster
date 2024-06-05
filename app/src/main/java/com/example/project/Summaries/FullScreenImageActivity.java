package com.example.project.Summaries;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.project.R;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        // Add back button
        Button buttonBack = findViewById(R.id.button_back_full_screen);
        buttonBack.setOnClickListener(v -> finish());

        ImageView imageView = findViewById(R.id.full_screen_image_view);
        String imageUrl = getIntent().getStringExtra("image_url");

        if (imageUrl != null) {
            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView);
        }
    }
}
