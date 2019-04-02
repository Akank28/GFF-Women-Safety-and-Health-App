package com.vit.gff.codebusters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by suraj on 11/9/18.
 */

public class homeFragment extends Fragment {


    LinearLayout emergency, news, period, health;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home,null);

        emergency = root.findViewById(R.id.emergency_home);
        news = root.findViewById(R.id.news_home);
        period = root.findViewById(R.id.period_home);
        health = root.findViewById(R.id.health_home);

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        emergency.setOnClickListener(clickBtn);
        news.setOnClickListener(clickBtn);
        period.setOnClickListener(clickBtn);
        health.setOnClickListener(clickBtn);
    }

    View.OnClickListener clickBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.emergency_home:
                    startActivity(new Intent(getContext(), emergency.class));
                    break;
                case R.id.news_home:
                    startActivity(new Intent(getContext(),news.class));
                    Toast.makeText(getContext(),"NEWS",Toast.LENGTH_LONG).show();
                    break;
                case R.id.period_home:
                    startActivity(new Intent(getContext(), datepicker.class));
                    Toast.makeText(getContext(),"PERIOD",Toast.LENGTH_LONG).show();
                    break;
                case R.id.health_home:
                    Toast.makeText(getContext(),"HEALTH",Toast.LENGTH_LONG).show();
                    break;
                default:break;
            }
        }
    };
}
