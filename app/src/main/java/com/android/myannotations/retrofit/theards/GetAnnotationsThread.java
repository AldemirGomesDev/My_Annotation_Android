package com.android.myannotations.retrofit.theards;

import android.os.AsyncTask;
import android.util.Log;

import com.android.myannotations.retrofit.controllers.AnnotationController;
import com.android.myannotations.retrofit.models.Annotation;
import com.android.myannotations.retrofit.models.api.AnnotationResult;

import java.io.IOException;

public class GetAnnotationsThread extends AsyncTask<Void, Void, AnnotationResult> {

    private IThreadResult<AnnotationResult> iThreadResult;
    private int id;

    public GetAnnotationsThread(int id) {
        this.id = id;
    }

    @Override
    protected AnnotationResult doInBackground(Void... voids) {

        try {
            return new AnnotationController().getAnnotations(id);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(AnnotationResult routerResult) {
        Log.w("RouterSaver", "Busca de Annotation => " + routerResult);
        if (iThreadResult != null)
            iThreadResult.onResult(routerResult);
    }

    public void setOnResult(IThreadResult<AnnotationResult> iThreadResult) {
        this.iThreadResult = iThreadResult;
    }
}
