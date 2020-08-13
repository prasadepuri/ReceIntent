package com.example.receveintent;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessageInit extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "TeraNews" ;
    Context context;
    String msg="";
    public void initProcess(Context context,String topic)
    {
        this.context=context;
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        createNotificationChannel();
        subscribeToTopic(context,topic);
    }

    private  void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            String description = "Requset Notification Channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
           channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManagerCompat notificationManagerCompat= NotificationManagerCompat.from(context);
            //notificationManagerCompat.createNotificatation(new NotificationChannel(CHANNEL_ID,
            //        "first", NotificationManager.IMPORTANCE_HIGH));
        }


    }
    public  void subscribeToTopic(final Context context, final String topic) {
        // [START subscribe_topics]

        if (!topic.equals("null")) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                             msg = "subscribed to topic "+topic;
                            if (!task.isSuccessful()) {
                                msg = "Failed to subscribe";
                            }
                            Log.d("status", msg);
                            //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
            // [END subscribe_topics]
        }
        else {
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        }
    }

}
