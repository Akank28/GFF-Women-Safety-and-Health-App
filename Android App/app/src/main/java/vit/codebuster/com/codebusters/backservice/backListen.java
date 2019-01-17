package vit.codebuster.com.codebusters.backservice;

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
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
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

import vit.codebuster.com.codebusters.MainActivity;
import vit.codebuster.com.codebusters.R;
import vit.codebuster.com.codebusters.Setting;
import vit.codebuster.com.codebusters.emergency;

import static vit.codebuster.com.codebusters.backservice.RootWork.FOREGROUND_JOB_ID;
import static vit.codebuster.com.codebusters.backservice.RootWork.MY_PREFS_NAME;
import static vit.codebuster.com.codebusters.backservice.RootWork.channel1;

public class backListen extends Service implements LocationListener, SensorEventListener{

    private static final String TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE";

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";

    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";

    public static final String ACTION_PAUSE = "ACTION_PAUSE";

    public static final String ACTION_PLAY = "ACTION_PLAY";

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

    LocationManager locationManager;

    SharedPreferences.Editor editor;
    SharedPreferences read_setting;
    AlertDialog dialog;

    Double lat,longi;

    public backListen() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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
                    .setSmallIcon(R.drawable.icon_record)
                    .setContentIntent(pendingIntent)
                    .build();
        }else
        {
            notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("GFF..")
                    .setContentText("Emergency Mode ON")
                    .setSmallIcon(R.drawable.icon_record)
                    .setContentIntent(pendingIntent)
                    .build();
        }

        //NotificationManager mNotifyManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

        //mNotifyManager.notify(FOREGROUND_JOB_ID, notification);

        startForeground(FOREGROUND_JOB_ID,notification);

        work();

    }

    void work()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mySensorManager.registerListener(mySensorEventListener,
                mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);





        checkPermission();


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

                //displaying the first match
                if (matches != null)
                {   String helpw=matches.get(0);
                    //speech.setText(helpw);
                    if (helpw.equalsIgnoreCase("help") || (helpw.equalsIgnoreCase("bachao")) ||  (helpw.equalsIgnoreCase("utavi")))
                    {
                        sendLocation();
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

    private void sendLocation() {






        // Set the service center address if needed, otherwise null.
        String scAddress = null;
// Set pending intents to broadcast
// when message sent and when delivered, or set to null.
        PendingIntent sentIntent = null, deliveryIntent = null;
        //PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, **Object.class**),0);
        SmsManager smsManager = SmsManager.getDefault();
        String destinationAddress1=read_setting.getString("emergency1","0");
        String destinationAddress2=read_setting.getString("emergency2","0");
        String smsMessage="Help Me! Track my location at "+"http://maps.google.com/maps?daddr=" + lat + "," + longi;
        smsManager.sendTextMessage
                (destinationAddress1, scAddress, smsMessage,
                        sentIntent, deliveryIntent);
        smsManager.sendTextMessage
                (destinationAddress2, scAddress, smsMessage,
                        sentIntent, deliveryIntent);
        Toast.makeText(this, smsMessage, Toast.LENGTH_SHORT).show();
    }


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

    private boolean isAccelerationChanged() {
        float deltaX = Math.abs(xPreviousAccel - xAccel);
        float deltaY = Math.abs(yPreviousAccel - yAccel);
        float deltaZ = Math.abs(zPreviousAccel - zAccel);
        return (deltaX > shakeThreshold && deltaY > shakeThreshold)
                || (deltaX > shakeThreshold && deltaZ > shakeThreshold)
                || (deltaY > shakeThreshold && deltaZ > shakeThreshold);
    }

    private void updateAccelParameters(float xNewAccel, float yNewAccel, float zNewAccel) {
        if (firstUpdate) {
            xPreviousAccel = xNewAccel;
            yPreviousAccel = yNewAccel;
            zPreviousAccel = zNewAccel;
            firstUpdate = false;
        }else{
            xPreviousAccel = xAccel;
            yPreviousAccel = yAccel;
            zPreviousAccel = zAccel;
        }
        xAccel = xNewAccel;
        yAccel = yNewAccel;
        zAccel = zNewAccel;
    }
    private void executeShakeAction() {
        //this method is called when devices shakes
        sendLocation();
    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        updateAccelParameters(se.values[0], se.values[1], se.values[2]);
        if ((!shakeInitiated) && isAccelerationChanged()) {
            shakeInitiated = true;
        }else if ((shakeInitiated) && isAccelerationChanged()){
            executeShakeAction();
        }else if((shakeInitiated) && (!isAccelerationChanged())){
            shakeInitiated = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

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
