package com.project.mobility.messaging.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.storage.Preferences;
import com.project.mobility.util.notification.NotificationModel;
import com.project.mobility.util.notification.NotificationsHelper;

import java.util.Map;

import javax.inject.Inject;

import timber.log.Timber;

public class NotificationsFirebaseMessagingService extends FirebaseMessagingService {
    @Inject Preferences preferences;
    @Inject NotificationsHelper notificationsHelper;

    @Inject
    public NotificationsFirebaseMessagingService() {
        Injection.inject(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Timber.d("From: %s", remoteMessage.getFrom());

        if (hasDataPayload(remoteMessage)) {
            Timber.d("Message data payload: %s", remoteMessage.getData());
            handleDataMessage(remoteMessage.getData());
        }

        if (hasNotificationPayload(remoteMessage)) {
            Timber.d("Got title %s", remoteMessage.getNotification().getTitle());
            handleNotificationMessage(remoteMessage.getNotification());
        }
    }

    private boolean hasNotificationPayload(RemoteMessage remoteMessage) {
        return remoteMessage.getNotification() != null;
    }

    private boolean hasDataPayload(RemoteMessage remoteMessage) {
        return remoteMessage.getData().size() > 0;
    }

    private void handleNotificationMessage(RemoteMessage.Notification notification) {
        String title = notification.getTitle();
        String body = notification.getBody();
        notificationsHelper.createNotification(new NotificationModel(title, body, null));
    }

    private void handleDataMessage(Map<String, String> data) {
        String title = data.get("title");
        String url = data.get("url");
        String body = data.get("body");
        notificationsHelper.createNotification(new NotificationModel(title, body, url));
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Timber.d("FCM token %s", token);
        preferences.setString(Preferences.KEY_FCM_TOKEN, token);
    }
}
