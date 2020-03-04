package com.replon.www.grace_thehealthapp.Nearby;

public class ContentsNearby {

    String place_id;
    String name;
    String phone;
    String address;

    public ContentsNearby(String place_id, String name, String phone, String address) {
        this.place_id = place_id;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
