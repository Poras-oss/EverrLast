package com.yourcitydate.poras.datingapp.Notifications;

import android.app.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({"Content-Type:application/json", "Authorization: key=AAAA7fr_ydA:APA91bF1kpuDZUvWHeHQ_xGdx0JBAxxlvhuzmxDvi8qHmj79gmPuSJGxdqDMGL6oED7h6PnJAgkOy0xIM9EBzQ2YSUemy01NxvZXKba4BUIPQR1_Z9rGXMWXvo2iL7zjvKRrMjDkOnBa"})

    @POST("fcm/send")
    Call<mResponse> sendNotification(@Body NotificationSender body);
}
