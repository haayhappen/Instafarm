package com.haayhappen.instafarm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Properties;

import static android.R.attr.delay;

/**
 * Created by Fynn on 26.04.2017.
 */

public class AsyncSSHManager extends AsyncTask<String, Void, String> {

        private static final String TAG = "SSH MANAGER";
        private AsyncResponse asyncResponse;
        private String response="";
    private static Session session;
    private static ChannelShell channel;
    private static String username = "";
    private static String password = "";
    private static String hostname = "62.75.253.50";

        public interface AsyncResponse {
            void processFinish(String response);
        }

    public AsyncSSHManager(String username, String passwort, AsyncResponse asyncResponse) {
        this.username = username;
        this.password= passwort;
        this.asyncResponse = asyncResponse;
    }

        @Override
        protected String doInBackground (String...params){

            //Do SSH Connection here

            try {
                //response = executeRemoteCommand("root", "5Keosniluro", "62.75.253.50", 22);
                response = executeRemoteCommand("testuser", "testpw", hostname, 22);
//                            Toast toast = Toast.makeText(context,output, Toast.LENGTH_LONG);
//                            toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

        return response;
    }

        @Override
        protected void onPreExecute () {
        super.onPreExecute();
    }

        @Override
        protected void onPostExecute (String response){
        super.onPostExecute(response);
        Log.d(TAG, "SSH Transfer complete: " + response);
        asyncResponse.processFinish(response);

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
//        channelssh.setCommand("cd Instagram-API-python/;python checkuser.py hackingismylifeanonymous passwort");
        channelssh.setCommand("cd Instagram-API-python/;mkdir testdirectoryworking");
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();
    }
}