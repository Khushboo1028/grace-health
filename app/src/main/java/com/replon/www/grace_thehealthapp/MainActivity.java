package com.replon.www.grace_thehealthapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bigscreen.iconictabbar.view.IconicTab;
import com.bigscreen.iconictabbar.view.IconicTabBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.replon.www.grace_thehealthapp.Emergency.EmergencyFragment;
import com.replon.www.grace_thehealthapp.ForYou.ForYouFragment;
import com.replon.www.grace_thehealthapp.Login.LoginActivity;
import com.replon.www.grace_thehealthapp.Magazine.MagazineFragment;
import com.replon.www.grace_thehealthapp.Nearby.NearbyFragment;
import com.replon.www.grace_thehealthapp.SocialMedia.SocialFragment;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;
import com.replon.www.grace_thehealthapp.Utility.UserDataStore;
import com.replon.www.grace_thehealthapp.Walking.DatabaseHelperWalking;
import com.replon.www.grace_thehealthapp.Walking.StepDetector;
import com.replon.www.grace_thehealthapp.Walking.StepListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements SensorEventListener, StepListener {


    private static final String TAG = "MainActivity";
    private IconicTabBar iconicTabBar;
    FragmentTransaction ft;

    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private int storedSteps;
    DatabaseHelperWalking myDb;
    boolean data_with_date_found;

    UserDataStore userDataStore;

    SharedPreferences preferences;
    Fragment target;
    private int target_Steps = 6000;

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), MainActivity.this);


        setContentView(R.layout.activity_main);


        preferences = getSharedPreferences(getString(R.string.SharedPref), Context.MODE_PRIVATE);
        userDataStore = new UserDataStore(MainActivity.this);

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        myDb=new DatabaseHelperWalking(getApplicationContext());

        iconicTabBar = findViewById(R.id.tab_bar);
        ft = getSupportFragmentManager().beginTransaction();
        if (ft.isEmpty()){
            ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frame_tab, new ForYouFragment(),getString(R.string.for_you_tab));
            ft.commit();
        }




        iconicTabBar.setOnTabSelectedListener(new IconicTabBar.OnTabSelectedListener() {

            @Override
            public void onSelected(IconicTab tab, int position) {
//                Log.d(TAG, "selected tab on= " + position);
                int tabId = tab.getId();
                ft = getSupportFragmentManager().beginTransaction();
                switch (tabId) {
                    case R.id.for_you_tab:
                        target = getSupportFragmentManager().findFragmentByTag(getString(R.string.for_you_tab));
                        if (target!=null && target.isAdded() ){
                            Log.i(TAG," FOR YOU FRAGMENT ALREADY ADDED");
                            ft.show(target);

                        }else{
                            Log.i(TAG,"FOR YOU FRAGMENT NOT ADDED");
                            ft.add(R.id.frame_tab, new ForYouFragment(),getString(R.string.for_you_tab));
                        }
                        // Replace the contents of the container with the new fragment
                        // or ft.add(R.id.your_placeholder, new FooFragment());
                        // Complete the changes added above
                        break;

                    case R.id.social_tab:
                        // Replace the contents of the container with the new fragment
//                        ft.replace(R.id.frame_tab, new SocialFragment());
                        target = getSupportFragmentManager().findFragmentByTag(getString(R.string.social_tab));
                        if (target!=null && target.isAdded()){
                            Log.i(TAG," SOCIAL FRAGMENT ALREADY ADDED");
                            ft.show(target);

                        }else{
                            Log.i(TAG,"SOCIAL FRAGMENT NOT ADDED");
                            ft.add(R.id.frame_tab, new SocialFragment(),getString(R.string.social_tab));
                        }

                        break;

                    case R.id.news_tab:
                        // Replace the contents of the container with the new fragment
//                        ft.replace(R.id.frame_tab, new MagazineFragment());
                        target = getSupportFragmentManager().findFragmentByTag(getString(R.string.news_tab));
                        if (target!=null && target.isAdded()){
                            Log.i(TAG," NEWS FRAGMENT ALREADY ADDED");
                            ft.show(target);

                        }else{
                            Log.i(TAG,"NEWS FRAGMENT NOT ADDED");
                            ft.add(R.id.frame_tab, new MagazineFragment(),getString(R.string.news_tab));
                        }
                        break;

                    case R.id.nearby_tab:
                        // Replace the contents of the container with the new fragment
//                        ft.replace(R.id.frame_tab, new NearbyFragment());
                        target = getSupportFragmentManager().findFragmentByTag(getString(R.string.nearby_tab));
                        if (target!=null && target.isAdded()){
                            Log.i(TAG," NEARBY FRAGMENT ALREADY ADDED");
                            ft.show(target);

                        }else{
                            Log.i(TAG,"NEARBY FRAGMENT NOT ADDED");
                            ft.add(R.id.frame_tab, new NearbyFragment(),getString(R.string.nearby_tab));
                        }
                        break;

                    case R.id.emergency_tab:
                        // Replace the contents of the container with the new fragment
//                        ft.replace(R.id.frame_tab, new EmergencyFragment());
                        target = getSupportFragmentManager().findFragmentByTag(getString(R.string.first_aid_tab));
                        if (target!=null && target.isAdded()){
                            Log.i(TAG," EMERGENCY FRAGMENT ALREADY ADDED");
                            ft.show(target);
                        }else{
                            Log.i(TAG,"EMERGENCY FRAGMENT NOT ADDED");
                            ft.add(R.id.frame_tab, new EmergencyFragment(),getString(R.string.first_aid_tab));
                        }
                        break;

                }
                ft.commit();

            }

            @Override
            public void onUnselected(IconicTab tab, int position) {
                Log.d(TAG, "unselected tab on= " + position);
                int tabId = tab.getId();
                ft = getSupportFragmentManager().beginTransaction();
                switch (tabId){
                    case R.id.for_you_tab:
                        target = getSupportFragmentManager().findFragmentByTag(getString(R.string.for_you_tab));
                        Log.i(TAG,"Hiding FOR YOU TAB");
                        break;
                    case R.id.social_tab:
                        target = getSupportFragmentManager().findFragmentByTag(getString(R.string.social_tab));
                        Log.i(TAG,"Hiding SOCIAL TAB");
                        break;
                    case R.id.news_tab:
                        target = getSupportFragmentManager().findFragmentByTag(getString(R.string.news_tab));
                        Log.i(TAG,"Hiding NEWS TAB");
                        break;
                    case R.id.nearby_tab:
                        target = getSupportFragmentManager().findFragmentByTag(getString(R.string.nearby_tab));
                        Log.i(TAG,"Hiding NEARBY TAB");
                        break;
                    case R.id.emergency_tab:
                        target = getSupportFragmentManager().findFragmentByTag(getString(R.string.first_aid_tab));
                        Log.i(TAG,"Hiding EMERGENCY TAB");
                        break;

                }
                ft.hide(target).commit();

            }
        });


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        viewWalkingData();
        storedSteps++;

        Log.i(TAG, "NUMBER OF STEPS " + storedSteps);
        addWalkingData(storedSteps);
//        ForYouFragment fragment = (ForYouFragment)getSupportFragmentManager().findFragmentById(R.id.frame_tab);
//        fragment.updateSteps(storedSteps);
    }

    private void addWalkingData(int steps) {


        if(!data_with_date_found){
            boolean isInserted= myDb.insertData(steps,target_Steps);

            if(isInserted){
                Log.i(TAG,"Walking steps saved");
                data_with_date_found=true;
            }else{
                Log.i(TAG,"Unable to save walking steps");

            }
        }else{
            boolean isInserted= myDb.updateData(steps,target_Steps);

            if(isInserted){
                Log.i(TAG,"Walking steps updated");
            }else{
                Log.i(TAG,"Unable to update walking steps");

            }
        }

    }

    public void viewWalkingData() {

        Log.i(TAG,"ViewDate running");
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date);

        StringBuffer buffer=new StringBuffer();
        if(myDb.getDataWithDate(formattedDate)!=null) {

            Cursor res = myDb.getDataWithDate(formattedDate);
            if (res.getCount() == 0) {
                Log.i(TAG, "There is no stored data");
                data_with_date_found = false;

                storedSteps=0;


            }
            else {
                data_with_date_found = true;
                while (res.moveToNext()) {


                    buffer.append("ID: " + res.getString(0) + "\n");
                    buffer.append("steps: " + res.getInt(1) + "\n");
                    buffer.append("target_steps: " + res.getInt(2) + "\n");

                    storedSteps = res.getInt(1);
                    target_Steps = res.getInt(2);

                }
                Log.i(TAG,"\n"+buffer.toString());

            }

        }

    }



    private void checkCurrentUser(){

//        JSONObject userJSON = userDataStore.readUserData();
//        String auth_token;
//
//        try{
//            if (userJSON!=null){
//                auth_token = userJSON.getString("auth_token");
//                if (auth_token.equals("null") || auth_token.equals("")){
//                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                    startActivity(intent);
//                }else{
//                    Log.i(TAG,"AUTH TOKEN IS "+ auth_token);
//                }
//            }else {
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
//            }
//
//        }catch (JSONException e){
//            e.printStackTrace();
//        }

        //Firebase Login Code

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        if(firebaseUser!=null){
            Log.i(TAG,"AUTH FIREBASE TOKEN IS "+ firebaseUser.getUid());
        }else{
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }



    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
        checkCurrentUser();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
