package com.haayhappen.instafarm;

/**
 * Created by Fynn on 14.05.2017.
 */

public class bot {

    public String running;
    public String PID;
    private int likePerDay;
    private int followPerDay;
    private int unfollowPerDay;
    private int commentPerDay;
    private int mediaMinLike;
    private int mediaMaxLike;
    private String[] tagList;
    private String[] tagBlacklist;

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getRunning() {
        return running;
    }

    public void setRunning(String running) {
        this.running = running;
    }

    //TODO create constructor

}
