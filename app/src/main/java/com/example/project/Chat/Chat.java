package com.example.project.Chat;

public class Chat {
    private String id;
    private String name;
    private String description;
    private String sender;

    public Chat() {
        // Default constructor required for calls to DataSnapshot.getValue(Chat.class)
    }

    public Chat(String id, String name, String description, String sender) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSender() {
        return sender;
    }
}
