package vit.codebuster.com.codebusters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static vit.codebuster.com.codebusters.backservice.RootWork.server_ip;

public class health_analysis extends AppCompatActivity {


    ImageView back;
    RadioGroup option;
    Button next;
    Handler s;
    EditText input;
    TextView ques, result;

    int curr=0;
    int selected=0;

    List<String> output;

    String id;
    List<String> head;
    int type[] = new int[]{1,0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};

    ProgressDialog progress;
    RadioButton rd1, rd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_analysis);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        progress=new ProgressDialog(this);
        progress.setMessage("Evaluating On Your Data");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progress.show();

        result = findViewById(R.id.analysis_result);
        ques = findViewById(R.id.analysis_ques);
        input = findViewById(R.id.analysis_input);
        output =  new ArrayList<>();
        back = findViewById(R.id.analysis_back);
        option = findViewById(R.id.analysis_option);
        next = findViewById(R.id.analysis_next);
        s = new Handler();
        rd1 = findViewById(R.id.rd1);
        rd2 = findViewById(R.id.rd2);


        id ="Hormonal Contraceptives,Hormonal Contraceptives (years)" +
                ",IUD,IUD (years),STDs,STDs (number),STDs:condylomatosis," +
                "STDs:cervical condylomatosis,STDs:vaginal condylomatosis," +
                "STDs:vulvo-perineal condylomatosis,STDs:syphilis,STDs:pelvic inflammatory disease," +
                "STDs: Number of diagnosis,STDs: Time since first diagnosis," +
                "STDs: Time since last diagnosis,Dx:Cancer,Dx,Hinselmann,Schiller,Citology,Biopsy";
        head = Arrays.asList(id.split(","));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        next_question();

        rd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected=0;
            }
        });
        rd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected=1;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (input==null)
                {
                    Toast.makeText(health_analysis.this,"Empty Field !", Toast.LENGTH_SHORT).show();
                }else {

                    if(type[curr]==0) {

                        output.add(input.getText().toString().trim());
                    }
                    else
                    {
                        output.add(Integer.toString(selected));
                    }
                    curr++;
                    next_question();
                }
            }
        });
    }




        /*public void set_options() {



                final RadioButton b = new RadioButton(getApplicationContext());
                b.setText("False");
                final RadioButton c = new RadioButton(getApplicationContext());
                c.setText("True");

                s.post(new Runnable() {
                    @Override
                    public void run() {

                        option.addView(b);
                        option.addView(c);

                        progress.dismiss();
                    }
                });


        }*/




    void next_question()
    {

        if (curr==type.length)
        {
            progress.show();
            ques.setVisibility(View.GONE);
            option.setVisibility(View.GONE);
            input.setVisibility(View.GONE);
            findViewById(R.id.scrool_ques).setVisibility(View.GONE);
            fetch_predict.start();
            TextView x= findViewById(R.id.analysis_result);
            x.setText("Analysing");
            x.setVisibility(View.VISIBLE);


        }else{
            ques.setText(head.get(curr));
            //option.removeAllViews();
            if (type[curr]==0)
            {
                option.setVisibility(View.INVISIBLE);
                input.setVisibility(View.VISIBLE);

            }
            else
            {
                input.setVisibility(View.GONE);
                option.setVisibility(View.VISIBLE);

                //set_options ();
            }
        }
    }

    Thread fetch_predict = new Thread(new Runnable() {
        @Override
        public void run() {
            URL datalink=null;
            try {


                String idList = output.toString();
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
                    Uri.Builder eventbuilder1 = new Uri.Builder();
                    eventbuilder1.scheme("http")
                            .authority(server_ip)
                            //.appendPath("register")
                            .appendPath("GFF")
                            .appendPath("Cervical.py");

                    datalink = new URL(eventbuilder1.build().toString());

                    client = new OkHttpClient();
                    Log.d("URL ",datalink.toString());

                    //httpConnection.connect();
                    builder1 = new Request.Builder();
                    builder1.url(datalink);
                    request1 = builder1.build();


                    response1 = client.newCall(request1).execute();
                    if( response1.body() != null)
                    {
                        final String res= response1.body().string();
                        s.post(new Runnable() {
                            @Override
                            public void run() {

                                Spanned htmlAsSpanned = Html.fromHtml(res);
                                result.setText(htmlAsSpanned);
                                progress.dismiss();
                            }
                        });

                        Log.d(" data send 1",Integer.toString(line.length())+line);
                    }

                }

            }catch (Exception e)
            {
                Log.e("=====fetch", getPackageName());
            }


        }
    });

}
