package com.android.myannotations.retrofit.controllers;

import com.android.myannotations.retrofit.models.api.AnnotationResult;
import com.android.myannotations.retrofit.services.AnnotationService;

import java.io.IOException;

public class AnnotationController {
    private AnnotationService annotationService;

    public AnnotationController(){
        annotationService = AnnotationService.INSTANCE;
    }

    public AnnotationResult saveAnnotation(int rideId, int driverId, double lat, double lng) throws IOException {
        return annotationService.saveAnnotation(rideId, driverId, lat, lng);
    }

    public AnnotationResult getAnnotations(int rideId) throws IOException {
        return annotationService.getAnnotations(rideId);
    }
}
