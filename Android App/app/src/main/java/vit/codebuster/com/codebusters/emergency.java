package vit.codebuster.com.codebusters;

import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import vit.codebuster.com.codebusters.backservice.RootWork;
import vit.codebuster.com.codebusters.backservice.backListen;

import static vit.codebuster.com.codebusters.backservice.RootWork.MY_PREFS_NAME;

public class emergency extends AppCompatActivity implements LocationListener, SensorEventListener {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mySensorManager.registerListener(mySensorEventListener,
                mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        read_setting = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        String test = read_setting.getString("emergency1",null);
        String test1 = read_setting.getString("emergency2",null);
        if (test==null || test1==null){

            dialog = new AlertDialog.Builder(emergency.this)
                    .setTitle("Please Add Emergency Contacts")
                    .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(emergency.this,Setting.class);
                            startActivity(intent);
                            dialogend();
                        }
                    })
                    .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .create();
            dialog.show();
        }



        checkPermission();


        final TextView speech = (TextView) findViewById(R.id.speechtotext);

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
                    speech.setText(helpw);
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

        findViewById(R.id.button_mic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent backlis = new Intent(emergency.this,backListen.class);
                if (flag==1) {
                    change();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {



                        ContextCompat.startForegroundService(emergency.this,backlis);
                    } else {
                        startService(backlis);
                    }
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    speech.setText("");
                    speech.setHint("I am listening...");



                    ((TextView) findViewById(R.id.speechtotext)).setText("Enabled");

                }else
                {



                    ((TextView) findViewById(R.id.speechtotext)).setText("Diabled");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        stopService(backlis);
                    } else {
                        stopService(backlis);
                    }
                    change();
                    mSpeechRecognizer.stopListening();
                    speech.setText("");
                    speech.setHint("Press mic to speak");
                }
            }
        });
    }

    void change()
    {
        if (flag==1)
        {
            flag=0;
        }else
            flag=1;
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
                finish();
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
}
