package com.haayhappen.instafarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Profile");
        setContentView(R.layout.activity_profile);

    }
    public void backtomain(View view) {
        // Kabloey
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
