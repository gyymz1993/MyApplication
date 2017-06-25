package com.ys.coustom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CustomCircleProgressBar progress = (CustomCircleProgressBar) findViewById(R.id.id_progress);
        progress.setStatus(2);
        progress.setMaxProgress(320);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(count*10<320){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progress.nofityUpdate(count*10);
                    count++;
                }
            }
        }).start();
    }
}
