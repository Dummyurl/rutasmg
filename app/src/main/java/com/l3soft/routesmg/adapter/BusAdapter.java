package com.l3soft.routesmg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.l3soft.routesmg.R;
import com.l3soft.routesmg.activities.ComplaintActivity;
import com.l3soft.routesmg.activities.DetailsComplaint;
import com.l3soft.routesmg.entity.Bus;

import java.util.List;


public class BusAdapter extends RecyclerView.Adapter<BusAdapter.MyViewHolder> {

    private List<Bus> busList;
    private Context bContext ;

    private int[] colors;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView number;
        public TextView description;
        public ImageView image;
        public ImageView overflow;
        private CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            number = view.findViewById(R.id.number);
            description = view.findViewById(R.id.description);
            overflow = view.findViewById(R.id.overflow);
            colors = view.getResources().getIntArray(R.array.initial_colors);
            cardView = view.findViewById(R.id.card_view);
        }
    }


    public BusAdapter(List<Bus> busList, Context context) {
        this.busList = busList;
        this.bContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bus_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Bus bus = busList.get(position);
        int number = (int) (Math.random() * 9);
        String description = bus.getDescription();

        if (description.length() > 57) {


            String descrip = description.substring(0, 54);
            descrip += "...";
            description = descrip;

        }
        holder.number.setBackgroundColor(colors[number]);
        holder.number.setText(String.valueOf(bus.getNumber()));
        holder.description.setText(description);


        // para abrir el cardview
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(bContext, DetailsComplaint.class);
                intent.putExtra("busID", busList.get(position).getId());
                bContext.startActivity(intent);
            }
        });

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    showPopupMenu(holder.overflow, position);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Mostrando el pup-up cuando presiona los tres puntos
     */
    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(bContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.bus_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }

    /**
     * Click listener para el pup-up
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        int position;
        public MyMenuItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_complaint:
                    try {
                        Intent intent = new Intent(bContext, ComplaintActivity.class);

                        intent.putExtra("busID", busList.get(position).getId());
                        bContext.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
            }
            return false;
        }
    }
    @Override
    public int getItemCount() {
        return busList.size();
    }
}
