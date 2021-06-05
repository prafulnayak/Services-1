package com.techolution.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.techolution.services.App.CHANNEL_ID;

public class ExampleService extends Service {

    public static Boolean shouldIStop = false;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,notificationIntent,0);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_MIN);

        startForeground(111, notification.build());
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
                        NotificationManagerCompat.from(getApplicationContext()).notify(111, notification.build());
                    }

                    try{
                        Thread.sleep(1000);
                    }catch (Exception ignored){
                        ignored.printStackTrace();
                    }
                }
            }
        });
        //do heavy work on backgroud

//        stopSelf();

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.e("----","onDestroy cleared");
        super.onDestroy();
    }

}
