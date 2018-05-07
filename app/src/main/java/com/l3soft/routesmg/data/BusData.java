package com.l3soft.routesmg.data;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.l3soft.routesmg.MainActivity;
import com.l3soft.routesmg.R;
import com.l3soft.routesmg.adapter.BusAdapter;
import com.l3soft.routesmg.api.Api;
import com.l3soft.routesmg.db.AppDatabase;
import com.l3soft.routesmg.entity.Bus;
import com.l3soft.routesmg.entity.Place;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusData {
    private Context context;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;
    private AppDatabase db;

    public BusData(Context context) {
        this.context = context;

        db = Room.databaseBuilder(context,
                AppDatabase.class, "routesDB")
                .allowMainThreadQueries()
                .build();
    }

    public void getbuses(RecyclerView recycler, SwipeRefreshLayout swipeCont){
        this.recyclerView = recycler;
        this.swipeContainer = swipeCont;

        Call<List<Bus>> bus = Api.instance().getBuses();
        bus.enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (response.body() != null) {
                    try {
                        MainActivity.loadStatusConn(null,null,
                                R.drawable.online,context.getString(R.string.status_conn));
                        //load information from API REST
                        BusAdapter busAdap = new BusAdapter(response.body());
                        recyclerView.setAdapter(busAdap);
                        //finish refresh
                        swipeContainer.setRefreshing(false);
                        //update database
                        saveInDB(response.body());
                    }catch (Exception e){
                        Log.e("ERROR: ","API");
                    }
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                //message on view when fail
                swipeContainer.setRefreshing(false);
                Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createBus(Bus bus, final List<Place> places, final AlertDialog process){
        Call<Bus> call = Api.instance().postBus(bus);
        call.enqueue(new Callback<Bus>() {
            @Override
            public void onResponse(Call<Bus> call, Response<Bus> response) {
                if(response.body() != null){
                    Log.i("NEW BUS CREADO",response.body().getId());
                    TravelData travelData = new TravelData();
                    travelData.createTravel(response.body().getId(),places, process);
                }else{
                    Log.e("ERROR POST BUS",response.message());
                }
            }

            @Override
            public void onFailure(Call<Bus> call, Throwable t) {
                Log.e("ERROR POST BUS",t.getMessage());
            }
        });
    }

    public void saveInDB(List<Bus> buses){
        for (int i=0;i<buses.size();i++){
            if(!validExist(buses.get(i))){
                db.busDao().insertBus(buses.get(i));
            }
        }
    }

    public List<Bus> loadDB(){
        return  db.busDao().loadAllBuses();
    }

    public boolean validExist(Bus bus){

        boolean band = false;

        List<Bus> buses = loadDB();
        for(int i = 0; i < buses.size(); i++){
            if(bus.getId().equals(buses.get(i).getId())){
                band = true;
                break;
            }
        }
        return band;
    }
}
