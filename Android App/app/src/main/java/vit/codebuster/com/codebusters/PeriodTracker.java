package vit.codebuster.com.codebusters;

import android.app.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PeriodTracker extends Fragment {

    Button mon1, mon2, mon3;
    TextView date1, date2, date3, predict;
    final List<String> start=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.period_tracker, container, false);

        mon1 = rootview.findViewById(R.id.pick_date_button);
        mon2 = rootview.findViewById(R.id.pick_date_button2);
        mon3 = rootview.findViewById(R.id.pick_date_button3);
        date1 = rootview.findViewById(R.id.display_date);
        date2 = rootview.findViewById(R.id.display_date2);
        date3 = rootview.findViewById(R.id.display_date3);
        predict = rootview.findViewById(R.id.period_predict);

        // Inflate the layout for this fragment
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);





        final Dialog dialog = new Dialog(getContext());
// Include dialog.xml file
        dialog.setContentView(R.layout.datepicker);

        mon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();

                get_date(dialog, date1);
            }
        });
        mon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();

                get_date(dialog, date2);
            }
        });
        mon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();

                get_date(dialog, date3);


            }
        });

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                predictPeriod();
            }
        });








        // System.out.println(month);



    }

    void predictPeriod()
    {
        try {

            //List<String> end=new ArrayList<>();

            List<Date> sdates = new ArrayList<>();
            //List<Date> edates=new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                sdates.add(new SimpleDateFormat("dd/M/yyyy").parse(start.get(i)));
                //edates.add(new SimpleDateFormat("dd/MM/yyyy").parse(end.get(i)));
                Log.d("==========an",start.get(i));
            }

            Calendar myCal = new GregorianCalendar();
            int month;
            long diff;
            float days;
            float avg = 0;
            for (int j = 1; j < 3; j++) {
                diff = sdates.get(j - 1).getTime() - sdates.get(j).getTime();
                days = diff / (1000 * 60 * 60 * 24);
                avg += days;
            }
            avg = avg / 2;
            avg = Math.abs(avg);
            avg = Math.round(avg);
            int av = (int) avg;
            System.out.println(avg);
            Calendar c= Calendar.getInstance();

            Date newDate = addDays(sdates.get(2), av);
   /*  SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
     Calendar c= Calendar.getInstance();
     c.setTime(sdf.parse(start.get(3)));
     c.add(Calendar.DATE,av);
     String newDate=sdf.format(c.getTime());*/
            System.out.println(newDate);

            predict.setText(DateFormat.getDateInstance().format(newDate));
        }catch (Exception e)
        {
            Log.e("===error",Package.class.toString());
        }
    }

    void get_date(final Dialog dialog, final TextView temp)
    {
        final DatePicker picked = dialog.findViewById(R.id.datePicker);

        Button declineButton = (Button) dialog.findViewById(R.id.date_cancel);
// if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });

        Button doneButton = (Button) dialog.findViewById(R.id.date_done);
// if decline button is clicked, close the custom dialog
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = picked.getDayOfMonth();
                int mon = picked.getMonth()+1;
                int year = picked.getYear();

                start.add(day+"/"+mon+"/"+year);
                temp.setText(day+"/"+mon+"/"+year);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }

}
