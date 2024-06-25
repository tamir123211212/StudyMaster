package com.example.project.Chat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.HomeActivity;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private ImageButton imageButtonSend;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private DatabaseReference databaseMessages;
    private DatabaseReference databaseUsers;
    private FirebaseAuth auth;
    private TextView textViewChatName;
    private TextView textViewChatTopic;
    private Button back;
    private String chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);

        textViewChatName = findViewById(R.id.textViewName);
        textViewChatTopic = findViewById(R.id.textViewTopic);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        imageButtonSend = findViewById(R.id.imageButtonSend);
        back = findViewById(R.id.back);


        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerViewMessages.setAdapter(messageAdapter);

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();

        // Get chat details from intent
        chatId = getIntent().getStringExtra("chatId");
        String chatName = getIntent().getStringExtra("chatName");
        String chatTopic = getIntent().getStringExtra("chatTopic");

        textViewChatName.setText(chatName);
        textViewChatTopic.setText(chatTopic);

        // Reference to the messages for the specific chat
        databaseMessages = FirebaseDatabase.getInstance().getReference("chats").child(chatId).child("messages");

        imageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        back.setOnClickListener(v -> finish());



        databaseMessages.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Message message = postSnapshot.getValue(Message.class);
                    messageList.add(message);
                }
                // Sorting messages by timestamp
                Collections.sort(messageList, new Comparator<Message>() {
                    @Override
                    public int compare(Message m1, Message m2) {
                        return Long.compare(m1.getTimestamp(), m2.getTimestamp());
                    }
                });
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void sendMessage() {
        String content = editTextMessage.getText().toString().trim();
        if (!content.isEmpty()) {
            String id = databaseMessages.push().getKey();
            String senderId = auth.getCurrentUser().getUid();
            long timestamp = System.currentTimeMillis();

            // Fetch user details
            databaseUsers.child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String senderName = dataSnapshot.child("username").getValue(String.class);
                    String senderProfileImage = dataSnapshot.child("profileImageUrl").getValue(String.class);

                    Log.d("ChatDetailsActivity", "Sender Name: " + senderName);
                    Log.d("ChatDetailsActivity", "Sender Profile Image URL: " + senderProfileImage);

                    if (senderProfileImage == null) {
                        Log.e("ChatDetailsActivity", "Profile image URL is null");
                    }

                    Message message = new Message(id, content, senderId, senderName, senderProfileImage, timestamp);
                    if (id != null) {
                        databaseMessages.child(id).setValue(message);
                        editTextMessage.setText("");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("ChatDetailsActivity", "Failed to fetch user details", databaseError.toException());
                }
            });
        }
    }
}

