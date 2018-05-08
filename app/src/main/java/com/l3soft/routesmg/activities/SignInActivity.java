package com.l3soft.routesmg.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.l3soft.routesmg.MainActivity;
import com.l3soft.routesmg.R;
import com.l3soft.routesmg.data.AccessTokenData;
import com.l3soft.routesmg.entity.User;

public class SignInActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private ProgressBar progressBar;
    private Button signIn;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        activity = this;
        initViews();
    }

    private void initViews() {
        email =  findViewById(R.id.email);
        password =  findViewById(R.id.password);
        signIn =  findViewById(R.id.sign_in);
        Button signUp =  findViewById(R.id.sign_up);
        progressBar =  findViewById(R.id.signInProgressBar);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn.setEnabled(false);
                signIn();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * This method is for login user
     */
    private void signIn() {
        if (email.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),R.string.activity_sing_in_message_empty_email, Toast.LENGTH_LONG).show();
        } else if(password.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.activity_sing_in_message_empty_password, Toast.LENGTH_LONG).show();
        } else {
            loginRequest(email.getText().toString(), password.getText().toString());
        }
    }

    /**
     *
     * @param email
     * @param password
     * To make http request
     */
    private void loginRequest(String email, String password) {
        // instance a user
        progressBar.setVisibility(View.VISIBLE);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setTtl(31556926);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        AccessTokenData accessTokenData = new AccessTokenData(activity);
        accessTokenData.loginRequest(user,progressBar,intent,signIn);

    }
}
