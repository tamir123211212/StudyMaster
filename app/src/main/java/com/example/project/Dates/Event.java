package com.example.project.Dates;

public class Event {

    private String id;
    private String userId;
    private String eventId;
    private String eventDate;
    private String eventTime;
    private String eventType;
    private String description;
    private boolean wantsNotification;

    // Default constructor
    public Event() {
    }

    // Constructor with all fields
    public Event(String id, String userId, String eventId, String eventDate, String eventTime, String eventType, String description, boolean wantsNotification) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventType = eventType;
        this.description = description;
        this.wantsNotification = wantsNotification;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isWantsNotification() {
        return wantsNotification;
    }

    public void setWantsNotification(boolean wantsNotification) {
        this.wantsNotification = wantsNotification;
    }
}
