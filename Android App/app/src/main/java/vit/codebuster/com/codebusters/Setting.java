package vit.codebuster.com.codebusters;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import static vit.codebuster.com.codebusters.backservice.RootWork.MY_PREFS_NAME;

public class Setting extends AppCompatActivity {

    ImageView back ;
    TextView profile, emergency;

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
        profile = findViewById(R.id.setting_profile);
        emergency = findViewById(R.id.setting_emergency);

        read_setting = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        editor = read_setting.edit();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
