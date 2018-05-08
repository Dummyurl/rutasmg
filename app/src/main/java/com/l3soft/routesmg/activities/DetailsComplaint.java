package com.l3soft.routesmg.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.l3soft.routesmg.R;
import com.l3soft.routesmg.adapter.DetailsComplaintAdapter;
import com.l3soft.routesmg.api.Api;
import com.l3soft.routesmg.entity.CustomCommentary;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsComplaint extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String busID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_complaint);


        receiveParameter();

        //inicializacion de las vistas
        recyclerView = findViewById(R.id.list_complaint);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Call<List<CustomCommentary>> call = Api.instance().getCommentary();
        call.enqueue(new Callback<List<CustomCommentary>>() {
            @Override
            public void onResponse(Call<List<CustomCommentary>> call, Response<List<CustomCommentary>> response) {
                if (response.body() != null ) {
                    try {
                        System.out.println("BusSolo: "+busID);
                        DetailsComplaintAdapter dca = new DetailsComplaintAdapter(response.body(), getApplicationContext(), busID);

                        recyclerView.setAdapter(dca);

                    } catch (IndexOutOfBoundsException exception) {
                        System.out.println("Error al adaptar");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CustomCommentary>> call, Throwable t) {
                System.out.println("Failuree");
            }

        });

    }

    public void receiveParameter(){
        Bundle parameter = this.getIntent().getExtras();
        if(parameter != null) {
            busID = getIntent().getExtras().getString("busID");
        }
    }


}
