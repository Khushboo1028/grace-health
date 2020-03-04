package com.replon.www.grace_thehealthapp.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.replon.www.grace_thehealthapp.R;

import org.json.JSONObject;

public class UserDataStore {

    SharedPreferences preferences;

    Activity activity;

    public UserDataStore(Activity activity) {
        this.activity = activity;
    }

    public void storeUserData(JSONObject userJSON){
        preferences = activity.getSharedPreferences(activity.getString(R.string.SharedPref), Context.MODE_PRIVATE);
        //--SAVE Data
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        Gson gson = new Gson();
        String userString = gson.toJson(userJSON);
        editor.putString(activity.getString(R.string.userTagSP),userString);
        editor.commit();

    }

    public JSONObject readUserData(){
        preferences = activity.getSharedPreferences(activity.getString(R.string.SharedPref), Context.MODE_PRIVATE);

        String userString = preferences.getString(activity.getString(R.string.userTagSP), "");
        Gson gson=new Gson();
        JSONObject userJSON=gson.fromJson(userString,ContentsUser.class);
        return  userJSON;
    }

    public void deleteUserData(){

        preferences = activity.getSharedPreferences(activity.getString(R.string.SharedPref), Context.MODE_PRIVATE);

        preferences.edit().remove(activity.getString(R.string.userTagSP)).commit();

    }






}
