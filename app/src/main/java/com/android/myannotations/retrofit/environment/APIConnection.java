package com.android.myannotations.retrofit.environment;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public enum APIConnection {

    INSTANCE;

    private final Retrofit retrofit;

    APIConnection() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Api.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
