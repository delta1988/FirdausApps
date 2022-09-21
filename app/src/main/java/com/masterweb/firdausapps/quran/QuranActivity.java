package com.masterweb.firdausapps.quran;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.masterweb.firdausapps.R;
import com.masterweb.firdausapps.clasess.MasterFunction;
import com.masterweb.firdausapps.quran.adapter.SuratAdapter;
import com.masterweb.firdausapps.quran.model.SuratModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuranActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<SuratModel> dataArrayList;
    SuratAdapter adapter;
    RequestQueue requestQueue;
    Toolbar toolbar;
    LinearLayout loading;
    MasterFunction masterFunction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran);
        recyclerView = findViewById(R.id.recycler_view);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Alquran Digital");
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loading = findViewById(R.id.loading);

        adapter = new SuratAdapter(dataArrayList,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        dataArrayList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        parseJson();


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.cari_surat, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Pencarian Surat");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                cari(newText);
                return false;
            }
        });
        return true;
    }
    private void filter(String text) {
        ArrayList<SuratModel> filteredlist = new ArrayList<>();
        for (SuratModel item : dataArrayList) {
            if (item.getNama().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Data tidak ditemukan",Toast.LENGTH_LONG).show();
        } else {
            adapter.setHarga(filteredlist);
        }
    }
    private void cari(String text) {
        ArrayList<SuratModel> filteredlist = new ArrayList<>();
        for (SuratModel item : dataArrayList) {
            if (item.getNama().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Data tidak ditemukan",Toast.LENGTH_LONG).show();
        } else {
            adapter.setHarga(filteredlist);
        }
    }
    private void parseJson(){
        loading.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, "https://quran-endpoint.vercel.app/quran", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("viewprovider", "onResponse: "+response);
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        JSONObject asma = jsonObject1.getJSONObject("asma");
                        JSONObject ar = asma.getJSONObject("ar");
                        String arab = ar.getString("long");
                        JSONObject name = asma.getJSONObject("id");
                        String nama = name.getString("long");
                        JSONObject arti = asma.getJSONObject("translation");
                        String keterangan = arti.getString("id");
                        JSONObject type = jsonObject1.getJSONObject("type");
                        String turun = type.getString("id");
                        String ayat = jsonObject1.getString("ayahCount");
                        String nomor = jsonObject1.getString("number");
                        String ket = keterangan+" / "+turun;
                        String nama_surat = nama+" - "+ayat+" Ayat";
                        dataArrayList.add(new SuratModel(nomor,arab, nama_surat, ket));
                        adapter = new SuratAdapter(dataArrayList, getApplicationContext());
                        recyclerView.setAdapter(adapter);
                        Log.d("hasil", arab+" "+nama+" "+ket);
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