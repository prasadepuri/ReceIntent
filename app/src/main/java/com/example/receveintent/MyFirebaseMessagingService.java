package com.example.receveintent;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID="TeraNews";
    String title,body;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        if(remoteMessage.getNotification()!=null)
        {
            title=remoteMessage.getNotification().getTitle();
            body=remoteMessage.getNotification().getBody();
            createDisplayContext(title,body);
        }

    }

    private void createDisplayContext(String title, String body) {
        Intent i=new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,1,i,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder=(NotificationCompat.Builder)new NotificationCompat.Builder(this,CHANNEL_ID)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.deafult_news_icon);
        builder.setSound(uri);
        long[] v = {500,1000};
        builder.setVibrate(v);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
        NotificationManagerCompat managerCompat= NotificationManagerCompat.from(this);
        managerCompat.notify(1, builder.build());
    }


}
