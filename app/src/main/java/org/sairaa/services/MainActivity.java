package org.sairaa.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String UNIQUE_PULL_DATA_FROM_SERVER = "UNIQUE_PULL_DATA_FROM_SERVER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startService();
                statS();
            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                stopService();
                stopS();
            }
        });
    }

    private void stopS() {
        WorkManager workManager = WorkManager.getInstance(getApplicationContext());
        workManager.cancelAllWork();
    }

    private void statS() {
        WorkManager workManager = WorkManager.getInstance(getApplicationContext());
        Constraints.Builder constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.NOT_REQUIRED);

        OneTimeWorkRequest downloadSyncRequest = new OneTimeWorkRequest.Builder(DownLoadWorker.class)
                .setConstraints(constraints.build())
                .setBackoffCriteria(
                        BackoffPolicy.LINEAR,
                        OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                        TimeUnit.MICROSECONDS)
                .build();

//        workManager.enqueueUniqueWork(downloadSyncRequest);

        workManager.enqueueUniqueWork(UNIQUE_PULL_DATA_FROM_SERVER,ExistingWorkPolicy.REPLACE,downloadSyncRequest);
    }

    public void startService(){
        SimpleService.shouldIStop = false;
        Intent serviceIntent = new Intent(this, SimpleService.class);
//        serviceIntent.putExtra("inputExtra",binding.editText.getText().toString().trim());
        ContextCompat.startForegroundService(this,serviceIntent);
//        this.startService(serviceIntent);
    }

    public void stopService(){
        Intent serviceIntent = new Intent(this, SimpleService.class);
        SimpleService.shouldIStop = true;
        this.stopService(serviceIntent);
    }
}