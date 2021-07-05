package com.infuse.headlessworkmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.jstasks.HeadlessJsTaskConfig;

import javax.annotation.Nullable;

public class HeadlessService extends HeadlessJsTaskService {
    @Nullable
    @Override
    protected HeadlessJsTaskConfig getTaskConfig(Intent intent) {
        Bundle extras = intent.getExtras();

        String taskKey = extras.getString(Constants.TASK_KEY);
        extras.remove(Constants.TASK_KEY);

        Integer timeout = extras.getInt(Constants.TIMEOUT);
        extras.remove(Constants.TIMEOUT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String name = "HeadlessServiceJOB";
            NotificationChannel channel = new NotificationChannel(name, name, NotificationManager.IMPORTANCE_MIN);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            Notification notification = new Notification.Builder(this, name)
                    .setWhen(System.currentTimeMillis())
                    .setContentText("JOB STARTED")
                    .setContentTitle("JOB STARTED")
                    .setSmallIcon(getResources().getIdentifier(name,"drawable",getApplicationContext().getPackageName()))
                    .build();
            int number = (int) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000;
            startForeground(number, notification);
        }

        return new HeadlessJsTaskConfig(
                taskKey,
                Arguments.fromBundle(extras),
                timeout,
                true
        );
    }
}
