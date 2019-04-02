package com.vit.gff.codebusters;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.vit.gff.codebusters.RootWork.channel1;

/**
 * Created by suraj on 12/9/18.
 */

public class serviceEmer extends Service implements LocationListener, RecognitionListener{


    String TAG ="===service==";
    LocationManager locationManager;
    Boolean isGPSEnable, isNetworkEnable;
    Location location;
    double latitude, longitude, track_lat=0.0, track_lng=0.0;

    Handler mHandler, handler;
    Timer mTimer;
    Timer timer;
    TimerTask timerTask;

    IBinder binder = new MyBinder();
    SharedPreferences read_setting;

    double lat, longi;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mHandler = new Handler();
        handler = new Handler();
        timer = new Timer();
        read_setting = getSharedPreferences("setting",MODE_PRIVATE);
        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(), 5, 5000);

        SpeechRecognizer speech = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        speech.setRecognitionListener(this);

        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en");

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getApplicationContext().getPackageName());

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForegroundService();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {


        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        Log.d(TAG,"======resuly");
        if (matches != null) {
            for (int i = 0; i < matches.size(); i++) {
                String helpw = matches.get(i);
                //speech.setText(helpw);
                if (helpw.equalsIgnoreCase("help") ||
                        (helpw.equalsIgnoreCase("bachao")) ||
                        (helpw.equalsIgnoreCase("utavi")) ||
                        (helpw.equalsIgnoreCase("hel")) ||
                        (helpw.equalsIgnoreCase("elp")) ||
                        (helpw.equalsIgnoreCase("hell")) ||
                        (helpw.equalsIgnoreCase("heal")))
                {
                    sendLocation();
                }
            }
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }


    public class MyBinder extends Binder
    {
        serviceEmer getService()
        {
            return serviceEmer.this;
        }

    }

    private void trackLocation() {
        Log.e(TAG, "trackLocation");
        String TAG_TRACK_LOCATION = "trackLocation";
        Map<String, String> params = new HashMap<>();
        params.put("latitude", "" + track_lat);
        params.put("longitude", "" + track_lng);

        lat = track_lat;
        longi = track_lng;

        Log.e(TAG, "param_track_location >> " + params.toString());

        stopSelf();
        //sendLocation();
        //mTimer.cancel();

    }


    @SuppressLint("MissingPermission")
    private void fn_getlocation()
    {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {
            Log.e(TAG, "CAN'T GET LOCATION");
            stopSelf();
        } else {
            if (isNetworkEnable) {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0,this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        Log.e(TAG, "isNetworkEnable latitude" + location.getLatitude() + "\nlongitude" + location.getLongitude() + "");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        track_lat = latitude;
                        track_lng = longitude;
//                        fn_update(location);
                    }
                }
            }

            if (isGPSEnable) {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0,this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        Log.e(TAG, "isGPSEnable latitude" + location.getLatitude() + "\nlongitude" + location.getLongitude() + "");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        track_lat = latitude;
                        track_lng = longitude;
//                        fn_update(location);
                    }
                }
            }

            Log.e("===service", "START SERVICE");
            trackLocation();

        }
    }

    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_getlocation();
                }
            });

        }
    }


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

        /*final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);


        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());

        Log.d(TAG, "=====recognize");

        mSpeechRecognizer.startListening(new RecognitionListener(){
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

                Log.d("====service--",results.toString());
                sendLocation();
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

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
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.d(TAG, "=====recognize ready");

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

            }

            @Override
            public void onPartialResults(Bundle bundle) {
                Log.d(TAG, "partial=======");

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

    @Override
    public void onDestroy() {
        stopSelf();
        super.onDestroy();
    }

    private void sendLocation() {

        Log.d("====service","location");

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



    };
}
