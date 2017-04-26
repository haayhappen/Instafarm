package com.haayhappen.instafarm;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import static android.R.attr.duration;

public class ProfileActivity extends AppCompatActivity {

    private String username;
    private String passwort;

    //Context context = getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");


    }
    public void backtomain(View view) {
        // Kabloey
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void signin(View view) {


        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, "Signing in..", Toast.LENGTH_SHORT);
                            toast.show();

        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        this.username = usernameEditText.getText().toString();
        this.passwort = passwordEditText.getText().toString();

        AsyncSSHManager asyncTask = new AsyncSSHManager(username,passwort,new AsyncSSHManager.AsyncResponse() {
            @Override
            public void processFinish(String response) {
                //Do something with response
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, response, Toast.LENGTH_LONG);
                            toast.show();
            }
        });
    }
}
