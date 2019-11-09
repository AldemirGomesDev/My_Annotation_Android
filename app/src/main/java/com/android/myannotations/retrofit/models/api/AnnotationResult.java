package com.android.myannotations.retrofit.models.api;

import com.android.myannotations.models.Annotation;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnnotationResult extends APIResult {

    @SerializedName("annotation")
    private Annotation annotation;
    @SerializedName("annotations")
    private List<Annotation> annotations;

    public Annotation getAnnotation() {
        return annotation;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    @Override
    public String toString() {
        return "AnnotationResult{" +
                "annotation=" + annotation +
                ", annotations=" + annotations +
                ", result=" + result +
                ", msg='" + msg + '\'' +
                '}';
    }
}
