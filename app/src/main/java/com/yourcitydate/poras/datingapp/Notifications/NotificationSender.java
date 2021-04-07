package com.yourcitydate.poras.datingapp.Notifications;

import android.provider.ContactsContract;

import com.yourcitydate.poras.datingapp.Models.NotifData;

public class NotificationSender {
    public NotifData data;
    public String to;

    public NotificationSender(NotifData data, String to) {
        this.data = data;
        this.to = to;
    }

    public NotificationSender() {

    }
}
