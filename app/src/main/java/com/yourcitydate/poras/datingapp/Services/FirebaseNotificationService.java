package com.yourcitydate.poras.datingapp.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.renderscript.RenderScript;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yourcitydate.poras.datingapp.Chat.ChatActivity;
import com.yourcitydate.poras.datingapp.Constants.AllConstants;
import com.yourcitydate.poras.datingapp.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FirebaseNotificationService extends FirebaseMessagingService {
    private final String SharedPrefs = "prefs";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    String UID ;

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public FirebaseNotificationService() {
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        updateToken(s);

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0){
          String  title = remoteMessage.getData().get("Title");
          String  message = remoteMessage.getData().get("Message");

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
                BuildNotificationForHigherVersions(title,message);
            }else{
                BuildNotification(title,message);
            }
        }


    }


    public void updateToken(String token){
        sharedPreferences = getApplicationContext().getSharedPreferences(SharedPrefs, getApplicationContext().MODE_PRIVATE);
        UID = sharedPreferences.getString("UID", "null");

        if (!UID.equals("null")){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);
            Map<String, Object> map = new HashMap<>();
            map.put("t",token);
            databaseReference.updateChildren(map);
        }


    }

    private void BuildNotification(String title, String Message){
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, AllConstants.channel_id);
        builder.setContentTitle(title)
                .setContentText(Message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null))
                .setSound(uri);


        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(new Random().nextInt(85-65), builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void BuildNotificationForHigherVersions(String title, String Message){
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationChannel channel = new NotificationChannel(AllConstants.channel_id,"MessageNotif",NotificationManager.IMPORTANCE_HIGH);
        channel.setShowBadge(true);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setDescription("Message Description");
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);


        Notification notification = new Notification.Builder(this, AllConstants.channel_id)
                .setContentTitle(title)
                .setContentText(Message)
                .setAutoCancel(true)
                .setSound(uri)
                .setColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();



        manager.notify(new Random().nextInt(85-65),notification);



    }
}