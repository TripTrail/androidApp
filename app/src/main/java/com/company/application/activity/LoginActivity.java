package com.company.application.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.company.application.MainActivity;
import com.company.application.R;
import com.company.application.model.TokenResponse;
import com.company.application.util.Constant;
import com.company.application.util.URLConstant;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import static com.company.application.util.HTTPConstants.*;
import static com.company.application.util.Constant.*;
import static com.company.application.util.MessageConstant.*;


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

        Ion.with(view.getContext()).load(URLConstant.AUTH_SERVER_BASE_URL + URLConstant.TOKEN_URL)
                .addHeader(AUTHORIZATION, BASIC_AUTH)
                .addHeader(CONTENT_TYPE, CONTENT_TYPE_BASIC_AUTH)
                .setBodyParameter(GRANT_TYPE, GRANT_TYPE_PASSWORD)
                .setBodyParameter(PARAM_USERNAME, emailId)
                .setBodyParameter(PARAM_PASSWORD, password)
                .as(new TypeToken<TokenResponse>(){})
                .setCallback(new FutureCallback<TokenResponse>() {
                    @Override
                    public void onCompleted(Exception e, TokenResponse result) {
                        if(e==null){
                            if(result.getAccess_token()!=null && !result.getAccess_token().trim().equals("")){
                                Log.d(LOG_TAG_TOKEN_SUCCESS, result.getAccess_token());

                                getSharedPreferences(SPREFS_NAME, Context.MODE_PRIVATE)
                                        .edit()
                                        .putString(ACCESS_TOKEN, result.getAccess_token())
                                        .putString(REFRESH_TOKEN, result.getRefresh_token())
                                        .apply();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);

                            }else if(result.getStatus()!=null && !result.getStatus()){
                                Toast.makeText(getBaseContext(), result.getMessage(), Toast.LENGTH_LONG).show();
                            }else{
                                Log.e(LOG_TAG_TOKEN_ERROR, result.toString());
                                Toast.makeText(getBaseContext(), INTERNAL_ERROR, Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Log.e(LOG_TAG_TOKEN_ERROR, e.getMessage());
                            Toast.makeText(getBaseContext(), INTERNAL_ERROR, Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }
}
