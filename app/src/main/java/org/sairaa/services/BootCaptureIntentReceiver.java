package org.sairaa.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class BootCaptureIntentReceiver extends BroadcastReceiver {
    private static final String UNIQUE_PULL_DATA_FROM_SERVER = "UNIQUE_PULL_DATA_FROM_SERVER";
    @Override
    public void onReceive(Context context, Intent intent) {
        //we double check here for only boot complete event
        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED))
        {
            //here we start the service  again.
            WorkManager workManager = WorkManager.getInstance(context);
            Constraints.Builder constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.NOT_REQUIRED);

            OneTimeWorkRequest downloadSyncRequest = new OneTimeWorkRequest.Builder(DownLoadWorker.class)
                    .setConstraints(constraints.build())
                    .setBackoffCriteria(
                            BackoffPolicy.LINEAR,
                            OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                            TimeUnit.MICROSECONDS)
                    .build();

//        workManager.enqueueUniqueWork(downloadSyncRequest);

            workManager.enqueueUniqueWork(UNIQUE_PULL_DATA_FROM_SERVER, ExistingWorkPolicy.REPLACE,downloadSyncRequest);
//            Intent serviceIntent = new Intent(context, StartServiceOnBoot.class);
//            context.startService(serviceIntent);
        }
    }
}