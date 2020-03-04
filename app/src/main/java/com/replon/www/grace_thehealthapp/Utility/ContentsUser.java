package com.replon.www.grace_thehealthapp.Utility;

import org.json.JSONObject;

public class ContentsUser extends JSONObject {

    String uid;
    String name;
    String gender;
    Float weight;
    Float height;
    String email;
    String password;
    String device_type;
    //Something for bookmarked posts
    //Something for friend requests
    //Something for friend
    String dob;
    String profile_img_url;
    String date_created;
    String auth_token;

    public ContentsUser(String uid, String name, String gender, Float weight, Float height, String email, String password, String device_type, String dob, String profile_img_url, String date_created, String auth_token) {
        this.uid = uid;
        this.name = name;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.email = email;
        this.password = password;
        this.device_type = device_type;
        this.dob = dob;
        this.profile_img_url = profile_img_url;
        this.date_created = date_created;
        this.auth_token = auth_token;
    }
}
