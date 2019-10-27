package com.android.myannotations.retrofit.services.api;

import com.android.myannotations.retrofit.models.api.AnnotationResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AnnotationServiceAPI {

    @FormUrlEncoded
    @POST("create_router.php")
    Call<AnnotationResult> saveAnnotation(@Field("ride_id") int rideId,
                                      @Field("driver_id") int driverId,
                                      @Field("lat") double lat,
                                      @Field("lng") double lng
    );

    @GET("annotation/{id}")
    Call<AnnotationResult> getAnnotations(@Path("id") int id);

}
