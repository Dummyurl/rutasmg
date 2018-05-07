package com.l3soft.routesmg.activities;

import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.l3soft.routesmg.R;
import com.l3soft.routesmg.clases.DownloadTask;

import java.util.ArrayList;
import java.util.List;

public class NewTravelActivity extends AppCompatActivity implements OnMapReadyCallback, OnConnectionFailedListener, OnMarkerClickListener{

    private GoogleMap mMap;
    private int numDelete;
    private String codeMarker;
    private List<Marker> markers;
    private  DownloadTask downloadTask;
    private List<Integer> positions;
    private List<Polyline> polylines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_travel);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Initialization of variables
        markers = new ArrayList<>();
        positions = new ArrayList<>();
        numDelete = 0;
        codeMarker = "";
        polylines = new ArrayList<>();
        Button fabTraceRoute = findViewById(R.id.trace_route);
        fabTraceRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                traceRoute();
            }
        });

        Button fabClean = findViewById(R.id.clean);
        fabClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRoute();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(15.00f);
        mMap.setMaxZoomPreference(20.00f);
        //Move to camera at Managua city
        definePositionCamera(mMap);
        //Active click for Marker
        mMap.setOnMarkerClickListener(this);
        //Active click in map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {
                // Add a marker and move the camera
                LatLng latLng = new LatLng(point.latitude,point.longitude);
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(getAdressName(latLng)));
                markers.add(marker);
                Log.i("LOCATION: ",""+latLng.latitude+" "+latLng.longitude);

            }
        });
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        String clickCount;
        clickCount = marker.getId();
        if (numDelete < 1) {

            numDelete++;
            codeMarker = clickCount;

        } else if (!clickCount.equals(codeMarker)){
            numDelete = 0;
        } else if (clickCount.equals(codeMarker)){

            char[] code = codeMarker.toCharArray();
            positions.add(Integer.parseInt(""+code[1]));
            marker.remove();
            for(int i = 0; i < markers.size();i++){
                Log.i("CODES: MARC",codeMarker+" / "+markers.get(i).getId());
                if(markers.get(i).getId().equals(codeMarker)){
                    markers.remove(i);
                    break;
                }
            }
            deleteRoute();
            numDelete = 0;
            codeMarker = "";
        }

        return false;
    }

    private void deleteRoute(){
        if(!polylines.isEmpty()){
            for(Polyline polyline : polylines){
                polyline.remove();
            }
        }
        polylines.clear();
    }

    private String getAdressName(LatLng location){
        String adreesName = "";

        Geocoder geocoder = new Geocoder(getApplicationContext());
        //Element list that will contain the address
        List<Address> direcciones = null;

        // Function to obtain the name from the geocoder
        try {
            direcciones = geocoder.getFromLocation(location.latitude, location.longitude,1);
        } catch (Exception e) {
            Log.d("Error", "Error en geocoder:"+e.toString());
        }

        // Function that determines whether a result was obtained or not
        if(direcciones != null && direcciones.size() > 0 ){

            // We create the address object
            Address address = direcciones.get(0);

            adreesName = String.format("%s, %s, %s",
                    address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : address.getFeatureName(),
                    address.getLocality(),
                    address.getCountryName());
        }

        return adreesName;
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
        deleteRoute();
        for(int i = 0; i < (markers.size()-1); i++){
            url = getDirectionsUrl(markers.get(i).getPosition(),markers.get(i+1).getPosition());
            Log.i("LanLng 1",markers.get(i).getPosition().latitude+" "+markers.get(i).getPosition().longitude);
            Log.i("LatLng 2",markers.get(i+1).getPosition().latitude+" "+markers.get(i+1).getPosition().longitude);
            downloadTask = new DownloadTask(mMap,polylines);
            downloadTask.execute(url);

        }
        Log.i("LanLng error","1");
    }

    private void definePositionCamera(GoogleMap mMap){
        LatLng latLng = new LatLng(12.115211825907474 ,-86.23661834746599);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

