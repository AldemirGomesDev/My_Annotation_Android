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

public enum AnnotationService {
    INSTANCE;

    private Retrofit retrofit;
    //Intanciando o retrofit
    AnnotationService() {
        retrofit = APIConnection.INSTANCE.getRetrofit();
    }
    //metodo que chama o serviço para salvar a anotação
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
    //metodo que chama o serviço para buscar todas anotações
    public AnnotationResult getAnnotations() throws IOException {
        AnnotationServiceAPI service = retrofit.create(AnnotationServiceAPI.class);
        Call<AnnotationResult> call = service.getAnnotations();

        AnnotationResult result = null;
        Response<AnnotationResult> response = call.clone().execute().isSuccessful() ? call.clone().execute() : null;
        if (response != null) {
            result = response.body();
            if (response.code() != 200) {
                result = getResult(response);
            }
        }

        return result;

    }
    //metodo que chama o serviço para busca anotação pelo id
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
    //metodo que chama o serviço para atualizar a anotação
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
    //metodo que chama o serviço para deletar uma anotação
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
    //metodo que retorna o resultaddo da busca a API
    private AnnotationResult getResult(Response<AnnotationResult> response) throws IOException {
        AnnotationResult result = null;

        TypeAdapter<AnnotationResult> adapter = new Gson().getAdapter(AnnotationResult.class);

        if (response.errorBody() != null)
            result = adapter.fromJson(response.errorBody().string());

        return result;
    }
}
