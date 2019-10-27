package com.android.myannotations.retrofit.services;

import android.util.Log;

import com.android.myannotations.retrofit.environment.APIConnection;
import com.android.myannotations.retrofit.models.api.AnnotationResult;
import com.android.myannotations.retrofit.services.api.AnnotationServiceAPI;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public enum  AnnotationService {
    INSTANCE;

    private Retrofit retrofit;

    AnnotationService() {
        retrofit = APIConnection.INSTANCE.getRetrofit();
    }

    public AnnotationResult saveAnnotation(int rideId, int driverId, double lat, double lng ) throws IOException {
        AnnotationServiceAPI service = retrofit.create(AnnotationServiceAPI.class);

        Call<AnnotationResult> call = service.saveAnnotation(rideId, driverId, lat, lng);

        AnnotationResult result = null;

        Response<AnnotationResult> response = call.execute();
        result = response.body();

        if (response.code() != 200) {
            result = getResult(response);
        }

        return result;
    }

    public AnnotationResult getAnnotations(int id) throws IOException {
        AnnotationServiceAPI service = retrofit.create(AnnotationServiceAPI.class);

        Call<AnnotationResult> call = service.getAnnotations(id);

        call.enqueue(new Callback<AnnotationResult>() {
            @Override
            public void onResponse(Call<AnnotationResult> call, Response<AnnotationResult> response) {
                Log.w("RouterSaver", "=========== onResponse ============: " + response.body().getAnnotation());

            }

            @Override
            public void onFailure(Call<AnnotationResult> call, Throwable t) {
                Log.w("RouterSaver", "Error ==>: " + t);
            }
        });

        AnnotationResult result = null;
        Response<AnnotationResult> response = call.clone().execute();
        Log.w("RouterSaver", "Call: " + response.body());

        result = response.body();
        Log.w("RouterSaver", "getAnnotation: " + result.toString());

        if (response.code() != 200) {
            result = getResult(response);
        }

        return result;
    }

    private AnnotationResult getResult(Response<AnnotationResult> response) throws IOException {
        AnnotationResult result = null;

        TypeAdapter<AnnotationResult> adapter = new Gson().getAdapter(AnnotationResult.class);

        if (response.errorBody() != null)
            result = adapter.fromJson(response.errorBody().string());

        return result;
    }
}
