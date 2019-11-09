package com.android.myannotations.activitys;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.android.myannotations.R;
import com.android.myannotations.retrofit.controllers.AnnotationController;
import com.android.myannotations.retrofit.models.api.AnnotationResult;
import com.android.myannotations.retrofit.theards.IThreadResult;

import java.io.IOException;

public class AddActivity extends AppCompatActivity {

    private static final String TAG = "AdicionarActivity";
    private EditText edtTitulo;
    private EditText edtMessage;
    private LinearLayout llSalvar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Adicionar Anotação");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        edtTitulo = (EditText) findViewById(R.id.edtTitulo);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        llSalvar = (LinearLayout) findViewById(R.id.llSalvar);

        llSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnnotation();
            }
        });
    }

    private void setAnnotation(){
        String titulo = edtTitulo.getText().toString();
        String message = edtMessage.getText().toString();
        if (titulo.equals("")){
            Toast.makeText(getApplicationContext(), "O título é obrigatório!", Toast.LENGTH_SHORT).show();
        }else if (message.equals("")){
            Toast.makeText(getApplicationContext(), "A mensagem é obrigatório!", Toast.LENGTH_SHORT).show();
        }else {
            SetAnnotationThread setAnnotationThread = new SetAnnotationThread(titulo, message);
            setAnnotationThread.setOnResult(new IThreadResult<AnnotationResult>() {
                @Override
                public void onResult(AnnotationResult routerResult) {
                if(routerResult != null){
                    Log.w(TAG, "Salvar Annotation => " + routerResult.getMsg());
                }
                }
            });
            setAnnotationThread.execute();
        }

    }

    @Override
    public void onBackPressed() {
        Log.w(TAG, "onBackPressed: " );
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.w(TAG, "onOptionsItemSelected: " +  item.getItemId() );
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class SetAnnotationThread extends AsyncTask<Void, Void, AnnotationResult> {

        private IThreadResult<AnnotationResult> iThreadResult;
        private String titulo;
        private String message;
        private Dialog dialog;

        private SetAnnotationThread(String titulo, String message) {
            this.titulo = titulo;
            this.message = message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.setContentView(R.layout.loading_custom);
            dialog.show();
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
            dialog.dismiss();
            if (iThreadResult != null)
                iThreadResult.onResult(routerResult);
            edtTitulo.setText("");
            edtMessage.setText("");
            finish();
        }

        public void setOnResult(IThreadResult<AnnotationResult> iThreadResult) {
            this.iThreadResult = iThreadResult;
        }
    }
}
