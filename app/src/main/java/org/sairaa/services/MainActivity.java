package org.sairaa.services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });
    }

    public void startService(){
        SimpleService.shouldIStop = false;
        Intent serviceIntent = new Intent(this, SimpleService.class);
//        serviceIntent.putExtra("inputExtra",binding.editText.getText().toString().trim());
        this.startService(serviceIntent);
    }

    public void stopService(){
        Intent serviceIntent = new Intent(this, SimpleService.class);
        SimpleService.shouldIStop = true;
        this.stopService(serviceIntent);
    }
}