package com.vit.gff.codebusters;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.util.Log;

import android.content.Intent;
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


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink;

    URL datalink;
    String email, password;
    ProgressDialog progressDialog;

    Handler request;

    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.btn_login);
        signupLink = findViewById(R.id.link_signup);
        prefs = this.getSharedPreferences("setting",MODE_PRIVATE);

        request = new Handler();

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.


        login.start();

    }


    Thread login = new Thread(new Runnable() {
        @Override
        public void run() {

            try {
                int flag=0;
                Uri.Builder eventbuilder = new Uri.Builder();

                eventbuilder.scheme("http")
                        .authority(server_ip)
                        .appendPath("GFF")
                        .appendPath("login.php")
                        .appendQueryParameter("uid", email)
                        .appendQueryParameter("upw", password);

                datalink = new URL(eventbuilder.build().toString());

                OkHttpClient client = new OkHttpClient();
                Log.d(getClass().getName()+" URL ", datalink.toString());

                //httpConnection.connect();
                Request.Builder builder1 = new Request.Builder();
                builder1.url(datalink);
                Request request1 = builder1.build();


                Response response1 = client.newCall(request1).execute();

                if (response1.body() != null) {
                    String b = response1.body().string().trim();
                    if (b.equalsIgnoreCase("success"))
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
        loginButton.setEnabled(true);
        prefs.edit().putString("username", email).apply();
        prefs.edit().putBoolean("login", true).apply();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    public void onLoginFailed() {
        prefs.edit().clear().apply();
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        recreate();
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty()) {
            emailText.setError("username cannot be empty");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}
