package com.project.mobility.util.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.view.activities.web.WebViewActivity;

import javax.inject.Inject;

public class NotificationIntentHelper {
    @Inject Context context;

    public NotificationIntentHelper() {
        Injection.inject(this);
    }

    public PendingIntent buildIntent(NotificationModel notificationModel) {
        PendingIntent pendingIntent;
        if (notificationModel.getUrl() != null) {
            pendingIntent = createWebViewIntent(notificationModel.getUrl());
        } else {
            pendingIntent = createIntent();
        }

        return pendingIntent;
    }

    private PendingIntent createIntent() {
//        PendingIntent pendingIntent;
//        Intent intent = new Intent(context, AlertDetails.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        return null;
    }

    private PendingIntent createWebViewIntent(String url) {
        PendingIntent pendingIntent;
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(WebViewActivity.KEY_URL, url);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
