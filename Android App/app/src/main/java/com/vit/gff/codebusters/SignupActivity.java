package com.vit.gff.codebusters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.vit.gff.codebusters.RootWork.server_ip;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText username;
    EditText emailText;
    EditText passwordText;
    Button SignupBtn;
    TextView LoginLink;

    ProgressDialog progressDialog;
    Handler request;
    String email,uname, password;

    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.input_username_sign);
        emailText = findViewById(R.id.input_email_sign);
        passwordText = findViewById(R.id.input_password_sign);
        SignupBtn = findViewById(R.id.btn_sign);
        LoginLink = findViewById(R.id.link_login);

        request= new Handler();
        progressDialog = new ProgressDialog(this);

        prefs = getSharedPreferences("setting", MODE_PRIVATE);

        SignupBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        LoginLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        SignupBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        uname = username.getText().toString();

        // TODO: Implement your own authentication logic here.

        signup.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        SignupBtn.setEnabled(true);
        prefs.edit().putString("username",uname).apply();
        prefs.edit().putString("email",email).apply();
        prefs.edit().putBoolean("login",true).apply();


        Toast.makeText(SignupActivity.this,"Account Created Successfully", Toast.LENGTH_LONG).show();
        startActivity(new Intent(SignupActivity.this, MainActivity.class));
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        recreate();
        SignupBtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String uname = username.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty()|| !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        }
        else {
            emailText.setError(null);
        }

        if (uname.isEmpty()){
            username.setError("Enter your Username");
            valid =false;
        }
        else {
            username.setError(null);
        }


        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

    Thread signup = new Thread(new Runnable() {
        @Override
        public void run() {

            try {
                int flag=0;
                Uri.Builder eventbuilder = new Uri.Builder();

                eventbuilder.scheme("http")
                        .authority(server_ip)
                        .appendPath("GFF")
                        .appendPath("register.php")
                        .appendQueryParameter("email",email)
                        .appendQueryParameter("un", uname)
                        .appendQueryParameter("pw", password);

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

                                onLoginSuccess();
                                // onLoginFailed();
                                progressDialog.dismiss();

                            }
                        });
                    }else {

                        request.post(new Runnable() {
                            @Override
                            public void run() {

                                onLoginFailed();
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



}
