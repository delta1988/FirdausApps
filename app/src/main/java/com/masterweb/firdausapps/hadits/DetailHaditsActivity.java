package com.masterweb.firdausapps.hadits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.masterweb.firdausapps.R;
import com.masterweb.firdausapps.hadits.api.BaseUrlHadits;
import com.masterweb.firdausapps.hadits.api.UrlHadits;

public class DetailHaditsActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout loading;
    RecyclerView recyclerView;
    UrlHadits urlHadits;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hadits);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Hadits "+getIntent().getExtras().getString("title"));
        toolbar.setSubtitle(getIntent().getExtras().getString("subtitle"));
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loading = findViewById(R.id.loading);

        urlHadits = BaseUrlHadits.getClient().create(UrlHadits.class);
        recyclerView = findViewById(R.id.recycler_view);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}