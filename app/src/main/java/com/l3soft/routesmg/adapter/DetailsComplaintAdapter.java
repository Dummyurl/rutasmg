package com.l3soft.routesmg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.l3soft.routesmg.R;
import com.l3soft.routesmg.entity.CustomCommentary;

import java.util.List;

/**
 * Created by ElOskar101 on 08/02/2018.
 */

public class DetailsComplaintAdapter extends RecyclerView.Adapter<DetailsComplaintAdapter.ViewHolder>{
    private List<CustomCommentary> customCommentaries;
    private Context dcContext;
    private String busID;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleDetail;
        private TextView descripctionDetail;
        private ImageView imageView;


        public ViewHolder(View view) {
            super(view);
            titleDetail = view.findViewById(R.id.title_details_complaint);
            descripctionDetail = view.findViewById(R.id.description_details_complaint);
            imageView = view.findViewById(R.id.image_details_complaint);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DetailsComplaintAdapter(List<CustomCommentary> customCommentaries, Context context, String busID) {
        this.customCommentaries = customCommentaries;
        this.dcContext = context;
        this.busID = busID;
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
    public void onBindViewHolder(DetailsComplaintAdapter.ViewHolder holder, int position) {

        CustomCommentary c = customCommentaries.get(position);

        if (busID.equals(c.getBusID())){
            System.out.println("Bussss: "+busID+", "+c.getBusID());

            holder.titleDetail.setText(c.getTitle());
            Glide.with(dcContext).load(c.getUrlImage())
                    .apply(new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_default_image_complaint)
                            .fitCenter())
                    .into(holder.imageView);

            holder.descripctionDetail.setText(c.getDescription());
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return customCommentaries.size();
    }
}
