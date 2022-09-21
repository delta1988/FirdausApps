package com.masterweb.firdausapps.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.masterweb.firdausapps.R;
import com.masterweb.firdausapps.clasess.MasterFunction;
import com.masterweb.firdausapps.clasess.Session;
import com.masterweb.firdausapps.hadits.HaditsActivity;
import com.masterweb.firdausapps.quran.QuranActivity;
import com.masterweb.firdausapps.service.GPSTracker;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    MasterFunction masterFunction;
    TextView masehi,hijrah,lokasi,subuh,fajar,dzuhur,ashar,maghrib,isya;
    TextView next,next_time,selisih,timer;
    GPSTracker gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gpsTracker = new GPSTracker(getApplicationContext());
        masterFunction = new MasterFunction(getApplicationContext());
        masehi = findViewById(R.id.masehi);
        masehi.setText(masterFunction.tglIndo());
        hijrah = findViewById(R.id.hijrah);
        hijrah.setText(masterFunction.tglHijriyah());
        lokasi = findViewById(R.id.lokasi);
        if (gpsTracker.canGetLocation()){
            lokasi.setText(gpsTracker.getAddress());
        }else{
            gpsTracker.showSettingsAlert();
        }
        subuh = findViewById(R.id.subuh);
        subuh.setText(masterFunction.time_subuh());
        fajar = findViewById(R.id.terbit);
        dzuhur = findViewById(R.id.dzuhur);
        ashar = findViewById(R.id.ashar);
        maghrib = findViewById(R.id.maghrib);
        isya = findViewById(R.id.isya);
        fajar.setText(masterFunction.time_fajar());
        dzuhur.setText(masterFunction.time_dzuhur());
        ashar.setText(masterFunction.time_ashar());
        maghrib.setText(masterFunction.time_maghrib());
        isya.setText(masterFunction.time_isya());

        next = findViewById(R.id.next);
        next_time = findViewById(R.id.next_time);
        selisih = findViewById(R.id.selisih);
        timer = findViewById(R.id.timer);
        masterFunction.getNextTime(next,next_time,selisih,timer);
    }

    public void quran(View view) {
        Intent intent = new Intent(getApplicationContext(), QuranActivity.class);
        startActivity(intent);
    }

    public void hadits(View view) {
        Intent intent = new Intent(getApplicationContext(), HaditsActivity.class);
        startActivity(intent);
    }
}