package com.infuse.headlessworkmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.WorkerParameters;

import com.facebook.react.HeadlessJsTaskService;

import java.util.List;
import java.util.Set;

public class Worker extends androidx.work.Worker {
    private static String TAG = Worker.class.getSimpleName();
    private final Context context;

    public Worker(
            @NonNull Context context,
            @NonNull WorkerParameters params
    ) {
        super(context, params);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Bundle extras = bundleExtras();

        Intent service = new Intent(this.context, HeadlessService.class);
        service.putExtras(extras);
        HeadlessJsTaskService.acquireWakeLockNow(this.context);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            this.context.startForegroundService(service);
        } else {
            this.context.startService(service);
        }
        return Result.success();
    }

    private Bundle bundleExtras() {
        Data data = getInputData();
        Bundle extras = new Bundle();

        Set<String> dataKeys = data.getKeyValueMap().keySet();
        for (String key : dataKeys) {
            String value = data.getString(key);
            extras.putString(key, value);
        }
        return extras;
    }
}
