package com.haayhappen.instafarm;

import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static android.content.ContentValues.TAG;

/**
 * Created by Fynn on 27.04.2017.
 */

public class SshConnectionManager extends AsyncTask<String, Void, String> {

    private static Session session;
    private static Channel channel;
    private static String username = "";
    private static String password = "";
    private static String hostname = "";
    public AsyncResponse delegate = null;
    private String result;


    public interface AsyncResponse {
        void processFinish(String output);
    }
    public SshConnectionManager(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... params) {
        //this.hostname = params[0]; //"62.75.253.50"
        this.username = params[0]; //instagram username
        this.password = params[1]; //instagram passwort
        //List<String> commands = new ArrayList<String>();
        //commands.add("cd Instagram-API-python/ ;python checkuser.py hackingismylifeanonymous passwort");

        result = executeCommands(username,password);
        close();
        return result;
    }

    private Session getSession(){
        if(session == null || !session.isConnected()){
            session = connect();
        }
        return session;
    }

    private Channel getChannel(){
        if(channel == null || !channel.isConnected()){
            try{
                channel = getSession().openChannel("shell");
                channel.connect();

            }catch(Exception e){
                Log.d(TAG,"Error while opening channel: "+ e);
            }
        }
        return channel;
    }

    private Session connect(){

        JSch jSch = new JSch();

        try {

            session = jSch.getSession("instafarm","62.75.253.50", 22);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            //TODO get Secret Key from server instead of passwort string
            //Just for debugging-->
            session.setPassword("instafarm");

            Log.d(TAG,"Connecting SSH to " + hostname + " - Please wait for few seconds... ");
            session.connect();
            Log.d(TAG,"Connected!");
        }catch(Exception e){
            Log.d(TAG,"An error occurred while connecting to "+hostname+": "+e);
        }

        return session;

    }

    private String executeCommands(String username,String passwort/*List<String> commands*/){

        try{
            Channel channel=getChannel();

            Log.d(TAG,"Sending commands...");
            sendCommands(channel,username,passwort/*, commands*/);

            String output;
            output = readChannelOutput(channel);
            Log.d(TAG,"Finished sending commands!");

            return output;
        }catch(Exception e){
            Log.d(TAG,"An error ocurred during executeCommands: "+e);
        }
        return "Error getting channel output through execute commands";
    }

    private void sendCommands(Channel channel,String username,String passwort/*, List<String> commands*/){

        try{
            PrintStream out = new PrintStream(channel.getOutputStream());

            //out.println("#!/bin/bash");
//            for(String command : commands){
//                out.println(command);
//            }

            //changing directory to instagram api
            //& run file to check if user has a valid instagram account
            out.println("cd Instagram-API-python/");
            out.println("python checkuser.py "+username+" "+passwort);
            //out.println("exit");

            out.flush();
            //channel.setCommand("cd Instagram-API-python/ ;python checkuser.py hackingismylifeanonymous passwort");

        }catch(Exception e){
            Log.d(TAG,"Error while sending commands: "+ e);
        }

    }

    private String readChannelOutput(Channel channel){
        String output ="";
        try {
            InputStream inStr = channel.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStr));
            while (!(output += reader.readLine()).contains("done"))
            {
                Log.d(TAG,"Output: " + output);
            }
            String test = output;
            return test;
        }
        catch (Exception e){e.printStackTrace();}



        return output;
//        byte[] buffer = new byte[1024];
//
//        try{
//            InputStream in = channel.getInputStream();
//            String line = "Server: ";
//            while (true){
//                while (in.available() > 0) {
//                    int i = in.read(buffer, 0, 1024);
//                    if (i < 0) {
//                        break;
//                    }
//                    line = new String(buffer, 0, i);
//                    //Log.d(TAG,line);
//
//                }
//
//                if(line.contains("logout")){
//                    break;
//                }
//
//                if (channel.isClosed()){
//                    break;
//                }
//                try {
//                    Thread.sleep(1000);
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//
//                return line;
//            }
//        }catch(Exception e){
//            Log.d(TAG,"Error while reading channel output: "+ e);
//        }
//
//        return "error in readchannel";
    }

    public void close(){
        if(channel.isConnected()){
            channel.disconnect();
        }
        if (session.isConnected()){
            session.disconnect();
        }
        Log.d(TAG,"Disconnected channel and session");
    }

    @Override
    protected void onPreExecute () {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute (String result){
        delegate.processFinish(result);
        Log.d(TAG, "SSH Transfer complete: " + result);

    }
}