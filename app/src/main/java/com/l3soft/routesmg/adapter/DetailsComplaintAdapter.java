package com.l3soft.routesmg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.l3soft.routesmg.R;
import com.l3soft.routesmg.activities.ComplaintActivity;
import com.l3soft.routesmg.api.Api;
import com.l3soft.routesmg.entity.CustomCommentary;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Creado por ElOskar101 on 08/02/2018.
 */

public class DetailsComplaintAdapter extends RecyclerView.Adapter<DetailsComplaintAdapter.ViewHolder>{
    private List<CustomCommentary> customCommentaries;
    private Context dcContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleDetail;
        private TextView descriptionDetail;
        private ImageView imageView;
        public ImageView overflow;


        public ViewHolder(View view) {
            super(view);
            titleDetail = view.findViewById(R.id.title_details_complaint);
            descriptionDetail = view.findViewById(R.id.description_details_complaint);
            imageView = view.findViewById(R.id.image_details_complaint);
            overflow = view.findViewById(R.id.overflow_details_complaint);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DetailsComplaintAdapter(List<CustomCommentary> customCommentaries, Context context) {
        this.customCommentaries = customCommentaries;
        this.dcContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DetailsComplaintAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.details_complaint_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        DetailsComplaintAdapter.ViewHolder viewHolder = new DetailsComplaintAdapter.ViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final DetailsComplaintAdapter.ViewHolder holder, final int position) {

        CustomCommentary c = customCommentaries.get(position);
            holder.titleDetail.setText(c.getTitle());

            Glide.with(dcContext).load(c.getUrlImage())
                    .apply(new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_default_image_complaint))
                    .into(holder.imageView);

            holder.descriptionDetail.setText(c.getDescription());

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
        PopupMenu popup = new PopupMenu(dcContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.details_complaint_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new DetailsComplaintAdapter.MyMenuItemClickListener(position));

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
                case R.id.delete_complaint:
                    try {
                        deleteComplaint(customCommentaries.get(position).getCommentaryID());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;

                case R.id.update_complaint:
                    try {
                        Intent i = new Intent(dcContext, ComplaintActivity.class);
                        dcContext.startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;


                default:
            }
            return false;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return customCommentaries.size();
    }

    public void deleteComplaint(String busID){

        Call<List<CustomCommentary>> call = Api.instance(). deleteCommentary(busID);

        call.enqueue(new Callback<List<CustomCommentary>>() {
            @Override
            public void onResponse(Call<List<CustomCommentary>> call, Response<List<CustomCommentary>> response) {
                if (response.body() != null ) {
                    try {


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

}
