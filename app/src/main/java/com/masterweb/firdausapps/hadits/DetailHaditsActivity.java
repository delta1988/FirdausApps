package com.masterweb.firdausapps.hadits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.masterweb.firdausapps.R;
import com.masterweb.firdausapps.hadits.adapter.DetailHaditsAdapter;
import com.masterweb.firdausapps.hadits.api.BaseUrlHadits;
import com.masterweb.firdausapps.hadits.api.UrlHadits;
import com.masterweb.firdausapps.hadits.model.DetailHaditsModel;
import com.masterweb.firdausapps.quran.adapter.SuratAdapter;
import com.masterweb.firdausapps.quran.model.SuratModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailHaditsActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout loading;
    RecyclerView recyclerView;
    List<DetailHaditsModel> dataArrayList;
    DetailHaditsAdapter adapter;
    RequestQueue requestQueue;
    TextView total,total_halaman,halaman,prev,next;
    int pages=1;
    String url;
    String kitab;
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
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new DetailHaditsAdapter(getApplicationContext(),dataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        dataArrayList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        kitab = getIntent().getExtras().getString("slug");
        parseJson();
        total = findViewById(R.id.total);
        total_halaman = findViewById(R.id.total_halaman);
        halaman = findViewById(R.id.halaman);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        next.setOnClickListener(V->{
            pages++;
            parseJson();
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void parseJson(){
        url = "https://hadis-api-id.vercel.app/hadith/"+kitab+"?page="+pages+"&limit=20";
        loading.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                recyclerView.setVisibility(View.VISIBLE);
                Log.d("viewprovider", "onResponse: "+response);
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String nomor = jsonObject1.getString("number");
                        String arab = jsonObject1.getString("arab");
                        String id = jsonObject1.getString("id");
                        dataArrayList.add(new DetailHaditsModel(nomor,arab, id));
                        adapter = new DetailHaditsAdapter(getApplicationContext(),dataArrayList);
                        recyclerView.setAdapter(adapter);
                    }
                    JSONObject pagination = jsonObject.getJSONObject("pagination");
                    String totalItems = pagination.getString("totalItems");
                    String currentPage = pagination.getString("currentPage");
                    String pageSize = pagination.getString("pageSize");
                    String totalPages = pagination.getString("totalPages");
                    if (currentPage.matches("1")){
                        next.setVisibility(View.VISIBLE);
                    }else{
                        next.setVisibility(View.VISIBLE);
                        prev.setVisibility(View.VISIBLE);
                    }

                    total.setText("Total Hadits : "+totalItems+" Hadits");
                    total_halaman.setText("Total Halaman : "+totalPages+" Halaman");
                    halaman.setText("Halaman "+currentPage);
                    loading.setVisibility(View.GONE);
                }catch (JSONException e){
                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                loading.setVisibility(View.GONE);
            }
        });

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
}