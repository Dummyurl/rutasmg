package com.l3soft.routesmg.data;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.l3soft.routesmg.MainActivity;
import com.l3soft.routesmg.R;
import com.l3soft.routesmg.api.Api;
import com.l3soft.routesmg.db.AppDatabase;
import com.l3soft.routesmg.entity.AccessToken;
import com.l3soft.routesmg.entity.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccessTokenData {
    private AppDatabase db;
    private Context context;
    private Activity activity;
    public AccessTokenData(Activity activity) {
        this.context = activity.getApplicationContext();
        this.activity = activity;
        db = Room.databaseBuilder(context,
                AppDatabase.class, "routesDB")
                .allowMainThreadQueries()
                .build();
    }
    public AccessTokenData(){

    }

    public void loginRequest(User userReg) {

        User user = new User();

        user.setTtl(31556926);
        user.setEmail(userReg.getEmail());
        user.setPassword(userReg.getPassword());
        // create call
        Call<AccessToken> call = Api.instance().login(user);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, final Response<AccessToken> response) {
                if (response.body() != null) {
                    Log.i("AccessTokenData ID",response.body().getId());
                    saveInDB(response.body());
                    activity.finish();
                    Intent intent = new Intent(context, MainActivity.class);
                    activity.startActivity(intent);
                }else{
                    Log.e("AccessTokenData ERROR",response.code()+"");
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.e("AccessTokenData",t.getMessage());
            }
        });

    }

    public void loginRequest(User user, final ProgressBar progressBar, final Intent intent
            ,final Button signIn) {
        // create call
        Call<AccessToken> call = Api.instance().login(user);
        user.setTtl(31556926);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, final Response<AccessToken> response) {
                if (response.body() != null) {
                    Log.e("AccessTokenData EXITO",response.body().getId());
                    saveInDB(response.body());
                    activity.finish();
                    progressBar.setVisibility(View.GONE);
                    activity.startActivity(intent);
                }else{
                    progressBar.setVisibility(View.GONE);
                    Log.e("AccessTokenData ERROR",response.code()+"");
                    Toast.makeText(activity,"Error, correo o contraseña incorrectos",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    signIn.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.e("AccessTokenData ERROR",t.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context,context.getString(R.string.message_without_connection),Toast.LENGTH_SHORT).show();
                signIn.setEnabled(true);
            }
        });

    }

    public void saveInDB(AccessToken accessToken){
        if(!validExist()){
            db.accessTokenDao().insertAccessToken(accessToken);
        }
    }

    public AccessToken loadDB(){
        return  db.accessTokenDao().loadAccessToken();
    }

    public boolean validExist(){

        boolean band = false;
        AccessToken accessTokenDB = loadDB();

        if(accessTokenDB != null){
            band = true;
        }

        return band;
    }

    public void deleteAccessTokenDB(AccessToken accessToken){
        db.accessTokenDao().deleteAccessToken(accessToken);
    }
}