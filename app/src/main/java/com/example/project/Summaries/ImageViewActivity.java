package com.example.project.Summaries;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.project.R;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        ImageView imageView = findViewById(R.id.image_view);
        Button btnBack = findViewById(R.id.btn_back);

        String imageUrl = getIntent().getStringExtra("imageUrl");
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.baseline_account_circle_24)
                    .into(imageView);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // סיים את הפעילות הנוכחית וחזור לפעילות הקודמת
            }
        });
    }
}
