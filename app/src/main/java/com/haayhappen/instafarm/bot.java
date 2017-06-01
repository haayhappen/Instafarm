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
    private int maxLikeForOneTag;
    private int mode;

    public bot(int likePerDay, int followPerDay, int unfollowPerDay, int commentPerDay, int mediaMinLike, int mediaMaxLike, String[] tagList, String[] tagBlacklist,int maxLikeForOneTag,int mode) {
        this.likePerDay = likePerDay;
        this.followPerDay = followPerDay;
        this.unfollowPerDay = unfollowPerDay;
        this.commentPerDay = commentPerDay;
        this.mediaMinLike = mediaMinLike;
        this.mediaMaxLike = mediaMaxLike;
        this.tagList = tagList;
        this.tagBlacklist = tagBlacklist;
        this.maxLikeForOneTag =maxLikeForOneTag;
        this.mode = mode;
    }

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
