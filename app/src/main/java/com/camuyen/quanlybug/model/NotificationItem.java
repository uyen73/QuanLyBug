package com.camuyen.quanlybug.model;

public class NotificationItem {
    private String id;      // ID của tài liệu Firestore
    private String token;   // Token của thiết bị
    private String title;
    private String message;
    private boolean isRead;
    private String maBug;

    // Constructors
    public NotificationItem() {
        // Default constructor required for Firestore
    }

    public NotificationItem(String id, String token, String title, String message, boolean isRead, String maBug) {
        this.id = id;
        this.token = token;
        this.title = title;
        this.message = message;
        this.isRead = isRead;
        this.maBug = maBug;
    }

    // Getters and setters

    public String getMaBug() {
        return maBug;
    }

    public void setMaBug(String maBug) {
        this.maBug = maBug;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}


