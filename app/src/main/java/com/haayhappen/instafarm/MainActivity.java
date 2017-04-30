package com.haayhappen.instafarm;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    String output;
    private final static Logger LOGGER = Logger.getLogger(MainActivity.class.getName());
    private static final int NUM_PAGES = 3;
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

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
//        Toast toast = Toast.makeText(context, output, Toast.LENGTH_LONG);
//        toast.show();
//        Context context = getApplicationContext();

        vpPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            //When Tabs are swiped, the menu selection is changed accordingly
            @Override
            public void onPageSelected(int position) {
                BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
                if (position == 0){
                    bottomBar.selectTabAtPosition(0);
                }else if(position == 1){
                    bottomBar.selectTabAtPosition(1);
                }else if(position == 2){
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


        ///////////////////////////////////////////////////////////////////////
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
        ///////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////


    }

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


//    @Override
//    public boolean onCreateOptionsMenu(final Menu menu) {
//        getMenuInflater().inflate(R.menu.menuitems, menu);
//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.action_settings:
//                // User chose the "Settings" item, show the app profile
//                Intent intent = new Intent(this, ProfileActivity.class);
//                startActivity(intent);
//                return true;
//
////                case R.id.action_favorite:
////                    // User chose the "Favorite" action, mark the current item
////                    // as a favorite...
////                    return true;
//
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//
//        }
//    }

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

}



