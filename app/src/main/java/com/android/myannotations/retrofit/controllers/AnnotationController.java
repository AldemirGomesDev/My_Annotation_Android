package com.android.myannotations.retrofit.controllers;

import com.android.myannotations.models.Annotation;
import com.android.myannotations.retrofit.models.api.AnnotationResult;
import com.android.myannotations.retrofit.services.AnnotationService;

import java.io.IOException;

//classe Controller que é chamada pelo metodos das telas e chama os serviços específicos
public class AnnotationController {
    private AnnotationService annotationService;

    public AnnotationController(){
        annotationService = AnnotationService.INSTANCE;
    }

    public AnnotationResult saveAnnotation(String titulo, String message) throws IOException {
        return annotationService.saveAnnotation(titulo, message);
    }

    public AnnotationResult getAnnotation(int rideId) throws IOException {
        return annotationService.getAnnotation(rideId);
    }

    public AnnotationResult getAnnotations() throws IOException {
        return annotationService.getAnnotations();
    }

    public AnnotationResult updateAnnotation(int id, Annotation annotation) throws IOException {
        return annotationService.updateAnnotation(id, annotation);
    }

    public AnnotationResult deleteAnnotation(int id) throws IOException {
        return annotationService.deleteAnnotation(id);
    }
}
