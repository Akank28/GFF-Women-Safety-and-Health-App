package com.vit.gff.codebusters;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;

/**
 * Created by suraj on 11/9/18.
 */

public class RootWork extends Application {
    public static String server_ip = "192.168.43.134";
    public static String channel1="HIGH";
    public static String channel2="LOW";

    SharedPreferences local_storage;
    @Override
    public void onCreate() {
        super.onCreate();
        local_storage = getSharedPreferences("setting",MODE_PRIVATE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channelH= new NotificationChannel(channel1,"Channel 1", NotificationManager.IMPORTANCE_HIGH);
            channelH.setDescription("HIGH working");

            NotificationChannel channelL= new NotificationChannel(channel2,"Channel 2", NotificationManager.IMPORTANCE_LOW);
            channelH.setDescription("LOW working");

            NotificationManager manager= getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channelH);
            manager.createNotificationChannel(channelL);
        }

    }
}
