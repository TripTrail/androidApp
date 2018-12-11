package com.company.application;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.company.application.model.TokenResponse;
import com.company.application.util.Constant;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        /* Hide Title Bar **/
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        setContentView(R.layout.activity_login);
    }

    public void showSignUpActivity(View view){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

    public void signIn(final View view){
        EditText emailField = findViewById(R.id.email_field);
        String emailId = emailField.getText().toString();

        EditText passwordField = findViewById(R.id.password_field);
        String password = passwordField.getText().toString();

        Ion.with(view.getContext()).load(Constant.AUTH_SERVER_BASE_URL + Constant.TOKEN_URL)
                .addHeader("Authorization", "Basic VFRfQ0xJRU5UX0lEOlRUX0NMSUVOVF9TRUNSRVQ=")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .setBodyParameter("grant_type", "password")
                .setBodyParameter("username", emailId)
                .setBodyParameter("password", password)
                .as(new TypeToken<TokenResponse>(){})
                .setCallback(new FutureCallback<TokenResponse>() {
                    @Override
                    public void onCompleted(Exception e, TokenResponse result) {
                        if(e==null){
                            if(result.getAccess_token()!=null && !result.getAccess_token().trim().equals("")){
                                Log.d("TOKEN", result.getAccess_token());
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }else if(result.getStatus()!=null && !result.getStatus()){
                                Toast.makeText(getBaseContext(), result.getMessage(), Toast.LENGTH_LONG).show();
                            }else{
                                Log.e("TOKEN ERROR", result.toString());
                                Toast.makeText(getBaseContext(), "Something Went Wrong!", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getBaseContext(), "Something Went Wrong!", Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }
}
