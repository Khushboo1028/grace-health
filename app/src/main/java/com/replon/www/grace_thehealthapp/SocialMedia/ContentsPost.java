package com.replon.www.grace_thehealthapp.SocialMedia;

import org.json.JSONArray;

public class ContentsPost {


    String pid;
    String content;
    int likes_count;
    String image_url;
//    JSONArray likes_uid;
    String uid;
    String date_created;
    String community_name ;
    String profile_image_url;
    String name;
    Boolean liked_boolean;

    public ContentsPost(String pid, String content, int likes_count, String image_url, String uid, String date_created, String community_name, String profile_image_url, String name, Boolean liked_boolean) {
        this.pid = pid;
        this.content = content;
        this.likes_count = likes_count;
        this.image_url = image_url;
//        this.likes_uid = likes_uid;
        this.uid = uid;
        this.date_created = date_created;
        this.community_name = community_name;
        this.profile_image_url = profile_image_url;
        this.name = name;
        this.liked_boolean = liked_boolean;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
//
//    public JSONArray getLikes_uid() {
//        return likes_uid;
//    }
//
//    public void setLikes_uid(JSONArray likes_uid) {
//        this.likes_uid = likes_uid;
//    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getLiked_boolean() {
        return liked_boolean;
    }

    public void setLiked_boolean(Boolean liked_boolean) {
        this.liked_boolean = liked_boolean;
    }
}
