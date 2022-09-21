package com.masterweb.firdausapps.hadits.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseUrlHadits {
    private static Retrofit retrofit = null;
    private static final String IP = "hadis-api-id.vercel.app/";
    public static final String BASE_URL = "https://" +IP ;

    public static Retrofit getClient() {

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
