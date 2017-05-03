package com.haayhappen.instafarm;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("API/{username}/{password}")
    Call<Login> authenticate(@Path("username") String username, @Path("password") String password);
    @POST("API/{username}/{password}")
    Call<Login> registration(@Path("username") String username, @Path("password") String password);
}