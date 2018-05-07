package com.l3soft.routesmg.clases;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DownloadTask extends AsyncTask<String, Void, String> {
    private GoogleMap googleMap;
    private List<Polyline> polylines;
    public DownloadTask(GoogleMap googleMap, List<Polyline> polylines) {
        this.googleMap = googleMap;
        this.polylines = polylines;
    }

    @Override
    protected String doInBackground(String... url) {

        String data = "";

        try{
            data = downloadUrl(url[0]);
        }catch(Exception e){
            Log.d("ERROR AL OBTENER INFO",e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        ParserTask parserTask = new ParserTask(googleMap, polylines);
        parserTask.execute(result);
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Create a http connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            // read from URL
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}

