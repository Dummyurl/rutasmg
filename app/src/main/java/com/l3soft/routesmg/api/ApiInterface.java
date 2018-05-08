package com.l3soft.routesmg.api;

import android.arch.persistence.room.Delete;

import com.l3soft.routesmg.entity.AccessToken;
import com.l3soft.routesmg.entity.Bus;
import com.l3soft.routesmg.entity.Commentary;
import com.l3soft.routesmg.entity.CustomCommentary;
import com.l3soft.routesmg.entity.Place;
import com.l3soft.routesmg.entity.Route;
import com.l3soft.routesmg.entity.Travel;
import com.l3soft.routesmg.entity.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ElOskar101 on 11/04/2018.
 */
public interface ApiInterface {
    //Metodos de la entidad buses
    @GET("buses?filter[order]=create_at%20DESC")
    Call<List<Bus>> getBuses();

    @POST("buses")
    Call<Bus> postBus(@Body Bus bus);

    @GET("travels")
    Call<List<Travel>> getTravels(@Query("filter") String filter);
    @POST("travels")
    Call<Travel> postTravel(@Body Travel travel);

    @GET("routes")
    Call<List<Route>> getRoutes(@Query("filter") String filter);
    @POST("routes")
    Call<Route> postRoutes(@Body Route route);

    @POST("places")
    Call<Place> postPlace(@Body Place place);

    //Métodos de la entidad commentary
    @GET("comments")
    Call<List<CustomCommentary>> getCommentary();

    @GET("comments")
    Call<Commentary> getCommentaryForID(@Query("filter") String filter);

    @POST("comments")
    Call<Commentary> postCommentary(@Body CustomCommentary customCommentary);

    @POST("Users/login")
    Call<AccessToken> login(@Body User user);

    @POST("Users")
    Call<User> signUp(@Body User user);
}