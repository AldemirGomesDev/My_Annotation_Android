package com.android.myannotations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.myannotations.activitys.AddActivity;
import com.android.myannotations.adapter.AnnotationAdapter;
import com.android.myannotations.models.Annotation;
import com.android.myannotations.retrofit.models.api.AnnotationResult;
import com.android.myannotations.retrofit.theards.GetAnnotationsThread;
import com.android.myannotations.retrofit.theards.IThreadResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "testeunitatio";
    private RecyclerView mRecyclerView;
    private List<Annotation> list = new ArrayList<>();
    private Activity context;
    private AnnotationAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private LinearLayout no_results_found_layout;
    private TextView tv_not_result_found;
    boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        Log.w(TAG, "isConnection => " + isOnline());

        setSupportActionBar(toolbar);
        context = this;

        startUIVariable();
        getAnnotation(context);
    }

    private void startUIVariable() {
        mRecyclerView = findViewById(R.id.recycler_view_layour_recycler);
        tv_not_result_found = findViewById(R.id.tv_not_result_found);
        no_results_found_layout = (LinearLayout) findViewById(R.id.no_results_found_layout);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(getBaseContext(), R.color.colorPrimary)));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, 200);

            }
        });
    }
    //testar conexao
    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
    //adicionar o recyclerView
    private void setupRecycler() {
        try {
            layoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);
            mAdapter = new AnnotationAdapter(list, this);
            mRecyclerView.setAdapter(mAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Cria o menu da ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    //retorno da tela adicionar anotação
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            getAnnotation(context);
        }
    }
    //pega os clique nos itens do menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                getAnnotation(context);
                break;
            case R.id.action_settings:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //metodo que chama Classe AsyncTask e busca as anotações da API
    public void getAnnotation(Activity context) {
        GetAnnotationsThread getRoutersThread = new GetAnnotationsThread(context);
        getRoutersThread.setOnResult(new IThreadResult<AnnotationResult>() {
            @Override
            public void onResult(AnnotationResult routerResult) {
                loading = false;
                if (routerResult != null) {
                    list = routerResult.getAnnotations();
                    if (list.size() > 0) {
                        no_results_found_layout.setVisibility(View.GONE);
                        setupRecycler();
                    } else {
                        tv_not_result_found.setText(R.string.text_not_found);
                        no_results_found_layout.setVisibility(View.VISIBLE);
                    }
                } else {
                    tv_not_result_found.setText(R.string.text_not_found_service);
                    no_results_found_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        getRoutersThread.execute();
    }

    @Override
    protected void onPause() {
        Log.w(TAG, "onPause ");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.w(TAG, "onResume ");
        super.onResume();
    }
}
