package com.masterweb.firdausapps.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.masterweb.firdausapps.R;
import com.masterweb.firdausapps.clasess.MasterFunction;
import com.masterweb.firdausapps.clasess.Session;

public class StartActivity extends AppCompatActivity {
    Session session;
    MasterFunction masterFunction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        session = new Session(getApplicationContext());
        masterFunction = new MasterFunction(getApplicationContext());
    }

    public void startApplication(View view) {
        session.createSession(masterFunction.device());
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
    }
}