package com.haayhappen.instafarm;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

// assumes the current class is called MyLogger

import java.io.ByteArrayOutputStream;
import java.util.Properties;

import static android.R.attr.duration;

public class MainActivity extends AppCompatActivity {
    String output;
    private final static Logger LOGGER = Logger.getLogger(MainActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button button = (Button) findViewById(R.id.btn_ssh);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                new AsyncTask<Integer, Void, Void>(){
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            output = executeRemoteCommand("root", "5Keosniluro", "62.75.253.50", 22);
                            Context context = getApplicationContext();

                            Toast toast = Toast.makeText(context,output, Toast.LENGTH_LONG);
                            toast.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(1);
            }
        });

    }

    public static String executeRemoteCommand(String username, String password, String hostname, int port)
            throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, hostname, port);
        session.setPassword(password);

        // Avoid asking for key confirmation
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);

        session.connect();

        // SSH Channel
        ChannelExec channelssh = (ChannelExec)
                session.openChannel("exec");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelssh.setOutputStream(baos);

        // Execute command
        channelssh.setCommand("cd InstagramBot/;screen -S androidscreen");
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();
    }
}
