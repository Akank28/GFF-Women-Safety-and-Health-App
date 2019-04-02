package com.vit.gff.codebusters;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by suraj on 11/9/18.
 */

public class Setting extends AppCompatActivity {

    ImageView back ;
    TextView uname, email, emergency, logout;

    SharedPreferences read_setting;
    SharedPreferences.Editor editor;

    Button done, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        back = findViewById(R.id.setting_back);
        uname = findViewById(R.id.setting_username);
        email = findViewById(R.id.setting_email);
        emergency = findViewById(R.id.setting_emergency);
        logout = findViewById(R.id.setting_logout);

        read_setting = getSharedPreferences("setting",MODE_PRIVATE);
        editor = read_setting.edit();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        uname.setText(read_setting.getString("username","Name"));
        email.setText(read_setting.getString("email","example@example.com"));

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(getApplicationContext());
                final Dialog dialog = new Dialog(Setting.this);


                dialog.setContentView(R.layout.emergency_contact);
                dialog.setCancelable(false);
                dialog.create();

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                EditText userInput1 = (EditText) dialog.findViewById(R.id.con1);
                EditText userInput2 = (EditText) dialog.findViewById(R.id.con2);

                String t1 =read_setting.getString("emergency1",null);
                String t2 =read_setting.getString("emergency2",null);
                if (t1!=null)
                    userInput1.setText(t1);

                if(t2!=null)
                    userInput2.setText(t2);


                done = dialog.findViewById(R.id.emer_done);
                cancel = dialog.findViewById(R.id.emer_cancel);

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText temp = dialog.findViewById(R.id.con1);
                        if (temp.getText().toString().length()!=0)
                            editor.putString("emergency1",temp.getText().toString().trim());

                        temp = dialog.findViewById(R.id.con2);
                        if (temp.getText().toString().length()!=0)
                            editor.putString("emergency2",temp.getText().toString().trim());

                        editor.apply();
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.getWindow().setAttributes(lp);


                // set dialog message

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                read_setting.edit().clear().apply();
                                Intent intent = new Intent(Setting.this, Splash.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}