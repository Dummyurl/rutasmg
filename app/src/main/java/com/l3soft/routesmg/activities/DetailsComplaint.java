package com.l3soft.routesmg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.l3soft.routesmg.R;
import com.l3soft.routesmg.adapter.DetailsComplaintAdapter;
import com.l3soft.routesmg.api.Api;
import com.l3soft.routesmg.entity.CustomCommentary;
import com.l3soft.routesmg.fragment.BusFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsComplaint extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String busID;



    public DetailsComplaint(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_complaint);

        receiveParameter();

        //inicializacion de las vistas
        recyclerView = findViewById(R.id.list_complaint);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        System.out.println("BusSolo: "+busID);

        String filter = String.format(this.getApplicationContext().getString(R.string.filter_commentary_for_bus_id), busID);

        Call<List<CustomCommentary>> call = Api.instance().getCommentaryForID(filter);

        call.enqueue(new Callback<List<CustomCommentary>>() {
            @Override
            public void onResponse(Call<List<CustomCommentary>> call, Response<List<CustomCommentary>> response) {
                if (response.body() != null ) {
                    try {

                        DetailsComplaintAdapter dca = new DetailsComplaintAdapter(response.body(), getApplicationContext());

                        recyclerView.setAdapter(dca);

                    } catch (IndexOutOfBoundsException exception) {
                        System.out.println("Error al adaptar");
                    }
                }else{
                    System.out.println("Java.langNullPointerException");
                }
            }

            @Override
            public void onFailure(Call<List<CustomCommentary>> call, Throwable t) {
                System.out.println("Failuree");
            }

        });

    }

    //recivo el ID del bus
    public void receiveParameter(){
        Bundle parameter = this.getIntent().getExtras();
        if(parameter != null) {
            busID = getIntent().getExtras().getString("busID");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshComplains(BusFragment.class);
        Log.i("RESULT CODE",requestCode+"");
    }


    private void refreshComplains(Class fragmentClass){

        Fragment fragment;
        try {

            fragment = (Fragment) fragmentClass.newInstance();
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


}
