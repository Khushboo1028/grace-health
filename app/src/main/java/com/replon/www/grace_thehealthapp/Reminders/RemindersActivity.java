package com.replon.www.grace_thehealthapp.Reminders;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RemindersActivity extends AppCompatActivity {

    public static final String TAG = "RemindersActivity";
    ImageView back;
    TextView add_dose,date_text,no_dose_text;
    LinearLayout display_date;
    DecimalFormat formatter;
    int YEAR, MONTH, DATE;
    String dayOfTheWeek;
    Boolean[] dose_today;

    LottieAnimationView angel_animation;
    private RecyclerView recyclerViewMorning, recyclerViewAfternoon, recyclerViewEvening;
    private PillRemindersAdapter mAdapterMorning,mAdapterAfternoon, mAdapterEvening;
    private ArrayList<ContentsPillReminders> remindersListMorning,remindersListAfternoon,remindersListEvening;
    DatabaseHelperReminders myDb;

    private RelativeLayout morningViewRel, afternoonViewRel, eveningViewRel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(),  RemindersActivity.this);

        setContentView(R.layout.activity_reminders);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.violet));
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        dose_today = new Boolean[]{false,false,false};

        no_dose_text = (TextView)findViewById(R.id.no_dose_text);
        angel_animation=(LottieAnimationView)findViewById(R.id.smile_animation);


        myDb=new DatabaseHelperReminders(this);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.mainrel);
        relativeLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                InputMethodManager imm =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }
        });

        morningViewRel=(RelativeLayout)findViewById(R.id.morning_rel);
        afternoonViewRel=(RelativeLayout)findViewById(R.id.afternoon_rel);
        eveningViewRel=(RelativeLayout)findViewById(R.id.evening_rel);

        remindersListMorning = new ArrayList<>();
        remindersListAfternoon=new ArrayList<>();
        remindersListEvening=new ArrayList<>();

        recyclerViewMorning = (RecyclerView) findViewById(R.id.recycler_view_morning);
        recyclerViewMorning.setHasFixedSize(true); //so it doesn't matter if element size increases or decreases


        mAdapterMorning = new PillRemindersAdapter(this, remindersListMorning);
        recyclerViewMorning.setAdapter(mAdapterMorning);


        recyclerViewAfternoon = (RecyclerView) findViewById(R.id.recycler_view_afternoon);
        recyclerViewAfternoon.setHasFixedSize(true); //so it doesn't matter if element size increases or decreases

        mAdapterAfternoon = new PillRemindersAdapter(this, remindersListAfternoon);
        recyclerViewAfternoon.setAdapter(mAdapterAfternoon);


        recyclerViewEvening = (RecyclerView) findViewById(R.id.recycler_view_evening);
        recyclerViewEvening.setHasFixedSize(true); //so it doesn't matter if element size increases or decreases

        mAdapterEvening = new PillRemindersAdapter(this, remindersListEvening);
        recyclerViewEvening.setAdapter(mAdapterEvening);


        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date date=new Date();
        dayOfTheWeek = sdf.format(date);
        Log.i(TAG,"day of the week is "+dayOfTheWeek);

        add_dose = (TextView)findViewById(R.id.add_dose_text);
        add_dose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddReminderActivity.class);
                startActivity(intent);
            }
        });


        formatter = new DecimalFormat("00");
        display_date=(LinearLayout)findViewById(R.id.date_picker_layout);
        date_text = (TextView)findViewById(R.id.date_text);

        Calendar currentDate = Calendar.getInstance();
        DATE = currentDate.get(Calendar.DATE);
        MONTH = currentDate.get(Calendar.MONTH);
        YEAR = currentDate.get(Calendar.YEAR);


        display_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(RemindersActivity.this,R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String date_format;

                        if(year==currentDate.get(Calendar.YEAR) && month==currentDate.get(Calendar.MONTH) &&  dayOfMonth ==currentDate.get(Calendar.DATE)){
                            date_format = "Today";

                            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                            Date date=new Date();
                            dayOfTheWeek = sdf.format(date);
                            Log.i(TAG,"day of the week is "+dayOfTheWeek);
                            viewMorningData(dayOfTheWeek);
                            viewAfternoonData(dayOfTheWeek);
                            viewEveningData(dayOfTheWeek);
                            setNoDoseText();

                        }
                        else{
                            date_format = formatter.format(dayOfMonth) + "/" + formatter.format(month+1)  + "/" + year;

                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            Log.i(TAG,"Date is "+date_format);
                            try {
                                Date date = format.parse(date_format);
                                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                                dayOfTheWeek = sdf.format(date);
                                Log.i(TAG,"Day of the week is "+dayOfTheWeek);

                               viewMorningData(dayOfTheWeek);
                               viewAfternoonData(dayOfTheWeek);
                               viewEveningData(dayOfTheWeek);
                               setNoDoseText();

                            } catch (ParseException e) {
                                e.printStackTrace();
                                Log.i(TAG,"Error is "+e.getMessage());
                            }
                        }
                        date_format += " ▼";
                        date_text.setText(date_format);
                        DATE = dayOfMonth;
                        YEAR = year;
                        MONTH = month;



                    }
                },YEAR, MONTH, DATE);
                datePickerDialog.show();
            }
        });






    }


    private void viewMorningData(String day){


        Cursor res = myDb.getDataWithHourAndDay("Morning",day);
        StringBuffer buffer=new StringBuffer();
        if(res.getCount()==0){
            Log.i(TAG,"There is no stored data in  Morning");
            morningViewRel.setVisibility(View.GONE);
            dose_today[0]=false;


        }else {
            morningViewRel.setVisibility(View.VISIBLE);
            dose_today[0]=true;


            remindersListMorning.clear();
            while (res.moveToNext()) {
                remindersListMorning.add(new ContentsPillReminders(
                        res.getString(0),
                        res.getString(1),
                        res.getString(2),
                        Integer.parseInt(res.getString(5)),
                        Boolean.parseBoolean(res.getString(6))
                ));


            }


            mAdapterMorning.notifyDataSetChanged();

        }

    }

    private void viewAfternoonData(String day){


        Cursor res = myDb.getDataWithHourAndDay("Afternoon",day);
        StringBuffer buffer=new StringBuffer();
        if(res.getCount()==0){
        Log.i(TAG,"There is no stored data in  Afternoon");
        afternoonViewRel.setVisibility(View.GONE);
        dose_today[1]=false;


        }else {

            afternoonViewRel.setVisibility(View.VISIBLE);
            dose_today[1]=true;

            remindersListAfternoon.clear();
            while (res.moveToNext()) {
                remindersListAfternoon.add(new ContentsPillReminders(
                        res.getString(0),
                        res.getString(1),
                        res.getString(2),
                        Integer.parseInt(res.getString(5)),
                        Boolean.parseBoolean(res.getString(6))
                ));

            }



            mAdapterAfternoon.notifyDataSetChanged();

        }

    }

    private void viewEveningData(String day){


        Cursor res = myDb.getDataWithHourAndDay("Evening",day);
        StringBuffer buffer=new StringBuffer();
        if(res.getCount()==0){
            Log.i(TAG,"There is no stored data in getDataWithHourAndDate");
            eveningViewRel.setVisibility(View.GONE);
            dose_today[2]=false;


        }else {
            eveningViewRel.setVisibility(View.VISIBLE);
            dose_today[2]=true;

            remindersListEvening.clear();
            while (res.moveToNext()) {
                remindersListEvening.add(new ContentsPillReminders(
                        res.getString(0),
                        res.getString(1),
                        res.getString(2),
                        Integer.parseInt(res.getString(5)),
                        Boolean.parseBoolean(res.getString(6))


                ));

                buffer.append("ID: "+res.getString(0)+"\n");
                buffer.append("name_of_dose: "+res.getString(1)+"\n");
                buffer.append("time_of_dose: "+res.getString(2)+"\n");
                buffer.append("hour_of_dose: "+res.getString(3)+"\n");
                buffer.append("how_often: "+res.getString(4)+"\n");
                buffer.append("pills_in_dose: "+res.getString(5)+"\n");
                buffer.append("pill_taken: "+res.getString(6)+"\n\n");

            }

           Log.i(TAG,"data is "+buffer.toString());


            mAdapterAfternoon.notifyDataSetChanged();


        }

    }


    public void viewData(){

                Cursor res = myDb.getAllData();

                if(res.getCount()==0){
                    Log.i(TAG,"There is no data");
                    showMessage("Error","No data found");

                }else{
                    StringBuffer buffer=new StringBuffer();

                    while(res.moveToNext()){
                        buffer.append("ID: "+res.getString(0)+"\n");
                        buffer.append("name_of_dose: "+res.getString(1)+"\n");
                        buffer.append("time_of_dose: "+res.getString(2)+"\n");
                        buffer.append("hour_of_dose: "+res.getString(3)+"\n");
                        buffer.append("how_often: "+res.getString(4)+"\n");
                        buffer.append("pills_in_dose: "+res.getString(5)+"\n");
                        buffer.append("pill_taken: "+res.getString(6)+"\n\n");


                    }

                    //show all data
                    showMessage("Data",buffer.toString());
                }

    }

    private void showMessage(String title, String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(RemindersActivity.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }

    private void setNoDoseText(){
        int i=0;
        for (i=0;i<dose_today.length;i++){
            if(dose_today[i]){
                no_dose_text.setVisibility(View.GONE);
                angel_animation.setVisibility(View.GONE);
                break;
            }
        }
        Log.i(TAG, "i="+i);
        if(i==dose_today.length){
            angel_animation.setVisibility(View.VISIBLE);
            no_dose_text.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.enter_finish_activity,R.anim.exit_finish_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date date=new Date();
        dayOfTheWeek = sdf.format(date);
        Log.i(TAG,"day of the week is "+dayOfTheWeek);

        viewMorningData(dayOfTheWeek);
        viewAfternoonData(dayOfTheWeek);
        viewEveningData(dayOfTheWeek);
        setNoDoseText();

    }
}
