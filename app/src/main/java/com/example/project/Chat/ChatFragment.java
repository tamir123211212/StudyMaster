package com.example.project.Chat;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<Chat> chatList;
    private DatabaseReference databaseChats;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatList = new ArrayList<>();
        // Initialize the adapter and set it to the RecyclerView
        chatAdapter = new ChatAdapter(chatList, getContext(), this::openChatDetails);
        recyclerView.setAdapter(chatAdapter);

        databaseChats = FirebaseDatabase.getInstance().getReference("chats");
        auth = FirebaseAuth.getInstance();

        view.findViewById(R.id.buttonAddChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddChatDialog();
            }
        });

        databaseChats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Chat chat = postSnapshot.getValue(Chat.class);
                    chatList.add(chat);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        return view;
    }

    private void showAddChatDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_chat, null);
        builder.setView(dialogView);

        EditText editTextChatName = dialogView.findViewById(R.id.editTextChatName);
        EditText editTextChatDescription = dialogView.findViewById(R.id.editTextChatDescription);

        builder.setTitle("Add New Chat");
        builder.setPositiveButton("Add", (dialog, which) -> {
            String chatName = editTextChatName.getText().toString().trim();
            String chatDescription = editTextChatDescription.getText().toString().trim();
            String userId = auth.getCurrentUser().getUid();

            if (chatName.isEmpty() || chatDescription.isEmpty()) {
                Toast.makeText(getContext(), "Both fields are required", Toast.LENGTH_LONG).show();
            } else {
                addChat(chatName, chatDescription, userId);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void addChat(String name, String description, String userId) {
        String id = databaseChats.push().getKey();
        Chat chat = new Chat(id, name, description, userId);
        if (id != null) {
            databaseChats.child(id).setValue(chat);
        }
    }

    private void openChatDetails(Chat chat) {
        Intent intent = new Intent(getContext(), ChatDetailsActivity.class);
        intent.putExtra("chatId", chat.getId());
        intent.putExtra("chatName", chat.getName());
        intent.putExtra("chatTopic", chat.getDescription());
        startActivity(intent);
    }
}
