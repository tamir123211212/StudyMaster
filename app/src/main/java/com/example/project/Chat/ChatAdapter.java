package com.example.project.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project.R;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Chat> chatList;
    private Context context;
    private OnChatClickListener onChatClickListener;

    public ChatAdapter(List<Chat> chatList, Context context, OnChatClickListener onChatClickListener) {
        this.chatList = chatList;
        this.context = context;
        this.onChatClickListener = onChatClickListener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.textViewName.setText(chat.getName());
        holder.textViewDescription.setText(chat.getDescription());

        holder.itemView.setOnClickListener(v -> onChatClickListener.onChatClick(chat));
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewDescription;

        public ChatViewHolder(View view) {
            super(view);
            textViewName = view.findViewById(R.id.textViewName);
            textViewDescription = view.findViewById(R.id.textViewDescription);
        }
    }

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }
}
