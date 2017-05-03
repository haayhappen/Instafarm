package com.haayhappen.instafarm;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SettingsFragment.LoginListener{

    //Variables definitions
    private String username;
    private String passwort;
    private final String TAG = "MainActivity";
    public static final String BASE_URL = "http://instafarm.stackr.de/";
    String output;
    private final static Logger LOGGER = Logger.getLogger(MainActivity.class.getName());
    //    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager vpPager = (ViewPager) findViewById(R.id.pager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        vpPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            //When Tabs are swiped, the menu selection is changed accordingly
            @Override
            public void onPageSelected(int position) {
                BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
                if (position == 0) {
                    bottomBar.selectTabAtPosition(0);
                } else if (position == 1) {
                    bottomBar.selectTabAtPosition(1);
                } else if (position == 2) {
                    bottomBar.selectTabAtPosition(2);
                    bottomBar.setActiveTabColor(Color.parseColor("#FFFFFF"));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setActiveTabColor(Color.parseColor("#FFFFFF"));
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_settings) {
                    ViewPager vpPager = (ViewPager) findViewById(R.id.pager);
                    vpPager.setCurrentItem(0);
//                    The tab with id R.id.tab_favorites was selected, change your content accordingly.
//                    BottomBarTab bot = bottomBar.getTabWithId(R.id.tab_bot);
//                    bot.setBadgeCount(5);
//                    Remove the badge when you're done with it.
//                    nearby.removeBadge();

                } else if (tabId == R.id.tab_profile) {
                    ViewPager vpPager = (ViewPager) findViewById(R.id.pager);
                    vpPager.setCurrentItem(1);

                } else if (tabId == R.id.tab_bot) {

                    ViewPager vpPager = (ViewPager) findViewById(R.id.pager);
                    vpPager.setCurrentItem(2);
                }
            }
        });
        ////////////////////////Only for reselection///////////////////////////
        ///////////////////////////////////////////////////////////////////////
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_settings) {
                    // The tab with id R.id.tab_favorites was reselected,
                    // change your content accordingly.
                }
            }
        });
        ///////////////////////////////////////////////////////////////////////
    }
//    @Override
//    public void onFragmentInteraction(Uri uri){
//        //you can leave it empty
//    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void getLoginData(String username, String passwort) {

        this.username =username;
        this.passwort =passwort;
        Log.d("MAIN", "username: "+username+" password: "+ passwort);

        signin();
    }


    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return SettingsFragment.newInstance("Settings");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ProfileFragment.newInstance("Profile");
                case 2: // Fragment # 1 - This will show SecondFragment
                    return BotFragment.newInstance("Bot");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

    private ApiInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiInterface mInterfaceService = retrofit.create(ApiInterface.class);
        return mInterfaceService;
    }

    private void loginProcessWithRetrofit(final String username, String password) {
        ApiInterface mApiService = this.getInterfaceService();
        Call<Login> mService = mApiService.authenticate(username, password);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login mLoginObject = response.body();
                String returnedResponse = mLoginObject.isLogin;
                Toast.makeText(MainActivity.this, "Returned " + returnedResponse, Toast.LENGTH_LONG).show();
                //showProgress(false);
                if (returnedResponse.trim().equals("1")) {
                    //user can succesfully login
                }
                if (returnedResponse.trim().equals("0")) {
                    //user cant login
                    //log in with a valid instagram account
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                Toast.makeText(MainActivity.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void registrationProcessWithRetrofit(final String username, String password) {
        ApiInterface mApiService = this.getInterfaceService();
        Call<Login> mService = mApiService.registration(username, password);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login mLoginObject = response.body();
                String returnedResponse = mLoginObject.isLogin;
                //showProgress(false);
                if (returnedResponse.trim().equals("1")) {
                    // redirect to Main Activity page
                    //user can login
                    Toast.makeText(MainActivity.this, "Login succesful", Toast.LENGTH_LONG).show();
                }
                if (returnedResponse.trim().equals("0")) {
                    // use the registration button to register
                    //user cant login
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                Toast.makeText(MainActivity.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void signin() {

        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, "Signing in..", Toast.LENGTH_SHORT);
        toast.show();
        try {
            EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
            EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);

            this.username = usernameEditText.getText().toString();
            this.passwort = passwordEditText.getText().toString();

            SshConnectionManager asyncTask = (SshConnectionManager) new SshConnectionManager(new SshConnectionManager.AsyncResponse() {

                @Override
                public void processFinish(String output) {
                    //Here you will receive the result fired from async class
                    //of onPostExecute(result) method.
                    if(output.contains("Login success")){
                        showOutput("Login success");
                    }else{
                        showOutput("Wrong password or username");
                    }

                }
            }).execute(username, passwort);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void showOutput(String response) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, "" + response, Toast.LENGTH_LONG);
        toast.show();
    }
}



