package com.yourcitydate.poras.datingapp.Models;

public class message {
    String sender;
    String message;
    String time;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public message(String sender, String message, String time) {
        this.sender = sender;
        this.message = message;
        this.time = time;
    }
}
