package com.l3soft.routesmg.data;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.l3soft.routesmg.R;
import com.l3soft.routesmg.api.Api;
import com.l3soft.routesmg.db.AppDatabase;
import com.l3soft.routesmg.entity.Place;
import com.l3soft.routesmg.entity.Travel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravelData {
    private Context context;
    private AppDatabase db;

    public TravelData(Context context) {
        this.context = context;

        db = Room.databaseBuilder(context,
                AppDatabase.class, "routesDB")
                .allowMainThreadQueries()
                .build();
    }

    public TravelData() {}

    public void getTravels(int id){

        String filter = String.format(context.getString(R.string.filter_travel_for_bus_id),id);
        Call<List<Travel>> call =  Api.instance().getTravels(filter);

        call.enqueue(new Callback<List<Travel>>() {
            @Override
            public void onResponse(Call<List<Travel>> call, Response<List<Travel>> response) {
                if(response.body() != null){

                }
            }

            @Override
            public void onFailure(Call<List<Travel>> call, Throwable t) {
                Log.e("ERROR","SAD "+t.getMessage());
            }
        });
    }

    public void createTravel(String busID, final List<Place> places, final AlertDialog process){

        Travel travel = new Travel();
        travel.setBusId(busID);

        Call<Travel> call = Api.instance().postTravel(travel);
        call.enqueue(new Callback<Travel>() {
            @Override
            public void onResponse(Call<Travel> call, Response<Travel> response) {
                if(response.body() != null){
                    Log.i("CREATE TRAVEL",response.body().getId());
                    RouteData routeData = new RouteData();
                    routeData.createRoute(response.body().getId(),places, process);
                }else{
                    Log.e("ERROR CREATE TRAVEL",response.message());
                }
            }

            @Override
            public void onFailure(Call<Travel> call, Throwable t) {
                Log.e("ERROR CREATE TRAVEL",t.getMessage());
            }
        });
    }
}
