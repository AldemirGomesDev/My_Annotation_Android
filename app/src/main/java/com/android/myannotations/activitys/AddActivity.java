package com.android.myannotations.activitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.myannotations.R;
import com.android.myannotations.retrofit.controllers.AnnotationController;
import com.android.myannotations.retrofit.models.api.AnnotationResult;
import com.android.myannotations.retrofit.theards.IThreadResult;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    private static final String TAG = "AdicionarActivity";
    private EditText edtTitulo;
    private EditText edtMessage;
    private TextView tvDate;
    private LinearLayout llSalvar;
    private Context context;
    private View layoutToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Adicionar Anotação");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        startUIVariable();
        getDate();
    }

    private void startUIVariable() {
        LayoutInflater inflater = LayoutInflater.from(this);
        layoutToast = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        edtTitulo = (EditText) findViewById(R.id.edtTitulo);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        tvDate = (TextView) findViewById(R.id.tvDate);
        llSalvar = (LinearLayout) findViewById(R.id.llSalvar);

        llSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnnotation();
            }
        });
    }

    //metodo para salvar a anotação
    private void setAnnotation(){
        String titulo = edtTitulo.getText().toString();
        String message = edtMessage.getText().toString();
        if (titulo.equals("")){
            customToast("O título é obrigatório!");
        }else if (message.equals("")){
            customToast("A mensagem é obrigatória!");
        }else {
            //chamando a classe AsyncTasck para fazer a comunicação com a API
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
    //botão para voltar para tela anterior
    @Override
    public void onBackPressed() {
        Log.w(TAG, "onBackPressed: " );
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //finaliza a activity a atual e volta pra anterior
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    //Metodo para pegar a data atual
    private void getDate(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        tvDate.setText(formataData.format(data));
    }
    //Classe AsyncTask
    @SuppressLint("StaticFieldLeak")
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
            //criando e exibindo o loading enquanto espera o retorno da API
            dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.setContentView(R.layout.loading_custom);
            dialog.show();
        }

        @Override
        protected AnnotationResult doInBackground(Void... voids) {
            //fazendo chamada a API
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
            //Retorno da API, se for diferente de null retorna para quem chamou
            if (iThreadResult != null)
                iThreadResult.onResult(routerResult);
            edtTitulo.setText("");
            edtMessage.setText("");
            finish();
        }

        private void setOnResult(IThreadResult<AnnotationResult> iThreadResult) {
            this.iThreadResult = iThreadResult;
        }
    }
    //metodo para criar um Toast personalizado
    private void customToast(String message) {
        ImageView image = (ImageView) layoutToast.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_alert);
        TextView text = (TextView) layoutToast.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layoutToast);
        toast.show();
    }
}
