package com.haayhappen.instafarm;

import android.app.Application;

/**
 * Created by Fynn on 14.05.2017.
 */

public class Instafarm extends Application {

    private boolean isLoggedIn;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
