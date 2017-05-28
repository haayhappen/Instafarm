package com.haayhappen.instafarm;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static android.R.attr.password;

public interface ApiInterface {
    //@FormUrlEncoded
    @POST("user")
    Call<Login> registration(@Query("username") String username, @Query("password") String password);

    @POST("run")
    Call<bot> runbot(@Query("username") String username, @Query("password") String password); //TODO add settings parameter as array?

    @POST("stop")
    Call<bot> stopbot(@Query("username") String username, @Query("PID") String PID);

    //@GET("hello/{username}")
    //Call<test> hello(@Path("username") String username);
}