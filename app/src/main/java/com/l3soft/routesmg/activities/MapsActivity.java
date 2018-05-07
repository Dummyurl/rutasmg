package com.l3soft.routesmg.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.l3soft.routesmg.data.BusData;
import com.l3soft.routesmg.entity.Bus;
import com.l3soft.routesmg.entity.Place;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, OnConnectionFailedListener, OnMarkerClickListener{

    private GoogleMap mMap;
    private int numDelete;
    private String codeMarker;
    private List<Marker> markers;
    private  DownloadTask downloadTask;
    private List<Integer> positions;
    private List<Polyline> polylines;
    private Activity activity;
    private AlertDialog newBusDialog;
    private AlertDialog creatingBus;
    private View view;
    //View with data for new bus
    private EditText number;
    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
        activity = this;
        creatingBus = buildCreatingDialog();

        newBusDialog = buildDialog();
        newBusDialog.show();
        newBusDialog.setCancelable(false);

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
                //deleteRoute();
                List<Place> places = new ArrayList<>();

                if(markers.isEmpty() || markers.size() == 1){
                    Toast.makeText(getApplicationContext(),"Debe selecionar al menos 2 lugares en el mapa",
                            Toast.LENGTH_LONG).show();
                }else{
                    for(Marker marker : markers){
                        LatLng latLng = marker.getPosition();
                        String[] name = marker.getTitle().split(", ");
                        Place place = new Place();
                        place.setCoordx(latLng.latitude);
                        place.setCoordy(latLng.longitude);
                        place.setDescription(marker.getTitle());
                        place.setName(name[0]);
                        places.add(place);
                    }

                    if(!places.isEmpty()){
                        creatingBus.show();
                        Bus bus = new Bus();
                        BusData busData = new BusData(getApplicationContext());
                        //Define data of bus
                        bus.setDescription(description.getText().toString());
                        bus.setNumber(Integer.parseInt(number.getText().toString()));
                        busData.createBus(bus, places, creatingBus);
                    }else{
                        Toast.makeText(getApplicationContext(),"No se encontraron lugares selecionados",
                                Toast.LENGTH_LONG).show();
                    }
                }
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

    private AlertDialog buildDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.dialog_new_bus, null);
        description = view.findViewById(R.id.description);
        number = view.findViewById(R.id.number);
        Button next = view.findViewById(R.id.button_next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(description.getText().toString().isEmpty() || number.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Los datos ingresados no son correctos",Toast.LENGTH_SHORT).show();
                }else{
                    if(number.getText().toString().length() < 2){
                        Toast.makeText(getApplicationContext(),"El número ingresado no es correcto",Toast.LENGTH_SHORT).show();
                    }else if(description.getText().toString().length() < 5){
                        Toast.makeText(getApplicationContext(),"La descripción es muy corta",Toast.LENGTH_SHORT).show();
                    }else{
                        newBusDialog.dismiss();
                    }

                    Toast.makeText(getApplicationContext(),"Defina el recorrido de la nueva ruta",Toast.LENGTH_SHORT).show();

                }
            }
        });

        builder.setView(view);
        builder.setTitle("Datos de la nueva ruta");

        return builder.create();
    }

    public AlertDialog buildCreatingDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.dialog_creating_bus, null);

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
                setResult(RESULT_OK, getIntent());
                supportFinishAfterTransition();
            }
        });

        builder.setView(view);
        builder.setTitle("Nueva Ruta");
        return builder.create();
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
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
