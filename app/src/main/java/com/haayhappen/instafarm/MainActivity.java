package com.haayhappen.instafarm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SettingsFragment.LoginListener, ProfileFragment.InteractionListener, BotFragment.InteractionListener {

    //Debug TAG
    private final String TAG = "MainActivity";
    private final static Logger LOGGER = Logger.getLogger(MainActivity.class.getName());
    ///////////

    //Gerneral Variables Declaration
    private String username;
    private String password;
    public String pid;
    private Context mContext;
    private Snackbar snackbar;
    // Choose an arbitrary request code value
    //private static final int RC_SIGN_IN = 1337;
    ////////////////////////////////

    //ViewPager Variables Declaration
    private ViewPager vpPager;
    private PagerAdapter mPagerAdapter;
    FragmentPagerAdapter adapterViewPager;
    /////////////////////////////////

    //Material Dialog Variables Declaration
    MaterialDialog.Builder builder;
    MaterialDialog dialog;
    ///////////////////////////////////////

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ((Instafarm) this.getApplication()).setLoggedIn(false);

        vpPager = (ViewPager) findViewById(R.id.pager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        Log.d(TAG, "current page: " + vpPager.getCurrentItem());

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
                    vpPager.setCurrentItem(0);
//                    The tab with id R.id.tab_favorites was selected, change your content accordingly.
//                    BottomBarTab bot = bottomBar.getTabWithId(R.id.tab_bot);
//                    bot.setBadgeCount(5);
//                    Remove the badge when you're done with it.
//                    nearby.removeBadge();
                } else if (tabId == R.id.tab_profile) {
                    vpPager.setCurrentItem(1);

                } else if (tabId == R.id.tab_bot) {
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

// [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    //Creates the Toolbars MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //Listener for Toolbar Menu Icons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_setting:

                // Firebase sign out
                mAuth.signOut();
                //Google sign out
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                //TODO Check if status really is ok
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });



                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            mGoogleApiClient.connect();
        }catch (IllegalStateException e)
        {
            Log.e("IllegalStateException", e.toString());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            mGoogleApiClient.disconnect();
        }catch (IllegalStateException e)
        {
            Log.e("IllegalStateException", e.toString());
        }
        finish();
    }

    //Handles the On back button through the fragments
    @Override
    public void onBackPressed() {
        if (vpPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            vpPager.setCurrentItem(vpPager.getCurrentItem() - 1);
        }
    }

    //Gets login data from settings tab
    @Override
    public void getLoginData(String username, String password) {

        this.username = username;
        this.password = password;

        //Login with retrofit
        registrationProcessWithRetrofit(username, password);
    }

    //says hello from profile tab
    @Override
    public void sayHello() {

//        this.username =username;
//        this.password =password;
//        Log.d("MAIN", "username: "+username+" password: "+ password);

        // retroHello(this.username);
    }

    //ovverides runbot method from botfragment,executed when runbot button is clicked)
    @Override
    public void runbot(bot bot) {
        if (((Instafarm) this.getApplication()).isLoggedIn()) {
            runBotWithRetro(this.username, this.password,bot);
        } else {
            Toast.makeText(MainActivity.this, "Log in first!", Toast.LENGTH_LONG).show();
        }
    }


    //Overrides method from botfragment ,executed when stopbot is clicked
    @Override
    public void stopbot() {
        if (((Instafarm) this.getApplication()).isLoggedIn()) {

            stopBotWithRetro(this.username, this.pid);
        } else {
            Toast.makeText(MainActivity.this, "Log in first!", Toast.LENGTH_LONG).show();
        }
    }

    //Pager Adapter handles swiping between fragments
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

    //Builder for the Retrofit Interface
    private ApiInterface getInterfaceService() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://62.75.253.50/API/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        final ApiInterface mInterfaceService = retrofit.create(ApiInterface.class);
        return mInterfaceService;
    }

    //register is actually just signing in
    private void registrationProcessWithRetrofit(final String username, String password) {

        builder = new MaterialDialog.Builder(mContext)
                .title("Signing in")
                .content("Please wait")
                .progress(true, 0);
        dialog = builder.build();
        dialog.setCancelable(false);
        dialog.show();

        ApiInterface mApiService = this.getInterfaceService();
        Call<Login> mService = mApiService.registration(username, password);
        mService.enqueue(new Callback<Login>() {


            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login mLoginObject = response.body();
                String returnedResponse = mLoginObject.isLogin;
                System.out.println(mLoginObject.isLogin);
                System.out.println(returnedResponse);
                //showProgress(false);
                if (returnedResponse.trim().equals("1")) {
                    // redirect to Main Activity page
                    //user can login
                    //TODO SAVE CREDENTIALS with shared preferences
                    Toast.makeText(MainActivity.this, "Login succesful", Toast.LENGTH_LONG).show();
                    ((Instafarm) getApplication()).setLoggedIn(true);
                    dialog.dismiss();
                }
                if (returnedResponse.trim().equals("2")) {
                    // redirect to Main Activity page
                    //user can login
                    //TODO SAVE CREDENTIALS with shared preferences
                    Toast.makeText(MainActivity.this, "Registration succesful", Toast.LENGTH_LONG).show();
                    ((Instafarm) getApplication()).setLoggedIn(true);
                    dialog.dismiss();
                }
                if (returnedResponse.trim().equals("0")) {
                    // use the registration button to register
                    //user cant login
                    Toast.makeText(MainActivity.this, "Username or password incorrect!", Toast.LENGTH_LONG).show();
                    ((Instafarm) getApplication()).setLoggedIn(false);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                Toast.makeText(MainActivity.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    private void runBotWithRetro(String username, String password, bot bot) {
//
//        username = "svenspieler";
//        password = "5Keosniluro";

        //Bot ist the config Object

        ApiInterface mApiService = this.getInterfaceService();
        Call<bot> mService = mApiService.runbot(username, password, bot);
        mService.enqueue(new Callback<bot>() {
            @Override
            public void onResponse(Call<bot> call, Response<bot> response) {
                bot mBotObject = response.body();
                String returnedResponse = mBotObject.running;
                String returnedIPD = mBotObject.PID;
                pid = returnedIPD;

                Toast.makeText(MainActivity.this, "Returned " + returnedResponse + "with PID:" + returnedIPD, Toast.LENGTH_LONG).show();
//                //showProgress(false);
                if (returnedResponse.trim().equals("1")) {
                    //user can succesfully login
                    Toast.makeText(MainActivity.this, "Bot is running with PID:" + returnedIPD, Toast.LENGTH_LONG).show();

                }
                if (returnedResponse.trim().equals("0")) {
                    //user cant login
                    //log in with a valid instagram account
                    Toast.makeText(MainActivity.this, "Couldn't start Bot", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<bot> call, Throwable t) {
                call.cancel();
                Toast.makeText(MainActivity.this, "Please check Internet conditions -> couldn't start bot", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void stopBotWithRetro(String username, final String PID) {

        ApiInterface mApiService = this.getInterfaceService();
        Call<bot> mService = mApiService.stopbot(username, PID);
        mService.enqueue(new Callback<bot>() {
            @Override
            public void onResponse(Call<bot> call, Response<bot> response) {
                bot mBotObject = response.body();
                String returnedResponse = mBotObject.running;

                //Toast.makeText(MainActivity.this, "Returned " + returnedResponse + "with PID:" +returnedIPD, Toast.LENGTH_LONG).show();
//                //showProgress(false);
                if (returnedResponse.trim().equals("0")) {
                    //user can succesfully login
                    Toast.makeText(mContext, "Bot with PID:" + PID + " stopped.", Toast.LENGTH_LONG).show();

                }
                if (returnedResponse.trim().equals("1")) {
                    //user cant login
                    //log in with a valid instagram account
                    Toast.makeText(mContext, "Couldn't stop Bot with PID: " + PID, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<bot> call, Throwable t) {
                call.cancel();
                Toast.makeText(MainActivity.this, "Please check Internet connection!", Toast.LENGTH_LONG).show();
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
            this.password = passwordEditText.getText().toString();

            SshConnectionManager asyncTask = (SshConnectionManager) new SshConnectionManager(mContext, new SshConnectionManager.AsyncResponse() {

                @Override
                public void processFinish(String output) {
                    //Here you will receive the result fired from async class
                    //of onPostExecute(result) method.
                    if (output.contains("Login success")) {
                        showOutput("Login success");
                    } else {
                        showOutput("Wrong password or username");
                    }

                }
            }).execute(username, password);
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



