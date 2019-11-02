package com.android.myannotations.retrofit.theards;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.android.myannotations.retrofit.controllers.AnnotationController;
import com.android.myannotations.retrofit.models.api.AnnotationResult;
import java.io.IOException;

public class SetAnnotationThread extends AsyncTask<Void, Void, AnnotationResult> {

    private IThreadResult<AnnotationResult> iThreadResult;
    private String titulo;
    private String message;
    private ProgressDialog progressBar;
    private Context context;

    public SetAnnotationThread(String titulo, String message, Context context) {
        this.titulo = titulo;
        this.message = message;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = new ProgressDialog(context);
        progressBar.setTitle("Realizando o carregamento dos dados");
        progressBar.setMessage("Aguarde o fim da requisição...");
        progressBar.show();
    }

    @Override
    protected AnnotationResult doInBackground(Void... voids) {

        try {
            return new AnnotationController().saveAnnotation(titulo, message);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(AnnotationResult routerResult) {
        progressBar.dismiss();
        Log.w("RouterSaver", "Busca de Annotation => " + routerResult);
        if (iThreadResult != null)
            iThreadResult.onResult(routerResult);
    }

    public void setOnResult(IThreadResult<AnnotationResult> iThreadResult) {
        this.iThreadResult = iThreadResult;
    }
}
