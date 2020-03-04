package com.replon.www.grace_thehealthapp.Utility;

public class ContentsDataDisplay {

    private int amount;
    private String units;
    private String date;


    public ContentsDataDisplay(int amount, String units, String date) {
        this.amount = amount;
        this.units = units;
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
