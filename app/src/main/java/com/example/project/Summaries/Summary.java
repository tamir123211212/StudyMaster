package com.example.project.Summaries;

public class Summary {
    private String title;
    private String userId;
    private String userName;
    private String subject;
    private String uploadTime;
    private String imageUrl;



    public Summary() {
    }



    public Summary(String title, String userId, String userName, String subject, String uploadTime, String imageUrl) {
        this.title = title;
        this.userId = userId;
        this.userName = userName;
        this.subject = subject;
        this.uploadTime = uploadTime;
        this.imageUrl = imageUrl;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
