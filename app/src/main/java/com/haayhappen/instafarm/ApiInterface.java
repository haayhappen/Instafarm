package com.haayhappen.instafarm;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    //@FormUrlEncoded
    @POST("user")
    Call<Login> registration(@Query("username") String username, @Query("password") String password);

    @POST("run")
    Call<bot> runbot(@Query("username") String username, @Query("password") String password, @Body bot bot); //TODO add settings parameter as array?

    @POST("stop")
    Call<bot> stopbot(@Query("username") String username, @Query("PID") String PID);

    //@GET("hello/{username}")
    //Call<test> hello(@Path("username") String username);
}