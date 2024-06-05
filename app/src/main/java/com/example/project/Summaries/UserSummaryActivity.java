package com.example.project.Summaries;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class UserSummaryActivity extends AppCompatActivity {

    private ListView listViewSummaries;
    private List<Summary> summaryList;
    private SummaryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_summary);

        listViewSummaries = findViewById(R.id.list_view_summaries);
        Button btnBack = findViewById(R.id.btn_back);
        summaryList = new ArrayList<>();
        adapter = new SummaryAdapter(this, summaryList);
        listViewSummaries.setAdapter(adapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("summaries");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                summaryList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Summary summary = snapshot.getValue(Summary.class);
                    summaryList.add(summary);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // סיום הפעילות הנוכחית וחזרה לפעילות הקודמת
            }
        });

        listViewSummaries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Summary summary = summaryList.get(position);
                Intent intent = new Intent(UserSummaryActivity.this, ImageViewActivity.class);
                intent.putExtra("imageUrl", summary.getImageUrl());
                startActivity(intent);
            }
        });
    }
}
