package com.l3soft.routesmg.data;


import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.l3soft.routesmg.adapter.TravelAdapter;
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
    private Context context;

    public PlaceData() {
        places = new ArrayList<>();
        position = 1;
    }

    public PlaceData(Context context) {
        this.context = context;
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

    public void getPlacesForID(String routeID, final TravelAdapter.MyViewHolder holder){
        String filter = routeID;
        Call<List<Place>> call = Api.instance().getPlace(filter);
        call.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                if(response.body() != null){
                    for(Place place : response.body()){
                        LatLng latLng = new LatLng(place.getCoordx(), place.getCoordy());
                        holder.latLngs.add(latLng);
                        holder.mapView.onCreate(null);
                        holder.mapView.getMapAsync(holder);
                    }
                }else{
                    Log.e("PLACE DATA GET ERROR",response.message()+" "+response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                Log.e("PLACE DATA GET ERROR",t.getMessage());
            }
        });

    }
}
