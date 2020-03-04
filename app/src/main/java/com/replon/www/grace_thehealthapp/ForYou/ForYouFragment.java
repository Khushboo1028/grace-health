package com.replon.www.grace_thehealthapp.ForYou;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.replon.www.grace_thehealthapp.Calories.CaloriesActivity;
import com.replon.www.grace_thehealthapp.Calories.DatabaseHelperCalories;
import com.replon.www.grace_thehealthapp.HeartbeatActivity;
import com.replon.www.grace_thehealthapp.Login.LoginActivity;
import com.replon.www.grace_thehealthapp.NewHeartbeatActivity;
import com.replon.www.grace_thehealthapp.Profile.TimelineActivity;
import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Reminders.DatabaseHelperReminders;
import com.replon.www.grace_thehealthapp.Reminders.RemindersActivity;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;
import com.replon.www.grace_thehealthapp.Utility.UserDataStore;
import com.replon.www.grace_thehealthapp.Walking.DatabaseHelperWalking;
import com.replon.www.grace_thehealthapp.Walking.WalkingActivity;
import com.replon.www.grace_thehealthapp.Water.DatabaseHelperWater;
import com.replon.www.grace_thehealthapp.Water.WaterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForYouFragment extends Fragment {

    public static final String TAG = "ForYouFragment";

    View view;

    //Recycler View
    private RecyclerView recyclerView;
    private RemindersForYouAdapter mAdapter;
    private ArrayList<ContentsRemindersForYou> remindersList;

    CircularImageView img_profile;

    RelativeLayout walking_rel, heartbeat_rel, water_rel, calories_rel, reminders_rel,period_layout;
    TextView walking_text, heartbeat_text, water_text, calories_text,water_value_text, steps_value_text,greeting_text,reminders_time_text;

    TextView name_text,tv_calories;

    //water
    DatabaseHelperWater myDbWater;
    TextView glass_text,glass_info_text;
    int target_glasses = 8;
    int glasses_drank = 0;
    int total_quantity_drank = 0;
    int glass_size =200;


    DatabaseHelperCalories myDbCalorie;
    int target_calories=2000,calories_consumed=0,calories_yet_to_be_consumed;

    TextView tv_walking_target, tv_calories_target;

    //Reminders
    DatabaseHelperReminders myDbReminder;

    SharedPreferences preferences;

    DatabaseHelperWalking myDbWalking;
    Boolean data_with_date_found_walking,getData_with_date_found_calorie;
    int storedSteps = 0;

    String name,profile_image_url, gender, auth_token;

    UserDataStore userDataStore;
    private int target_steps = 6000, steps_more_to_walk;

    public ForYouFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), getActivity());
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_for_you, container, false);

        init();

        viewReminderData();

        Calendar currentDate = Calendar.getInstance();
        greeting_text.setText("Good "+getFormattedTimeText(currentDate.get(Calendar.HOUR_OF_DAY)));

        walking_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), WalkingActivity.class);
                View sharedView = walking_rel;
                String transitionName = getString(R.string.transition_walking);

                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), sharedView, transitionName);
                startActivity(intent, transitionActivityOptions.toBundle());
            }
        });


        heartbeat_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewHeartbeatActivity.class);

                View sharedView = heartbeat_rel;
                String transitionName = getString(R.string.transition_heartbeat);

                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), sharedView, transitionName);
                startActivity(intent, transitionActivityOptions.toBundle());


            }
        });


        water_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WaterActivity.class);
                View sharedView = water_rel;
                String transitionName = getString(R.string.transition_water);

                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), sharedView, transitionName);
                startActivity(intent, transitionActivityOptions.toBundle());
            }
        });

        water_value_text.setText("0");

        viewWaterData();




        calories_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CaloriesActivity.class);
                View sharedView = calories_rel;
                String transitionName = getString(R.string.transition_calories);


                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), sharedView, transitionName);
                startActivity(intent, transitionActivityOptions.toBundle());

            }
        });

        reminders_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RemindersActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_start_activity,R.anim.exit_start_activity);
            }
        });


        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), TimelineActivity.class);
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.enter_start_activity,R.anim.exit_start_activity);

                logout();
            }
        });


        return view;
    }



    private void init(){

        img_profile = (CircularImageView) view.findViewById(R.id.img_profile);

        steps_value_text = (TextView) view.findViewById(R.id.steps_value_text);
        water_value_text=(TextView)view.findViewById(R.id.water_value_text);
        glass_text=(TextView)view.findViewById(R.id.glass_text);
        glass_info_text=(TextView)view.findViewById(R.id.glass_info_text);
        walking_text = (TextView) view.findViewById(R.id.walking_text);
        heartbeat_text = (TextView) view.findViewById(R.id.heartbeat_text);
        greeting_text=(TextView)view.findViewById(R.id.greeting_text);
        reminders_time_text = (TextView) view.findViewById(R.id.reminder_time_text);
        name_text = (TextView) view.findViewById(R.id.name_text);

        reminders_rel = (RelativeLayout) view.findViewById(R.id.reminders_rel);
        calories_rel = (RelativeLayout) view.findViewById(R.id.calories_box);
        water_rel = (RelativeLayout) view.findViewById(R.id.water_box);
        heartbeat_rel = (RelativeLayout) view.findViewById(R.id.heartbeat_box);
        walking_rel = (RelativeLayout) view.findViewById(R.id.walking_box);
        period_layout = (RelativeLayout) view.findViewById(R.id.period_layout);
        period_layout.setVisibility(View.GONE);


        myDbWater=new DatabaseHelperWater(getContext());

        remindersList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.reminders_recycler_view);
        recyclerView.setHasFixedSize(true); //so it doesn't matter if element size increases or decreases
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));//by default manager is vertical


        myDbReminder=new DatabaseHelperReminders(getContext());
        myDbCalorie = new DatabaseHelperCalories(getContext());

        tv_calories = (TextView) view.findViewById(R.id.calories_value_text);


        mAdapter = new RemindersForYouAdapter(getActivity(),remindersList);
        recyclerView.setAdapter(mAdapter);

        preferences = getActivity().getSharedPreferences(getString(R.string.SharedPref), Context.MODE_PRIVATE);

        myDbWalking=new DatabaseHelperWalking(getContext());
        steps_value_text = (TextView) view.findViewById(R.id.steps_value_text);

        userDataStore = new UserDataStore(getActivity());

        steps_more_to_walk = target_steps - storedSteps;
        steps_value_text.setText(String.valueOf(storedSteps));
        tv_walking_target = (TextView) view.findViewById(R.id.target_text);
        tv_walking_target.setText(String.valueOf(steps_more_to_walk) +" "+ getString(R.string.walking_target_text));


        tv_calories_target =(TextView) view.findViewById(R.id.calories_target_text);
        calories_yet_to_be_consumed = target_calories - calories_consumed;
        tv_calories.setText(String.valueOf(calories_consumed));
        tv_calories_target.setText(String.valueOf(calories_yet_to_be_consumed) +" "+ getString(R.string.calories_target_text));









    }

    private void viewWaterData() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date);
        Log.i(TAG,"date is " +formattedDate);

        if(myDbWater.getDataWithDate(formattedDate)!=null){

            Cursor res = myDbWater.getDataWithDate(formattedDate);
            if(res.getCount()==0){
                Log.i(TAG,"There is no stored data");


            }else{
                while(res.moveToNext()){
                    glasses_drank =res.getInt(2);

                }
                water_value_text.setText(String.valueOf(glasses_drank));
            }

            Cursor res2 = myDbWater.getAllData();
            if(res2.getCount()==0){
                Log.i(TAG,"There is no stored data");
                glass_text.setText("Glass of 8");


            }else{

                while(res2.moveToNext()){

                    target_glasses=res2.getInt(1);
                    glass_size=res2.getInt(3);


                }
                glass_text.setText("Glass of "+ String.valueOf(target_glasses));
                glass_info_text.setText("1 Glass = "+ String.valueOf(glass_size) +"mL");

            }
        }


    }

    private void viewReminderData() {

        remindersList.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date date=new Date();
        String dayOfTheWeek = sdf.format(date);

        Calendar currentDate = Calendar.getInstance();

        Cursor res = myDbReminder.getDataWithHourAndDay(getFormattedTimeTextReminders(currentDate.get(Calendar.HOUR_OF_DAY)),dayOfTheWeek);
        if(res.getCount()==0){
            Log.i(TAG,"There is no stored reminders data");

            remindersList.add(new ContentsRemindersForYou("","No Pills",0,false));

            reminders_time_text.setText("");
        }else{
            while(res.moveToNext()){
                remindersList.add(new ContentsRemindersForYou(res.getString(0),res.getString(1),Integer.parseInt(res.getString(5)),Boolean.parseBoolean(res.getString(6))));

                reminders_time_text.setText(getFormattedTimeTextReminders(currentDate.get(Calendar.HOUR_OF_DAY)));

            }

        }

        mAdapter.notifyDataSetChanged();


    }



    private String getFormattedTimeText(int hourOfDay){


        String dose_time_text;
        int hr;
        if(hourOfDay > 12) {
            hr = hourOfDay-12;
        }else {
            hr=hourOfDay;
        }

        if(hourOfDay >= 17){

            dose_time_text = "Evening!";
        }else if(hourOfDay >= 11){


            dose_time_text = "Afternoon!";

        }
        else{
            dose_time_text = "Morning!";

        }
        return dose_time_text;

    }

    private String getFormattedTimeTextReminders(int hourOfDay){


        String dose_time_text;
        int hr;
        if(hourOfDay > 12) {
            hr = hourOfDay-12;
        }else {
            hr=hourOfDay;
        }

        if(hourOfDay >= 17){

            dose_time_text = "Evening";
        }else if(hourOfDay >= 11){


            dose_time_text = "Afternoon";

        }
        else{
            dose_time_text = "Morning";

        }
        return dose_time_text;

    }

    public void viewWalkingData() {

        Log.i(TAG,"ViewDate running");
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date);

        StringBuffer buffer=new StringBuffer();
        if(myDbWalking.getDataWithDate(formattedDate)!=null) {

            Cursor res = myDbWalking.getDataWithDate(formattedDate);
            if (res.getCount() == 0) {
                Log.i(TAG, "There is no stored data");
                data_with_date_found_walking = false;

            }
            else {
                data_with_date_found_walking = true;
                while (res.moveToNext()) {


                    buffer.append("ID: " + res.getString(0) + "\n");
                    buffer.append("steps: " + res.getInt(1) + "\n");
                    buffer.append("target_steps: " + res.getInt(2) + "\n");

                    storedSteps = res.getInt(1);
                    target_steps = res.getInt(2);

                }
                steps_value_text.setText(String.valueOf(storedSteps));
                Log.i(TAG,"\n"+buffer.toString());
                steps_more_to_walk = target_steps - storedSteps;
                tv_walking_target.setText(String.valueOf(steps_more_to_walk) +" "+ getString(R.string.walking_target_text));
//
            }

        }

    }


    private String capitalize(String str){
        String words[]=str.split("\\s");
        String capitalizeWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
    }

    private void updateUIForValues(){

        JSONObject userJSON = userDataStore.readUserData();

        try{
            if (userJSON!=null){

                if (userJSON.has("profile_image_url")){
                    profile_image_url = userJSON.getString("profile_image_url");
                    if ( profile_image_url != null && !profile_image_url.equals("null") && !profile_image_url.equals("")){
                        Glide.with(getActivity()).load(profile_image_url).placeholder(R.drawable.ic_default_profile_image).into(img_profile);
                    }
                }

                if (userJSON.has("name")){
                    name = userJSON.getString("name");
                    name_text.setText(capitalize(name));
                }

                if (userJSON.has("gender")){
                    gender = userJSON.getString("gender");
                    if(gender.equalsIgnoreCase("male")){
                        period_layout.setVisibility(View.GONE);
                    }else {
                        period_layout.setVisibility(View.GONE);
                    }
                }


            }


        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void viewCalorieData() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date);
        Log.i(TAG,"date is " +formattedDate);

        StringBuffer buffer=new StringBuffer();
        if(myDbCalorie.getDataWithDate(formattedDate)!=null){

            Cursor res = myDbCalorie.getDataWithDate(formattedDate);
            if(res.getCount()==0){
                Log.i(TAG,"There is no stored data");
                getData_with_date_found_calorie=false;

            }else{
                getData_with_date_found_calorie=true;
                while(res.moveToNext()){

                    buffer.append("ID:(Date) " +res.getString(0)+"\n" );
                    buffer.append("target_calories: " +res.getInt(1)+"\n");
                    buffer.append("calories_consumed: "+res.getString(2)+"\n\n");

                    calories_consumed=res.getInt(2);
                    tv_calories.setText(String.valueOf(calories_consumed));



                }
                Log.i(TAG,buffer.toString());


            }

            Cursor res2 = myDbCalorie.getAllData();
            StringBuffer buffer2=new StringBuffer();
            if(res2.getCount()==0){
                Log.i(TAG,"There is no stored data");


            }else{

                while(res2.moveToNext()){
                    buffer2.append("ID:(Date) " +res2.getString(0)+"\n" );
                    buffer2.append("target_calories: " +res2.getInt(1)+"\n");
                    buffer2.append("calories_consumed: "+res2.getString(2)+"\n\n");

                    target_calories=res2.getInt(1);

                }

                Log.i(TAG,buffer2.toString());

            }
            calories_yet_to_be_consumed = target_calories - calories_consumed;
            tv_calories_target.setText(String.valueOf(calories_yet_to_be_consumed) + " "+getString(R.string.calories_target_text));
        }

    }


    @Override
    public void onResume() {
        super.onResume();

        viewWaterData();
        viewReminderData();
        viewWalkingData();
        viewCalorieData();
        updateUIForValues();

    }


    private void logout(){
        userDataStore.deleteUserData();
        Intent intent = new Intent(getContext(),LoginActivity.class);
        startActivity(intent);
    }
}
