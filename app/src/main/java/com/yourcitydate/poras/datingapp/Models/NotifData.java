package com.yourcitydate.poras.datingapp.Models;

public class NotifData {
    private String Title;
    private String Message;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public NotifData(String title, String message) {
        Title = title;
        Message = message;
    }
}
