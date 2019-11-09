package com.android.myannotations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
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
            final int[] visibleItemCount = new int[1];
            final int[] totalVisible = new int[1];
            final int[] pastVisibleItems = new int[1];
//            mRecyclerView.addItemDecoration(
//                    new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                        if (!loading) {
                            getAnnotation(context);
                            loading = true;

                        }
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {
                        //loading = true;
                    } else {
                        //loading = false;
                    }


                    if (dy > 0) //check for scroll down
                    {
                        visibleItemCount[0] = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        pastVisibleItems[0] = layoutManager.findFirstVisibleItemPosition();
                        totalVisible[0] = visibleItemCount[0] + pastVisibleItems[0];
                        Log.w(TAG, "setupRecycler: 1 => " + totalVisible[0] );

                        if (totalVisible[0] >= totalItemCount) {
                            Log.w(TAG, "onScrolled: " + visibleItemCount[0] + " - " + totalItemCount + " - " + pastVisibleItems[0]);
                            loading = false;
//                                if (mAdapter.countOfShowing < mAdapter.allChallenges.size()) {
//                                    Log.e("...", "Last Item Wow !");
//                                    mAdapter.increaseCountOfShowing();
//                                    mAdapter.notifyDataSetChanged();
//                                }
                            //loading = true;
                            //Do pagination.. i.e. fetch new data

                        }
                    } else {
                        loading = true;
                    }
                }
            });
            int visibleItemCounts = layoutManager.getChildCount();
            int pastVisibleItemss = layoutManager.findFirstVisibleItemPosition();
            int totalItemCounts = visibleItemCounts + pastVisibleItemss;

            Log.w(TAG, "SoothScrollToPosition: " + totalItemCounts );
            mRecyclerView.smoothScrollToPosition(totalItemCounts);

        } catch (Exception e) {
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
        if (data != null) {
            if (requestCode == 1 && resultCode == RESULT_OK) {

            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_new) {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            intent.putExtra("MESSAGE", "Olá sou uma informação passada de outra activity");
            startActivityForResult(intent, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    public void getAnnotation(Activity context) {
        Log.e(TAG, "getAnnotation: ");
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
        Log.w("RouterSaver", "onPause ");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.w("RouterSaver", "onResume ");
        super.onResume();
        getAnnotation(context);
    }

    private void loading() {

    }

}
