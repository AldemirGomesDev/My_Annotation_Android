package com.android.myannotations.retrofit.theards;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;
import com.android.myannotations.R;
import com.android.myannotations.retrofit.controllers.AnnotationController;
import com.android.myannotations.retrofit.models.api.AnnotationResult;
import java.io.IOException;

public class GetAnnotationsThread extends AsyncTask<Void, Void, AnnotationResult> {

    private IThreadResult<AnnotationResult> iThreadResult;
    private Activity activity;
    private Dialog dialog;

    public GetAnnotationsThread(Activity activity) {
        this.activity = activity;
        this.dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        this.dialog.setContentView(R.layout.loading_custom);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.show();
    }

    @Override
    protected AnnotationResult doInBackground(Void... voids) {

        try {
            return new AnnotationController().getAnnotations();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(AnnotationResult routerResult) {
        Log.d("RouterSaver", "onPostExecute " + routerResult);
        dialog.dismiss();
        if (iThreadResult != null)
            iThreadResult.onResult(routerResult);
    }

    public void setOnResult(IThreadResult<AnnotationResult> iThreadResult) {
        this.iThreadResult = iThreadResult;
    }
}
