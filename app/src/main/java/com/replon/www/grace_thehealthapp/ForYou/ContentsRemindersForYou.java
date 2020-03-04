package com.replon.www.grace_thehealthapp.ForYou;

public class ContentsRemindersForYou {

    private String pill_id;
    private String pill_name;
    private int pill_number;
    private boolean pill_taken;


    public ContentsRemindersForYou(String pill_id, String pill_name, int pill_number, boolean pill_taken) {
        this.pill_id=pill_id;
        this.pill_name = pill_name;
        this.pill_number = pill_number;
        this.pill_taken = pill_taken;
    }

    public String getPill_id() {
        return pill_id;
    }

    public void setPill_id(String pill_id) {
        this.pill_id = pill_id;
    }

    public String getPill_name() {
        return pill_name;
    }

    public void setPill_name(String pill_name) {
        this.pill_name = pill_name;
    }

    public int getPill_number() {
        return pill_number;
    }

    public void setPill_number(int pill_number) {
        this.pill_number = pill_number;
    }

    public boolean isPill_taken() {
        return pill_taken;
    }

    public void setPill_taken(boolean pill_taken) {
        this.pill_taken = pill_taken;
    }
}
