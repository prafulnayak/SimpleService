package org.sairaa.services;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleService extends Service {
    public static Boolean shouldIStop = false;
    private static final String CHANNEL_ID = "test SimpleService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Context appContext = getApplicationContext();

        String name = "VERBOSE_NOTIFICATION_CHANNEL_NAME";
        String description = "VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            // Add the channel
            NotificationManager notificationManager =
                    (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

        }

        Intent notificationIntent = new Intent(appContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,notificationIntent,0);

         NotificationCompat.Builder notification = new NotificationCompat.Builder(appContext, CHANNEL_ID)
                .setContentTitle("Simple Service")
                .setContentText("0")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_MIN);

        startForeground(123, notification.build());

        ExecutorService singleT = Executors.newSingleThreadExecutor();

        singleT.execute(new Runnable() {
            @Override
            public void run() {
                for(int i =0; i<10000;i++){
                    Log.i("-----i",": "+String.valueOf(i));
                    if(singleT.isShutdown())
                        break;
                    if(shouldIStop){
                        singleT.shutdown();
                        stopSelf();
                        break;
                    }else {
                        notification.setContentText(String.valueOf(i));
                        NotificationManagerCompat.from(getApplicationContext()).notify(123, notification.build());
                    }

                    try{
                        Thread.sleep(1000);
                    }catch (Exception ignored){
                        ignored.printStackTrace();
                    }
                }
            }
        });
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("----","onDestroy cleared");
    }
}