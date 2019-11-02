package com.android.myannotations;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.myannotations.adapter.AnnotationAdapter;
import com.android.myannotations.retrofit.controllers.AnnotationController;
import com.android.myannotations.retrofit.models.Annotation;
import com.android.myannotations.retrofit.models.api.AnnotationResult;
import com.android.myannotations.retrofit.theards.GetAnnotationThread;
import com.android.myannotations.retrofit.theards.GetAnnotationsThread;
import com.android.myannotations.retrofit.theards.IThreadResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RouterSaver";
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

        setSupportActionBar(toolbar);
        context = this;

        mRecyclerView = findViewById(R.id.recycler_view_layour_recycler);
        tv_not_result_found = findViewById(R.id.tv_not_result_found);
        no_results_found_layout = (LinearLayout) findViewById(R.id.no_results_found_layout);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra("MESSAGE", "Olá sou uma informação passada de outra activity");
                startActivityForResult(intent, 1);// Activity é iniciada com requestCode

            }
        });
    }

    private void setupRecycler() {
        try {
            layoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);
            mAdapter = new AnnotationAdapter(list, this);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addItemDecoration(
                    new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                        if(!loading){
                            getAnnotation(context);
                            loading = true;
                        }
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {
                        loading = true;
                        Log.w(TAG, "onScrolled: " + dy);
                    }else {
                        loading = false;
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if(requestCode == 1 && resultCode == RESULT_OK) {

            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_new){
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            intent.putExtra("MESSAGE", "Olá sou uma informação passada de outra activity");
            startActivityForResult(intent, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    public void getAnnotation(Activity context){
        Log.e(TAG, "getAnnotation: " );
        GetAnnotationsThread getRoutersThread = new GetAnnotationsThread(context);
        getRoutersThread.setOnResult(new IThreadResult<AnnotationResult>() {
            @Override
            public void onResult(AnnotationResult routerResult) {
                loading = false;
                if(routerResult != null ){
                    list = routerResult.getAnnotations();
                    if(list.size() > 0){
                        no_results_found_layout.setVisibility(View.GONE);
                        setupRecycler();
                    }else{
                        tv_not_result_found.setText(R.string.text_not_found);
                        no_results_found_layout.setVisibility(View.VISIBLE);
                    }
                }else{
                    tv_not_result_found.setText(R.string.text_not_found_service);
                    no_results_found_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        getRoutersThread.execute();
    }

    @Override
    protected void onPause() {
        Log.w("RouterSaver", "onPause ");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.w("RouterSaver", "onResume ");
        super.onResume();
        getAnnotation(context);
    }

    private void loading(){

    }

}
