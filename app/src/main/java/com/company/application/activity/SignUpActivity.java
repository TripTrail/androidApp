package com.company.application.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.company.application.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
