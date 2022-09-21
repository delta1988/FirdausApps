package com.masterweb.firdausapps.quran;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
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
import com.masterweb.firdausapps.clasess.MasterFunction;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SuratActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout loading;
    TextView arab;
    MasterFunction masterFunction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surat);
        masterFunction = new MasterFunction(SuratActivity.this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getExtras().getString("title").replace("Surah",""));
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loading = findViewById(R.id.loading);

        arab = findViewById(R.id.arab);
        arab.setMovementMethod(new ScrollingMovementMethod());
        showArab();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_surat, menu);
        return true;
    }

    private void showArab(){
        loading.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, "https://quran-endpoint.vercel.app/quran/"+getIntent().getExtras().getString("id"), new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("viewprovider", "onResponse: "+response);
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = data.getJSONArray("ayahs");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        JSONObject asma = jsonObject1.getJSONObject("text");
                        String ayat_arab = asma.getString("ar");
                        JSONObject ayat = jsonObject1.getJSONObject("number");
                        String no_ayat = ayat.getString("insurah");
                        String ayats = masterFunction.huruf_arab(no_ayat);
                        arab.append(ayat_arab+""+ayats+" ");
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