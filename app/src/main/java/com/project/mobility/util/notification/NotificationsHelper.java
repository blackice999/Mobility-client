package com.project.mobility.util.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import com.project.mobility.R;
import com.project.mobility.di.injection.Injection;

import javax.inject.Inject;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import timber.log.Timber;

public class NotificationsHelper {
    @Inject Context context;

    private static final String CHANNEL_ID = "notifications_channel_id";
    private static final int NOTIFICATION_ID = 6685484;
    private static final String CHANNEL_NAME = "Notifications";

    @Inject
    public NotificationsHelper() {
        Injection.inject(this);
    }

    public void createNotification(NotificationModel notificationModel) {
        Timber.d("Created notification with title%s", notificationModel.getTitle());
        NotificationIntentHelper notificationIntentHelper = new NotificationIntentHelper();
        PendingIntent pendingIntent = notificationIntentHelper.buildIntent(notificationModel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setColor(context.getResources().getColor(R.color.primary))
                .setContentTitle(notificationModel.getTitle())
                .setContentText(notificationModel.getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        displayNotification(builder.build());
    }

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void displayNotification(Notification build) {
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, build);
    }

    public void removeNotification() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
