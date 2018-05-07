package com.l3soft.routesmg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.l3soft.routesmg.R;
import com.l3soft.routesmg.entity.Bus;

import java.util.List;


public class BusAdapter extends RecyclerView.Adapter<BusAdapter.MyViewHolder> {

    private List<Bus> busList;
    private int[] colors;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView number;
        public TextView description;
        public ImageView image;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            number = view.findViewById(R.id.number);
            description = view.findViewById(R.id.description);
            overflow = view.findViewById(R.id.overflow);
            colors = view.getResources().getIntArray(R.array.initial_colors);
        }
    }


    public BusAdapter(List<Bus> busList) {
        this.busList = busList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bus_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Bus bus = busList.get(position);
        int number = (int) (Math.random() * 9);
        String description = bus.getDescription();

        if(description.length() > 57){

            String descrip = description.substring(0,54);
            descrip +="...";
            description = descrip;

            Log.i("Descripción 2",description.length()+"");
        }
        holder.number.setBackgroundColor(colors[number]);
        holder.number.setText(String.valueOf(bus.getNumber()));
        holder.description.setText(description);
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }
}
