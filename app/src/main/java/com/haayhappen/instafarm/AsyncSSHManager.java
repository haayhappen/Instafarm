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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Properties;

import static android.R.attr.delay;
import static android.R.attr.port;

/**
 * Created by Fynn on 26.04.2017.
 */

public class AsyncSSHManager extends AsyncTask<String, Void, String> {

        private static final String TAG = "SSH MANAGER";
//        private AsyncResponse asyncResponse;
        private String response="";
        private static Session session;
        private static ChannelShell channel;
        private static String username = "";
        private static String password = "";
        private static String hostname = "62.75.253.50";
    public AsyncResponse delegate = null;

//        public interface AsyncResponse {
//            void processFinish(String response);
//        }

    public interface AsyncResponse {
        void processFinish(String output);
    }
    public AsyncSSHManager(AsyncResponse delegate){
        this.delegate = delegate;
    }

    public AsyncSSHManager(String username, String passwort/*, AsyncResponse asyncResponse*/) {
        this.username = username;
        this.password= passwort;
        //this.asyncResponse = asyncResponse;
    }

        @Override
        protected String doInBackground (String...params){

            //Do SSH Connection here

            //try {
                /*
                //response = executeRemoteCommand("root", "5Keosniluro", "62.75.253.50", 22);
//                response = executeRemoteCommand("testuser", "testpw", hostname, 22);
                JSch jsch = new JSch();
                Session session = jsch.getSession("instafarm", "62.75.253.50", 22);
                session.setPassword("instafarm");

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
                channelssh.setCommand("cd Instagram-API-python/ ;touch hierlandetderuser.txt");
                channelssh.connect();
                channelssh.disconnect();
*/

                    String result = new String("");

                    Session session = null;
                    ChannelExec channel = null;

                    try {
                        JSch jsch = new JSch();
                        session = jsch.getSession("instafarm", "62.75.253.50", 22);
                        session.setPassword("instafarm");

                        // Avoid asking for key confirmation
                        Properties prop = new Properties();
                        prop.put("StrictHostKeyChecking", "no");
                        session.setConfig(prop);

                        session.connect();
                        channel = (ChannelExec) session.openChannel("exec");
                        channel.setCommand("cd Instagram-API-python/ ;python checkuser.py hackingismylifeanonymous passwort");
                        channel.setInputStream(null);
                        InputStream stdout = channel.getInputStream();
                        InputStream stderr = channel.getErrStream();
                        channel.connect();

                        //waitForChannelClosed(channel);
                        //Thread.sleep(10000L);
                        while(channel.isConnected()){
                            Thread.sleep(1000L);
                        }

                        if (channel.getExitStatus() != 0) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stderr));
                            //readAll(bufferedReader, result);
                            String line = "";
                            while((line = bufferedReader.readLine()) != null){

                            }
                            result +=line+"";

                            throw new Exception(result.toString());
                        } else {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stdout));
                                //result = readAll(bufferedReader, result);
                                //response = String.valueOf(readAll(bufferedReader, result));

                            String line = "";
                            while((line = bufferedReader.readLine()) != null){

                            }
                            result +=line+"";
                        }

                        } catch (Exception e) {
                        //throw new Exception(e);
                        e.printStackTrace();
                    } finally {
                        if (channel != null) {
                            channel.disconnect();
                        }

                        if (session != null) {
                            session.disconnect();
                        }
                    }

                    return result.toString();

    }

//    private StringBuffer readAll(BufferedReader bufferedReader, StringBuffer result) {
//        try  {
//            long length = 0;
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                if (line.isEmpty()) {
//                    break;
//                }
//                length += line.length();
//            }
//            //System.out.println("Read length: " + length);
//            //System.out.println("Bufferd Reader output: "+line);
//            Log.d(TAG, "read length " + length);
//            Log.d(TAG, "buffered reader output " + line);
//
//            return new StringBuffer(line +result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//return new StringBuffer("Error handling shell output");
//    }

//    private void waitForChannelClosed(ChannelExec channel) {
//
//    }

        @Override
        protected void onPreExecute () {
        super.onPreExecute();
    }

        @Override
        protected void onPostExecute (String result){
            delegate.processFinish(result);
        Log.d(TAG, "SSH Transfer complete: " + response);

    }



//    public static String executeRemoteCommand(String username, String password, String hostname, int port)
//            throws Exception {
//        JSch jsch = new JSch();
//        Session session = jsch.getSession(username, hostname, port);
//        session.setPassword(password);
//
//        // Avoid asking for key confirmation
//        Properties prop = new Properties();
//        prop.put("StrictHostKeyChecking", "no");
//        session.setConfig(prop);
//
//        session.connect();
//
//        // SSH Channel
//        ChannelExec channelssh = (ChannelExec)
//                session.openChannel("exec");
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        channelssh.setOutputStream(baos);
//
//        // Execute command
////        channelssh.setCommand("cd Instagram-API-python/;python checkuser.py hackingismylifeanonymous passwort");
//        channelssh.setCommand("touch hierlandetderuser.txt");
//        channelssh.connect();
//        channelssh.disconnect();
//
//        return baos.toString();
//    }
}