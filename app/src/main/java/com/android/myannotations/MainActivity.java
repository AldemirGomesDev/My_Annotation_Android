package com.android.myannotations;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.myannotations.retrofit.controllers.AnnotationController;
import com.android.myannotations.retrofit.models.Annotation;
import com.android.myannotations.retrofit.models.api.AnnotationResult;
import com.android.myannotations.retrofit.theards.GetAnnotationsThread;
import com.android.myannotations.retrofit.theards.IThreadResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "VoltarActivity";
    private TextView tvName;
    private RecyclerView mRecyclerView;

    private LineAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        tvName = (TextView) findViewById(R.id.tvName);
        mRecyclerView = findViewById(R.id.recycler_view_layour_recycler);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra("MESSAGE", "Olá sou uma informação passada de outra activity");
                startActivityForResult(intent, 1);// Activity é iniciada com requestCode

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        setupRecycler();

        getAnnotation();
    }

    private void setupRecycler() {
        try {

            UserModel userModel = null;
            ArrayList<UserModel> list = new ArrayList<>();
            String[] nameArray = {"Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb", "Ice Cream Sandwich","JellyBean", "Kitkat", "Lollipop", "Marshmallow"};
            String[] versionArray = {"01/03/2019", "21/08/2019", "14/06/2019", "06/03/2018", "09/12/2019", "26/03/2017", "01/03/2019", "01/03/2019", "01/03/2019", "5.0-5.1.1","6.0-6.0.1"};

            for (int i = 0; i < nameArray.length; i++) {
                userModel = new UserModel();
                userModel.setName(nameArray[i]);
                userModel.setDate(versionArray[i]);
                list.add(userModel);
                Log.w(TAG, "setupRecycler: " + nameArray[i] );
            }

            // Configurando o gerenciador de layout para ser uma lista.
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);

            // Adiciona o adapter que irá anexar os objetos à lista.
            // Está sendo criado com lista vazia, pois será preenchida posteriormente.
            mAdapter = new LineAdapter(list);
            mRecyclerView.setAdapter(mAdapter);

            // Configurando um dividr entre linhas, para uma melhor visualização.
            mRecyclerView.addItemDecoration(
                    new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            mRecyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.w(TAG, "onClick Aqui " );
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        tvName.setText(" " + requestCode + " - " + resultCode);
        if(requestCode == 1 && resultCode == RESULT_OK)
        {
            String message = data.getStringExtra("MESSAGE");
            tvName.setText(message);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_new){
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            intent.putExtra("MESSAGE", "Olá sou uma informação passada de outra activity");
            startActivityForResult(intent, 1);// Activity é iniciada com requestCode
        }

        return super.onOptionsItemSelected(item);
    }

    private void getAnnotation(){
        GetAnnotationsThread getRoutersThread = new GetAnnotationsThread(1);
        getRoutersThread.setOnResult(new IThreadResult<AnnotationResult>() {
            @Override
            public void onResult(AnnotationResult routerResult) {

            }
        });
        getRoutersThread.execute();

//        AnnotationController annotationController = new AnnotationController();
//
//        Annotation annotation;
//
//        try {
//            annotation =  annotationController.getAnnotations(1).getAnnotation();
//            Log.w("RouterSaver", "Annotation: " + annotation.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
