package com.masterweb.firdausapps.hadits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.masterweb.firdausapps.R;
import com.masterweb.firdausapps.hadits.adapter.BookAdapter;
import com.masterweb.firdausapps.hadits.api.BaseUrlHadits;
import com.masterweb.firdausapps.hadits.api.UrlHadits;
import com.masterweb.firdausapps.hadits.model.BukuHaditsModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HaditsActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout loading;
    RecyclerView recyclerView;
    List<BukuHaditsModel> listModel;
    UrlHadits urlHadits;
    BookAdapter bookAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hadits);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Kitab Hadits");
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loading = findViewById(R.id.loading);

        urlHadits = BaseUrlHadits.getClient().create(UrlHadits.class);
        recyclerView = findViewById(R.id.recycler_view);
        listModel = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        bookAdapter = new BookAdapter(getApplicationContext(),listModel);
        recyclerView.setAdapter(bookAdapter);
        getData();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void getData(){
        loading.setVisibility(View.VISIBLE);
        Call<List<BukuHaditsModel>> call = urlHadits.ShowBook();
        call.enqueue(new Callback<List<BukuHaditsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<BukuHaditsModel>> call, @NonNull Response<List<BukuHaditsModel>> response) {
                Log.d("list_transaksi", ""+new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    listModel = response.body();
                    Log.d("TAG","Response = "+listModel);
                    bookAdapter.setHarga(listModel);

                }else{
                    Log.d("errt", ""+response.errorBody());
                }
                loading.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(@NonNull Call<List<BukuHaditsModel>> call, @NonNull Throwable t) {
                Log.d("DataModel", ""+t.getMessage());
            }
        });
    }
}