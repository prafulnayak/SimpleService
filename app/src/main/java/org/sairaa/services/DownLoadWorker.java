package org.sairaa.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static android.content.Context.NOTIFICATION_SERVICE;

public class DownLoadWorker extends Worker {
    private static final String KEY_INPUT_URL = "KEY_INPUT_URL";
    private static final String KEY_OUTPUT_FILE_NAME = "KEY_OUTPUT_FILE_NAME";

    private NotificationManager notificationManager;

    public DownLoadWorker(
            @NonNull Context context,
            @NonNull WorkerParameters parameters) {
        super(context, parameters);
        notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data inputData = getInputData();
        String inputUrl = inputData.getString(KEY_INPUT_URL);
        String outputFile = inputData.getString(KEY_OUTPUT_FILE_NAME);
        // Mark the Worker as important
        String progress = "Starting Service";
        setForegroundAsync(createForegroundInfo());
        doWorkInBackground();
        return Result.success();
    }

    private void doWorkInBackground() {

        for(int i =0;i<10000;i++){
            Log.i("Tag",""+String.valueOf(i));
            updateNotification(String.valueOf(i));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateNotification(String s) {
        String id = getApplicationContext().getString(R.string.notification_channel_id);
        NotificationCompat.Builder notification = getNotificationBuilder(getApplicationContext(),id);
        notification.setContentText(s);
        NotificationManagerCompat.from(getApplicationContext()).notify(1,notification.build());
    }

    @NonNull
    private ForegroundInfo createForegroundInfo() {
        // Build a notification using bytesRead and contentLength

        Context context = getApplicationContext();
        String id = context.getString(R.string.notification_channel_id);
        String title = context.getString(R.string.notification_title);
        String cancel = context.getString(R.string.cancel_download);
        // This PendingIntent can be used to cancel the worker
        PendingIntent intent = WorkManager.getInstance(context)
                .createCancelPendingIntent(getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(id);
        }

        NotificationCompat.Builder notification = getNotificationBuilder(getApplicationContext(),id);

        return new ForegroundInfo(1, notification.build());
    }

    private NotificationCompat.Builder getNotificationBuilder(Context applicationContext, String id) {

        Intent notificationIntent = new Intent(applicationContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(applicationContext,
                0,notificationIntent,0);

        return new NotificationCompat.Builder(applicationContext, id)
                .setContentTitle("Test Service")
                .setTicker("title")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setAutoCancel(false);

                // Add the cancel action to the notification which can
                // be used to cancel the worker
//                .addAction(android.R.drawable.ic_delete, "cancel", intent);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel(String id) {
        // Create a Notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Test";//getString(R.string.channel_name);
            String description = "X";//getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
