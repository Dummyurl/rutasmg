package com.l3soft.routesmg.data;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.l3soft.routesmg.R;
import com.l3soft.routesmg.api.Api;
import com.l3soft.routesmg.entity.Place;
import com.l3soft.routesmg.entity.Route;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteData {
    private Context context;

    public RouteData(Context context) {
        this.context = context;
    }
    public RouteData() {}
    public void getRouteData(int id){
        String filter = String.format(context.getString(R.string.filter_route_for_bus_id),id);
        Call<List<Route>> call = Api.instance().getRoutes(filter);
        call.enqueue(new Callback<List<Route>>() {
            @Override
            public void onResponse(Call<List<Route>> call, Response<List<Route>> response) {

            }

            @Override
            public void onFailure(Call<List<Route>> call, Throwable t) {

            }
        });
    }

    public  void createRoute(String travelID, final List<Place> places, final AlertDialog process){
        Route route = new Route();
        route.setTravelID(travelID);
        Call<Route> call = Api.instance().postRoutes(route);
        call.enqueue(new Callback<Route>() {
            @Override
            public void onResponse(Call<Route> call, Response<Route> response) {
                if(response.body() != null){
                    Log.i("CREATE ROUTE",response.body().getId());
                    PlaceData placeData = new PlaceData();
                    placeData.createPlaces(places, response.body().getId(), process);
                }else{
                    Log.i("ERROR POST ROUTE",response.message());
                }
            }

            @Override
            public void onFailure(Call<Route> call, Throwable t) {
                Log.i("ERROR POST ROUTE",t.getMessage());
            }
        });
    }
}
