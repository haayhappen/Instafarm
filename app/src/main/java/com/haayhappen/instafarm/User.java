package com.haayhappen.instafarm;

/**
 * Created by Fynn on 03.05.2017.
 */

class User {
    String username;
    private String passwort;
    private int likePerDay;
    private int followPerDay;
    private int unfollowPerDay;
    private int commentPerDay;
    private int mediaMinLike;
    private int mediaMaxLike;
    private String[] tagList;
    private String[] tagBlacklist;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }
}
