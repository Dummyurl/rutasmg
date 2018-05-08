package com.l3soft.routesmg.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.signin.SignIn;
import com.l3soft.routesmg.R;
import com.l3soft.routesmg.api.Api;
import com.l3soft.routesmg.data.AccessTokenData;
import com.l3soft.routesmg.entity.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText realm;
    private EditText username;
    private EditText email;
    private EditText password;
    private Button signUp;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        activity = this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás!");
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Initialize the views
     */

    private void initViews() {
        realm =  findViewById(R.id.nameSignUp);
        username =  findViewById(R.id.userNameSignUp);
        email =  findViewById(R.id.emailSignUp);
        password =  findViewById(R.id.passwordSignUp);
        signUp =  findViewById(R.id.sign_up_btn);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Creando nuevo usuario",Toast.LENGTH_SHORT).show();
                signUp();
            }
        });

    }

    /**
     * Verify that the fields are not empty and create a new user.
     */

    private void signUp() {
        if (realm.getText().toString().isEmpty() || username.getText().toString().isEmpty() ||  email.getText().toString().isEmpty() || password.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(),getString(R.string.activity_sing_up_empty_fields_message), Toast.LENGTH_LONG).show();
        }
        else
        {
            // instance a user
            User user = new User();
            user.setUsername(username.getText().toString());
            user.setRealm(realm.getText().toString());
            user.setEmail(email.getText().toString());
            user.setPassword(password.getText().toString());

            // create call
            Call<User> call = Api.instance().signUp(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.body() != null) {
                        Log.i("SIGN UP CREATE",response.body().getId());
                        AccessTokenData accessToken = new AccessTokenData(activity);
                        response.body().setPassword(password.getText().toString());
                        accessToken.loginRequest(response.body());
                    }else{
                        Log.e("SIGN UP ERROR",response.code()+"");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("SIGN UP ERROR CONN",t.getMessage()+"");
                }
            });
        }
    }

    /**
     * Request the new user's access
     */
}
