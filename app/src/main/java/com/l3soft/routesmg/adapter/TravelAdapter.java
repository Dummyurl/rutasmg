package com.l3soft.routesmg.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.l3soft.routesmg.R;
import com.l3soft.routesmg.clases.DownloadTask;
import com.l3soft.routesmg.data.PlaceData;
import com.l3soft.routesmg.data.TravelData;
import com.l3soft.routesmg.entity.Route;

import java.util.ArrayList;
import java.util.List;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.MyViewHolder>{

    private List<Route> routes;
    private int actualization;

    private void definePositionCamera(@NonNull GoogleMap mMap){
        LatLng latLng = new LatLng(12.115211825907474 ,-86.23661834746599);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback{
        public TextView number;
        public Context context;
        public MapView mapView;
        private GoogleMap mMap;
        private DownloadTask downloadTask;
        public List<Marker> markers;
        public List<Polyline> polylines;
        public List<LatLng> latLngs;
        public MyViewHolder(View view) {
            super(view);
            context = view.getContext();
            number = view.findViewById(R.id.bus_number);
            mapView = view.findViewById(R.id.card_map);
            //Initialization for attribute maps
            markers = new ArrayList<>();
            polylines = new ArrayList<>();
            latLngs = new ArrayList<>();
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setMinZoomPreference(14.05f);
            //Move to camera at Managua city
            definePositionCamera(mMap);

            for(int i = 0; i < latLngs.size(); i++){
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("En desarrollo..."));
                markers.add(marker);
            }
            traceRoute();
        }

        private String getDirectionsUrl(LatLng origin,LatLng dest){

            String str_origin = "origin="+origin.latitude+","+origin.longitude;
            String str_dest = "destination="+dest.latitude+","+dest.longitude;
            String sensor = "sensor=false";
            String parameters = str_origin+"&"+str_dest+"&"+sensor;
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
            System.out.println("URL $$$: "+url);

            return url;
        }

        private void traceRoute(){
            String url;
            for(int i = 0; i < (markers.size()-1); i++){
                url = getDirectionsUrl(markers.get(i).getPosition(),markers.get(i+1).getPosition());
                Log.i("LanLng 1",markers.get(i).getPosition().latitude+" "+markers.get(i).getPosition().longitude);
                Log.i("LatLng 2",markers.get(i+1).getPosition().latitude+" "+markers.get(i+1).getPosition().longitude);
                downloadTask = new DownloadTask(mMap,polylines);
                downloadTask.execute(url);

            }
            Log.i("LanLng error","1");
        }
    }

    public TravelAdapter(List<Route> routes) {
        this.routes = routes;
        actualization = 0;
    }

    @Override
    public TravelAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.travel_card, parent, false);
        return new TravelAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(TravelAdapter.MyViewHolder holder, int position) {
        if(actualization < routes.size()){
            Route route = routes.get(position);
            Log.i("ROUTE GET ID",route.getId());
            TravelData travelData = new TravelData();
            travelData.getTravel(holder.number,route.getTravelID());
            PlaceData placeData = new PlaceData(holder.context);
            placeData.getPlacesForID(route.getId(), holder);
            actualization ++;
        }
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }
}
