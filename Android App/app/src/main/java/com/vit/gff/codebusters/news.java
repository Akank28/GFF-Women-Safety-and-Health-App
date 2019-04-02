package com.vit.gff.codebusters;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import static com.vit.gff.codebusters.RootWork.server_ip;

public class news extends AppCompatActivity {

    WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        web = findViewById(R.id.news_web);


        Uri.Builder eventbuilder1 = new Uri.Builder();

        eventbuilder1.scheme("http")
                .appendPath(server_ip)
                .appendPath("GFF")
                .appendPath("schemes.php");

        web.loadUrl(eventbuilder1.build().toString());

    }
}
