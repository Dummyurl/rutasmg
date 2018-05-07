package com.l3soft.routesmg.data;


import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.l3soft.routesmg.api.Api;
import com.l3soft.routesmg.entity.Place;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceData {
    private int position;
    private int size;
    private AlertDialog process;
    private List<Place> places;
    public PlaceData() {
        places = new ArrayList<>();
        position = 1;
    }

    public void createPlace(Place place){
        Call<Place> call = Api.instance().postPlace(place);
        call.enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                if(response.body() != null){
                    Log.i("POST PLACE",response.body().getId());
                    position ++;
                    places.add(response.body());
                    if(position == size ){
                        Log.i("CREATE PLACES","COMPLETE");
                        process.dismiss();
                        process.cancel();
                    }
                }else{
                    Log.e("ERROR POST PLACE",response.message());
                }
            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                Log.e("ERROR POST PLACE",t.getMessage());
            }
        });
    }

    public void createPlaces(List<Place> places, String routeID, AlertDialog process){
        size = places.size();
        Log.i("CREATE PLACES SIZE",places.size()+"");
        this.process = process;
        for(int i =0; i < places.size();i++){
            Log.i("CREATE PLACES POS",i+"");
            places.get(i).setRouteID(routeID);
            places.get(i).setPosition(i);
            createPlace(places.get(i));
        }
    }
}
