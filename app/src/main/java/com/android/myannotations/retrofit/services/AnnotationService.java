package com.android.myannotations.retrofit.services;

import android.util.Log;

import com.android.myannotations.retrofit.environment.APIConnection;
import com.android.myannotations.models.Annotation;
import com.android.myannotations.retrofit.models.api.AnnotationResult;
import com.android.myannotations.retrofit.services.api.AnnotationServiceAPI;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public enum  AnnotationService {
    INSTANCE;

    private Retrofit retrofit;

    AnnotationService() {
        retrofit = APIConnection.INSTANCE.getRetrofit();
    }

    public AnnotationResult saveAnnotation(String titulo, String message) throws IOException {
        AnnotationServiceAPI service = retrofit.create(AnnotationServiceAPI.class);

        Call<AnnotationResult> call = service.saveAnnotation(titulo, message);

        AnnotationResult result = null;
        Response<AnnotationResult> response = call.execute();
        result = response.body();
        Log.w("RouterSaver", "setAnnotation: " + result.getMsg());

        if (response.code() != 200) {
            result = getResult(response);
        }

        return result;
    }

    public AnnotationResult getAnnotations() throws IOException {
//         final AnnotationResult[] result = {null};
        AnnotationServiceAPI service = retrofit.create(AnnotationServiceAPI.class);
        Call<AnnotationResult> call = service.getAnnotations();

        AnnotationResult result = null;
        Response<AnnotationResult> response = call.clone().execute().isSuccessful() ? call.clone().execute() : null;
        if (response != null){
            result = response.body();
            if (response.code() != 200) {
                result = getResult(response);
            }
        }

    return result;

//        call.enqueue(new Callback<AnnotationResult>() {
//            @Override
//            public void onResponse(Call<AnnotationResult> call, Response<AnnotationResult> response) {
//                if (response.isSuccessful()) {
//                    // use response data and do some fancy stuff :)
//                    result[0] = response.body();
//                    Log.d("RouterSaver", "onResponse: isSuccessful " + result[0]);
//
//                } else {
//                    Log.d("RouterSaver", "onResponse: " + response.toString());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<AnnotationResult> call, Throwable t) {
//                // there is more than just a failing request (like: no internet connection)
//                Log.d("RouterSaver", "onFailure: " + t.getMessage());
//
//            }
//        });
//        Log.d("RouterSaver", "Return result[0] " + result[0]);
//        return result[0];
    }

    public AnnotationResult getAnnotation(int id) throws IOException {
        AnnotationServiceAPI service = retrofit.create(AnnotationServiceAPI.class);

        Call<AnnotationResult> call = service.getAnnotation(id);


        AnnotationResult result = null;
        Response<AnnotationResult> response = call.clone().execute();

        result = response.body();

        if (response.code() != 200) {
            result = getResult(response);
        }

        return result;
    }

    public AnnotationResult updateAnnotation(int id, Annotation annotation) throws IOException {
        AnnotationServiceAPI service = retrofit.create(AnnotationServiceAPI.class);
        Call<AnnotationResult> call = service.updateAnnotation(id, annotation);

        AnnotationResult result = null;
        Response<AnnotationResult> response = call.execute();
        Log.w("RouterSaver", "Call: " + response.body());

        result = response.body();

        if (response.code() != 200) {
            result = getResult(response);
        }

        return result;
    }
    public AnnotationResult deleteAnnotation(int id) throws IOException {
        AnnotationServiceAPI service = retrofit.create(AnnotationServiceAPI.class);

        Call<AnnotationResult> call = service.deleteAnnotation(id);

        AnnotationResult result = null;
        Response<AnnotationResult> response = call.execute();
        Log.w("RouterSaver", "Call: " + response.body());

        result = response.body();

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
