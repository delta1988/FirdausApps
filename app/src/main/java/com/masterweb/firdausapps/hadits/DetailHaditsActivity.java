package com.masterweb.firdausapps.hadits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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
        String url = "https://hadis-api-id.vercel.app/hadith/"+getIntent().getExtras().getString("slug")+"?page=1&limit=20";
        parseJson(url);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void parseJson(String url){
        loading.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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