package com.vit.gff.codebusters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.vit.gff.codebusters.RootWork.server_ip;

public class datepicker extends Activity{
    private Button janbtn;
    private TextView date1;
    Context context;
    int i;
    String[] items=new String[31];

    ArrayList<String> fdates;

    SharedPreferences prefs ;

    Handler request;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datepicker);
        for(i=0;i<31;i++)
        {
            if (i<9)
                items[i] ="0"+ String.valueOf(i + 1);
            else
                items[i] =String.valueOf(i + 1);
        }

        date1 = findViewById(R.id.date1);
        janbtn= (Button)findViewById(R.id.button_1);

        request= new Handler();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        fdates = new ArrayList<>();

        prefs = getSharedPreferences("setting", MODE_PRIVATE);
        //setting janbtn's click and implementing the onClick method
        janbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //List of items to be show in  alert Dialog are stored in array of strings/char sequences  final

                set(date1,"01");

            }
        });

        findViewById(R.id.button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                set((TextView) findViewById(R.id.date2),"02");
            }
        });

        findViewById(R.id.button_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                set((TextView) findViewById(R.id.date3),"03");
            }
        });


        findViewById(R.id.Send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fdates.size()<3)
                {
                    Toast.makeText(datepicker.this,"Provide 3 dates",Toast.LENGTH_LONG).show();
                }
                else
                {
                    progressDialog.show();
                    send_data.start();
                }
            }
        });

        findViewById(R.id.predict).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                    predict_date.start();

            }
        });
    }


    void set(final TextView t,final String i)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(datepicker.this);

        //set the title for alert dialog
        builder.setTitle("Choose date: ");

        //set items to alert dialog. i.e. our array , which will be shown as list view in alert dialog
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override public void onClick(DialogInterface dialog, int item) {
                //setting the janbtn text to the selected itenm from the list
                t.setText("2018-"+i+"-"+items[item]);

                fdates.add("2018-"+i+"-"+items[item]);

            }
        });



        //Creating alert dialog
        AlertDialog alert =builder.create();

        //Showingalert dialog
        alert.show();
    }


    Thread send_data = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                int flag=0;
                Uri.Builder eventbuilder = new Uri.Builder();



                eventbuilder.scheme("http")
                        .authority(server_ip)
                        .appendPath("GFF")
                        .appendPath("insert.php")
                        .appendQueryParameter("un", prefs.getString("username",""))
                        .appendQueryParameter("date1",fdates.get(0))
                        .appendQueryParameter("date2", fdates.get(1))
                        .appendQueryParameter("date3", fdates.get(2));

                URL datalink = new URL(eventbuilder.build().toString());

                OkHttpClient client = new OkHttpClient();
                Log.d(getClass().getName()+" URL ", datalink.toString());

                //httpConnection.connect();
                Request.Builder builder1 = new Request.Builder();
                builder1.url(datalink);
                Request request1 = builder1.build();


                Response response1 = client.newCall(request1).execute();

                if (response1.body() != null) {
                    String b = response1.body().string().trim();
                    if (b.equalsIgnoreCase("added"))
                    {
                        Log.d(getClass().getName(),"done");
                        request.post(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(datepicker.this,"Added",Toast.LENGTH_LONG).show();
                                // onLoginFailed();
                                progressDialog.dismiss();
                                findViewById(R.id.Send).setVisibility(View.GONE);
                                findViewById(R.id.predict).setVisibility(View.VISIBLE);

                            }
                        });
                    }else {

                        request.post(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(datepicker.this,"Error",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                }





            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });

    Thread predict_date = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                int flag=0;
                Uri.Builder eventbuilder = new Uri.Builder();



                eventbuilder.scheme("http")
                        .authority(server_ip)
                        .appendPath("GFF")
                        .appendPath("period_track.php")
                        .appendQueryParameter("un", prefs.getString("username",""));

                URL datalink = new URL(eventbuilder.build().toString());

                OkHttpClient client = new OkHttpClient();
                Log.d(getClass().getName()+" URL ", datalink.toString());

                //httpConnection.connect();
                Request.Builder builder1 = new Request.Builder();
                builder1.url(datalink);
                Request request1 = builder1.build();


                Response response1 = client.newCall(request1).execute();

                if (response1.body() != null) {
                    final String b = response1.body().string().trim();
                    request.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();

                            TextView t = new TextView(datepicker.this);
                            t.setPadding(10,10,10,5);
                            t.setTextSize(20);
                            t.setText("Next Date : "+b);
                            Dialog dialog = new Dialog(datepicker.this);
                            dialog.setContentView(t);
                            dialog.setTitle("next");
                            dialog.setCancelable(true);
                            dialog.show();
                        }
                    });
                }





            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });



}
