package com.android.myannotations.retrofit.services.api;

import com.android.myannotations.models.Annotation;
import com.android.myannotations.retrofit.models.api.AnnotationResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

//interface com os endepoints "rotas" para consumir a API
public interface AnnotationServiceAPI {

    @FormUrlEncoded
    @POST("annotation")
    Call<AnnotationResult> saveAnnotation(@Field("titulo") String titulo,
                                          @Field("message") String message);

    @GET("annotation")
    Call<AnnotationResult> getAnnotations();

    @GET("annotation/{id}")
    Call<AnnotationResult> getAnnotation(@Path("id") int id);

    @Headers({
            "Content-Type: application/json",
    })
    @PUT("annotation/{id}")
    Call<AnnotationResult> updateAnnotation(@Path("id") int id, @Body Annotation annotation);

    @DELETE("annotation/{id}")
    Call<AnnotationResult> deleteAnnotation(@Path("id") int id);

}
