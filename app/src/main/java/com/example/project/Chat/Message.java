package com.example.project.Chat;

public class Message {
    private String id;
    private String content;
    private String senderId;
    private String senderName;
    private String senderProfileImage;
    private long timestamp;

    public Message() {
    }

    public Message(String id, String content, String senderId, String senderName, String senderProfileImage, long timestamp) {
        this.id = id;
        this.content = content;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderProfileImage = senderProfileImage;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderProfileImage() {
        return senderProfileImage;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
