package com.example.project.Chat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.project.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.textViewContent.setText(message.getContent());
        holder.textViewSenderName.setText(message.getSenderName());

        Log.d("MessageAdapter", "Loading Image URL: " + message.getSenderProfileImage());

        // Using Glide to load the profile image
        Glide.with(holder.itemView.getContext())
                .load(message.getSenderProfileImage())
                .placeholder(R.drawable.baseline_account_circle_24)
                .into(holder.imageViewProfile);

        // Formatting the timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String formattedTime = sdf.format(new Date(message.getTimestamp()));
        holder.textViewTimestamp.setText(formattedTime);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewContent;
        public TextView textViewSenderName;
        public TextView textViewTimestamp;
        public ImageView imageViewProfile;

        public MessageViewHolder(View view) {
            super(view);
            textViewContent = view.findViewById(R.id.textViewContent);
            textViewSenderName = view.findViewById(R.id.textViewSenderName);
            textViewTimestamp = view.findViewById(R.id.textViewTimestamp);
            imageViewProfile = view.findViewById(R.id.imageViewProfile);
        }
    }
}
