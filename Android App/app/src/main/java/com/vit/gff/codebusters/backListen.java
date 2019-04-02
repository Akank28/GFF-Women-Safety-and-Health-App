package com.vit.gff.codebusters;

import android.app.Activity;
import android.app.Service;

/**
 * Created by suraj on 11/9/18.
 */

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.vit.gff.codebusters.RootWork.channel1;


public class backListen extends Service implements LocationListener{

    private static final String TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE";

    Binder binder = new MyBinder();

    protected LocationListener locationListener;
    protected Context context;
    String provider;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;

    private float xAccel, yAccel, zAccel;
    private float xPreviousAccel, yPreviousAccel, zPreviousAccel;
    private boolean firstUpdate = true;
    private final float shakeThreshold = 1.5f;
    private boolean shakeInitiated = false;
    SensorEventListener mySensorEventListener;
    SensorManager mySensorManager;
    int flag=1;
    int ntimes=0;


    SharedPreferences.Editor editor;
    SharedPreferences read_setting;
    AlertDialog dialog;

    Double lat,longi;

    Timer timer;
    TimerTask timerTask;
    LocationManager locationManager;

    void initialize(LocationManager loc)
    {
        locationManager = loc;
    }

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();

    public backListen() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    public class MyBinder extends Binder
    {
        backListen getService()
        {
            return backListen.this;
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG_FOREGROUND_SERVICE, "My foreground service onCreate().");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startForegroundService();
        Toast.makeText(getApplicationContext(), "Foreground service is started.", Toast.LENGTH_LONG).show();

        return START_NOT_STICKY;
    }

    /* Used to build and start foreground service. */
    private void startForegroundService()
    {
        Log.d("====service","found");
        Notification notification = null;

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(getApplicationContext(), channel1)
                    .setContentTitle("GFF")
                    .setContentText("Emergency Mode ON")
                    .setSmallIcon(R.mipmap.ic_gff)
                    .setContentIntent(pendingIntent)
                    .build();
        }else
        {
            notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("GFF..")
                    .setContentText("Emergency Mode ON")
                    .setSmallIcon(R.drawable.ic_mic)
                    .setContentIntent(pendingIntent)
                    .build();
        }

        //NotificationManager mNotifyManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

        //mNotifyManager.notify(FOREGROUND_JOB_ID, notification);

        startForeground(121,notification);


        work();

    }


    void work()
    {


        Log.d("====service--","inside work");

        //checkPermission();


        //final TextView speech = (TextView) findViewById(R.id.speechtotext);

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);


        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());


        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

                if (bytes!=null)
                    Log.d("=====check==",bytes.toString());

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {

                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                Log.d("====service--",bundle.toString());
                //displaying the first match
                if (matches != null) {
                    for (int i = 0; i < matches.size(); i++) {
                        String helpw = matches.get(i);
                        //speech.setText(helpw);
                        if (helpw.equalsIgnoreCase("help") ||
                                (helpw.equalsIgnoreCase("bachao")) ||
                                (helpw.equalsIgnoreCase("utavi")) ||
                                (helpw.equalsIgnoreCase("hel"))||
                                (helpw.equalsIgnoreCase("elp"))||
                                (helpw.equalsIgnoreCase("hell"))||
                                (helpw.equalsIgnoreCase("heal")))
                        {
                            startTimer();
                        }
                    }

                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        /*findViewById(R.id.button_mic).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        speech.setHint("Press mic to speak");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        speech.setText("");
                        speech.setHint("I am listening...");
                        break;
                }
                return false;
            }
        });*/


    }
    private void stopTimer()
    {
        if(timer!=null)
        {
            timer.cancel();
            timer=null;
        }
    }

    private void startTimer() {

        timer=new Timer();
        sendLocation();
        timer.schedule(timerTask, 0, 5*60*1000);


    }

    private void sendLocation() {

        Log.d("====service","location");
        timerTask = new TimerTask() {
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String scAddress = null;
// Set pending intents to broadcast
// when message sent and when delivered, or set to null.
                        PendingIntent sentIntent = null, deliveryIntent = null;
                        //PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, **Object.class**),0);
                        SmsManager smsManager = SmsManager.getDefault();
                        String destinationAddress1 = read_setting.getString("emergency1", "0");
                        String destinationAddress2 = read_setting.getString("emergency2", "0");
                        String smsMessage = "Help Me! Track my location at " + "http://maps.google.com/maps?daddr=" + lat + "," + longi;
                        smsManager.sendTextMessage
                                (destinationAddress1, scAddress, smsMessage,
                                        sentIntent, deliveryIntent);
                        smsManager.sendTextMessage
                                (destinationAddress2, scAddress, smsMessage,
                                        sentIntent, deliveryIntent);
                        // Toast.makeText(this, smsMessage, Toast.LENGTH_SHORT).show();


                    }
                });
            }
        };
    };










            private void checkPermission() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:" + getPackageName()));
                        startActivity(intent);

                    }
                }
            }

            @Override
            public void onLocationChanged(Location location) {
                lat=location.getLatitude();
                longi=location.getLongitude();
                Log.d("=====service==",lat.toString());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }



            void dialogend()
            {
                dialog.dismiss();
            }

            public void stopForegroundService()
            {
                Log.d(TAG_FOREGROUND_SERVICE, "Stop foreground service.");

                // Stop foreground service and remove the notification.
                stopForeground(true);

                // Stop the foreground service.
                stopSelf();
            }
        }