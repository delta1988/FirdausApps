package com.masterweb.firdausapps.hadits.api;

import com.masterweb.firdausapps.hadits.model.BukuHaditsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UrlHadits {

    @GET("hadith")
    Call<List<BukuHaditsModel>> ShowBook();
}
