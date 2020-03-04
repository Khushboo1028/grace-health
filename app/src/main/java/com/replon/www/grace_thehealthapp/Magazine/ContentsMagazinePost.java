package com.replon.www.grace_thehealthapp.Magazine;

public class ContentsMagazinePost {


    String topic;
    String topic_description;
    String date;
    String image_url;
    String news_url;

    public ContentsMagazinePost(String topic, String topic_description, String date, String image_url, String news_url) {
        this.topic = topic;
        this.topic_description = topic_description;
        this.date = date;
        this.image_url = image_url;
        this.news_url = news_url;
    }

    public String getNews_url() {
        return news_url;
    }

    public void setNews_url(String news_url) {
        this.news_url = news_url;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic_description() {
        return topic_description;
    }

    public void setTopic_description(String topic_description) {
        this.topic_description = topic_description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
