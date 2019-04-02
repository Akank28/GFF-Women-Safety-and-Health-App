package com.vit.gff.codebusters;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.webkit.WebView;

import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.vit.gff.codebusters.RootWork.server_ip;

public class Display extends AppCompatActivity {

    Handler handler;
    String output;
    WebView web;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        setTitle("Analysis");

        output = getIntent().getExtras().getString("data");
        handler = new Handler();

        web = findViewById(R.id.webview);
        progress = new ProgressDialog(this);

        fetch_predict.start();
    }


    Thread fetch_predict = new Thread(new Runnable() {
        @Override
        public void run() {
            URL datalink=null;
            try {


                String idList = output;
                String csv = idList.substring(1, idList.length() - 1).replace(", ", ".");
                Uri.Builder eventbuilder = new Uri.Builder();

                eventbuilder.scheme("http")
                        .authority(server_ip)
                        //.appendPath("register")
                        .appendPath("GFF")
                        .appendPath("upload.php")
                        .appendQueryParameter("data",csv);

                datalink = new URL(eventbuilder.build().toString());

                OkHttpClient client = new OkHttpClient();
                Log.d("URL ",datalink.toString());



                //httpConnection.connect();
                Request.Builder builder1 = new Request.Builder();
                builder1.url(datalink);
                Request request1 = builder1.build();

                StringBuffer line=new StringBuffer();

                Response response1 = client.newCall(request1).execute();
                if( response1.body() != null)
                {
                    line.append(response1.body().string().trim());

                    Log.d(" data send 1",Integer.toString(line.length())+line);
                }

                if (line!=null)
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            show();
                        }
                    });
                }

            }catch (Exception e)
            {
                Log.e("=====fetch", getPackageName());
            }


        }
    });

    void show()
    {

        try {
            Uri.Builder eventbuilder1 = new Uri.Builder();

            eventbuilder1.scheme("http")
                    .authority(server_ip)
                    //.appendPath("register")
                    .appendPath("GFF")
                    .appendPath("Cervical.py");

            final URL datalink = new URL(eventbuilder1.build().toString());






                        web.loadUrl(datalink.toString());
                        progress.dismiss();
                   progress.dismiss();

                Log.d(" data send 1","webview");

        }catch (Exception e)
        {
            Log.d("===error", e.toString());
        }
    }
}
