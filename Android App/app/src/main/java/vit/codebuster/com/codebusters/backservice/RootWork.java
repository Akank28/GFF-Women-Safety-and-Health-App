package vit.codebuster.com.codebusters.backservice;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.List;

public class RootWork extends Application {

    public static final String channel1="HIGH";
    public static final String channel2="LOW";
    public static final String server_ip="192.168.43.134";
    public static SharedPreferences local_storage;
    public static final String sharedpref_name="storage";
    public static final int JOB_ID = 121;
    public static final int FOREGROUND_JOB_ID = 121;
    public static final String MY_PREFS_NAME = "User_Data";

    @Override
    public void onCreate() {
        super.onCreate();
        local_storage = getSharedPreferences(sharedpref_name,MODE_PRIVATE);
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

    public static SharedPreferences get_sharedPref()
    {
        return local_storage;
    }


}
