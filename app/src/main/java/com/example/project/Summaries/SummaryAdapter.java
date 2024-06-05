package com.example.project.Summaries;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SummaryAdapter extends ArrayAdapter<Summary> {

    private Context context;
    private List<Summary> summaries;

    public SummaryAdapter(@NonNull Context context, List<Summary> summaries) {
        super(context, 0, summaries);
        this.context = context;
        this.summaries = summaries;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.summary_item, parent, false);
        }

        Summary summary = summaries.get(position);

        TextView textViewTitle = convertView.findViewById(R.id.text_view_title);
        TextView textViewUserName = convertView.findViewById(R.id.text_view_user_name);
        ImageView imageViewProfile = convertView.findViewById(R.id.image_view_profile);

        textViewTitle.setText(summary.getTitle());
        textViewUserName.setText(summary.getUserName());

        // שליפת תמונת הפרופיל ממסד הנתונים
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(summary.getUserId()).child("profileImageUrl");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String profileImageUrl = dataSnapshot.getValue(String.class);
                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                    Glide.with(context).load(profileImageUrl).into(imageViewProfile);
                } else {
                    imageViewProfile.setImageResource(R.drawable.baseline_account_circle_24); // תמונה זמנית
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                imageViewProfile.setImageResource(R.drawable.baseline_account_circle_24); // תמונה זמנית
            }
        });

        return convertView;
    }
}
