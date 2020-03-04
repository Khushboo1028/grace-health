package com.replon.www.grace_thehealthapp.Reminders;

import android.app.AlertDialog;
import android.app.TimePickerDialog;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;
import com.replon.www.grace_thehealthapp.Utility.MultiSpinner;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddReminderActivity extends AppCompatActivity {


    private static final String TAG = "AddRemindersActivity";
    ImageView back, add_dose, minus_dose;
    EditText et_name_dose;
    int HOUR = -1,MINUTE = -1;

    String days[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    TextView dose_amount,tv_time_dose;

    TextView tv_taken_dose;
    List<Integer> spinnerSelectionList,everyday,weekdays,weekends;

    int doseCnt = 1;

    String name_of_dose,time_of_dose,hour_of_dose,how_often,number_of_pills;
    Button btn_save;
    DatabaseHelperReminders myDb;
    List<String> how_often_array;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(),  AddReminderActivity.this);

        setContentView(R.layout.activity_add_reminder);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getColor(R.color.violet));
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        add_dose = (ImageView)findViewById(R.id.add_dose_image);
        minus_dose = (ImageView)findViewById(R.id.remove_dose_image);

        dose_amount = (TextView)findViewById(R.id.dose_amount);
        dose_amount.setText(String.valueOf(doseCnt));

//        taken_dose_spinner = (MultiSpinner)findViewById(R.id.taken_dose_spinner);
        myDb=new DatabaseHelperReminders(this);

        add_dose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(doseCnt < 5){
                    doseCnt++;
//                    add_dose.setColorFilter(R.color.lightGrey_text, PorterDuff.Mode.MULTIPLY);
                    add_dose.setImageResource(R.drawable.ic_add_dose_dark);
                    minus_dose.setImageResource(R.drawable.ic_minus_dose_dark);
                    if (doseCnt == 5){
                        add_dose.setImageResource(R.drawable.ic_add_dose_light);
                    }

                }else{
                    add_dose.setImageResource(R.drawable.ic_add_dose_light);
                }
                dose_amount.setText(String.valueOf(doseCnt));
            }
        });

        minus_dose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(doseCnt > 1){
                    doseCnt--;
                    minus_dose.setImageResource(R.drawable.ic_minus_dose_dark);
                    add_dose.setImageResource(R.drawable.ic_add_dose_dark);
                    if (doseCnt == 1){
                        minus_dose.setImageResource(R.drawable.ic_minus_dose_light);
                    }

                }
                dose_amount.setText(String.valueOf(doseCnt));

            }
        });

        et_name_dose = (EditText)findViewById(R.id.et_name_dose);
        tv_time_dose = (TextView)findViewById(R.id.tv_time_dose);
        Calendar currentDate = Calendar.getInstance();
        tv_time_dose.setText(getFormattedTimeText(currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE)));
        int i=getFormattedTimeText(currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE)).indexOf(",");
        time_of_dose=getFormattedTimeText(currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE)).substring(i+2);

        hour_of_dose=getFormattedTimeText(currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE)).substring(0,i);

        tv_time_dose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentDate = Calendar.getInstance();
                if(HOUR == -1 && MINUTE == -1){
                    HOUR = currentDate.get(Calendar.HOUR_OF_DAY);
                    MINUTE = currentDate.get(Calendar.MINUTE);
                }

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddReminderActivity.this,R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        int i=getFormattedTimeText(hourOfDay,minute).indexOf(",");
                        time_of_dose=getFormattedTimeText(hourOfDay,minute).substring(i+2);
                        Log.i(TAG,"time of dose is "+time_of_dose);
                        hour_of_dose=getFormattedTimeText(hourOfDay,minute).substring(0,i);
                        Log.i(TAG,"hour of dose is "+hour_of_dose);
                        tv_time_dose.setText(getFormattedTimeText(hourOfDay,minute));
                        HOUR = hourOfDay;
                        MINUTE = minute;

                    }
                }, HOUR, MINUTE,false);

                timePickerDialog.show();

            }
        });



        tv_taken_dose = (TextView) findViewById(R.id.tv_taken_dose_spinner);


        spinnerSelectionList = Arrays.asList(0,1,2,3,4,5,6);
        everyday = spinnerSelectionList;
        weekdays = Arrays.asList(0,1,2,3,4);
        weekends = Arrays.asList(5,6);


        how_often_array=new ArrayList<>();

        how_often_array.add("Monday");
        how_often_array.add("Tuesday");
        how_often_array.add("Wednesday");
        how_often_array.add("Thursday");
        how_often_array.add("Friday");
        how_often_array.add("Saturday");
        how_often_array.add("Sunday");

        how_often = ("" + how_often_array).replaceAll("(^.|.$)", "  ").replace(", ", ", " );

        tv_taken_dose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiSpinner multiSpinner = new MultiSpinner(AddReminderActivity.this);
                multiSpinner.setItems(days);
                multiSpinner.setSelectionIntList(spinnerSelectionList);

                multiSpinner.setListener(new MultiSpinner.OnMultipleItemsSelectedListener() {
                    @Override
                    public void selectedIndices(List<Integer> indices) {

                        spinnerSelectionList = indices;
                        if(spinnerSelectionList.equals(everyday)){

                            tv_taken_dose.setText("Everyday");
                            Log.i(TAG,"In everyday");
                            how_often_array.clear();
                            how_often_array.add("Monday");
                            how_often_array.add("Tuesday");
                            how_often_array.add("Wednesday");
                            how_often_array.add("Thursday");
                            how_often_array.add("Friday");
                            how_often_array.add("Saturday");
                            how_often_array.add("Sunday");

                            how_often = ("" + how_often_array).replaceAll("(^.|.$)", "  ").replace(", ", ", " );
                            Log.i(TAG,"how often String is "+how_often);
                        }else if(spinnerSelectionList.equals(weekends)){
                            Log.i(TAG,"In Weekend");
                            tv_taken_dose.setText("Every Weekend");
                            how_often_array.clear();
                            how_often_array.add("Saturday");
                            how_often_array.add("Sunday");

                            how_often = ("" + how_often_array).replaceAll("(^.|.$)", "  ").replace(", ", ", " );
                            Log.i(TAG,"how often String is "+how_often);
                        }else if(spinnerSelectionList.equals(weekdays)){

                            tv_taken_dose.setText("Every Weekday");

                            how_often_array.clear();
                            how_often_array.add("Monday");
                            how_often_array.add("Tuesday");
                            how_often_array.add("Wednesday");
                            how_often_array.add("Thursday");
                            how_often_array.add("Friday");

                            how_often = ("" + how_often_array).replaceAll("(^.|.$)", "  ").replace(", ", ", " );
                            Log.i(TAG,"how often String is "+how_often);

                        }else{
                            String spinText = "Every ";
                            how_often_array.clear();
                            for(int i : indices){
                                spinText += days[i].substring(0,3)+ ", ";
                                how_often_array.add(days[i]);
                            }
                            spinText = spinText.substring(0,spinText.length()-2);
                            tv_taken_dose.setText(spinText);

                            how_often = ("" + how_often_array).replaceAll("(^.|.$)", "  ").replace(", ", ", " );
                            Log.i(TAG,"how often String is "+how_often);

                        }

//                        if(indices.isEmpty()){
//                    showMessage("Error!","Please select atleast one day",R.drawable.ic_error_dialog);
//                      }




                    }

                    @Override
                    public void selectedStrings(List<String> strings) {

                    }
                });
                multiSpinner.performClick();



            }
        });



        btn_save=(Button)findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"how often array is "+how_often_array);
                name_of_dose=et_name_dose.getText().toString().trim();
                if(name_of_dose.isEmpty()){
                    showMessage("Oops!","Please add the name of the dose");
                }else{
                    saveData();
                }

            }
        });



    }

    private void saveData() {

        if(time_of_dose==null || hour_of_dose==null || how_often==null||how_often.isEmpty()){
            showMessage("Oops","Please enter all data");
        }else{
            btn_save.setEnabled(false);
            boolean isInserted= myDb.insertData(name_of_dose,time_of_dose,hour_of_dose,how_often,String.valueOf(doseCnt),"false");
            Log.i(TAG,"Data saved");

            if(isInserted){
                Log.i(TAG,"Reminders data saved");
//                showMessage("Success","This dose has been recorded");
//                btn_save.setEnabled(true);
//                time_of_dose=null;
//                hour_of_dose=null;
//                name_of_dose=null;
//                et_name_dose.setText("");
               onBackPressed();
            }else{
                Log.i(TAG,"Unable to dose");
                showMessage("Oops","An error occurred");
            }
        }

    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(AddReminderActivity.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }

    private String getFormattedTimeText(int hourOfDay, int minute){

        final DecimalFormat formatter = new DecimalFormat("00");

        String dose_time_text;
        int hr;
        if(hourOfDay > 12) {
            hr = hourOfDay-12;
        }else {
            hr=hourOfDay;
        }

        if(hourOfDay >= 17){

            dose_time_text = "Evening, " + formatter.format(hr) + " : " + formatter.format(minute) + " PM";
        }else if(hourOfDay >= 11){


            dose_time_text = "Afternoon, " + formatter.format(hr) + " : " + formatter.format(minute);

            if (hourOfDay == 11){
                dose_time_text += " AM";
            }else{
                dose_time_text += " PM";
            }
        }
        else{
            dose_time_text = "Morning, " + formatter.format(hr) + " : " + formatter.format(minute) + " AM";

        }
        return dose_time_text;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }


}
