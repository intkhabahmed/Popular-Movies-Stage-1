package com.intkhabahmed.popularmoviesstage1.ui;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.intkhabahmed.popularmoviesstage1.R;
import com.intkhabahmed.popularmoviesstage1.adapter.MoviesAdapter;
import com.intkhabahmed.popularmoviesstage1.databinding.ActivityMainBinding;
import com.intkhabahmed.popularmoviesstage1.model.Movie;
import com.intkhabahmed.popularmoviesstage1.model.MovieResult;
import com.intkhabahmed.popularmoviesstage1.utils.AppConstants;
import com.intkhabahmed.popularmoviesstage1.utils.NetworkUtils;
import com.intkhabahmed.popularmoviesstage1.viewmodels.MoviesViewModel;
import com.intkhabahmed.popularmoviesstage1.viewmodels.MoviesViewModelFactory;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnItemClick {

    private ActivityMainBinding mMainBinding;
    private MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setupUI();
        performLoading(false);
    }

    private void performLoading(final boolean isCriteriaChanged) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final boolean isConnected = NetworkUtils.getConnectivityStatus(MainActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isConnected) {
                            mMoviesAdapter.setMovies(null);
                            mMainBinding.noConnectionLl.setVisibility(View.VISIBLE);
                            mMainBinding.loadingPb.setVisibility(View.INVISIBLE);
                            return;
                        }
                        setupViewModel(isCriteriaChanged);
                    }
                });
            }
        });
    }

    private void setupUI() {
        RecyclerView recyclerView = mMainBinding.moviesRv;
        final int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 16;
                int position = parent.getChildLayoutPosition(view);
                if (spanCount == 2) {
                    if (position % 2 != 0) {
                        outRect.left = 16;
                    }
                } else {
                    if (position % 2 == 0) {
                        outRect.left = 16;
                        outRect.right = 16;
                    }
                    if (position % 4 == 0) {
                        outRect.left = 0;
                    }
                }
            }
        });
        mMoviesAdapter = new MoviesAdapter(this, this);
        recyclerView.setAdapter(mMoviesAdapter);
        mMainBinding.retryIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLoading(true);
            }
        });
    }

    private void setupViewModel(boolean isMovieCriteriaChanged) {
        mMainBinding.noConnectionLl.setVisibility(View.INVISIBLE);
        mMainBinding.loadingPb.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortCriteria = sharedPreferences.getString(getString(R.string.sort_criteria), AppConstants.POPULAR_MOVIES);
        MoviesViewModelFactory factory = new MoviesViewModelFactory(sortCriteria, getString(R.string.api_key));
        MoviesViewModel moviesViewModel = ViewModelProviders.of(this, factory).get(MoviesViewModel.class);
        if (isMovieCriteriaChanged) {
            moviesViewModel.loadFromNetwork(sortCriteria, getString(R.string.api_key));
        }
        moviesViewModel.getResults().observe(this, new Observer<MovieResult>() {
            @Override
            public void onChanged(@Nullable MovieResult movieResult) {
                mMainBinding.loadingPb.setVisibility(View.INVISIBLE);
                if (movieResult != null) {
                    mMoviesAdapter.setMovies(movieResult.getMovies());
                } else {
                    mMoviesAdapter.setMovies(null);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        int subMenuOrder = PreferenceManager.getDefaultSharedPreferences(this)
                .getInt(getString(R.string.sort_criteria_id), 1);
        menu.getItem(0).getSubMenu().getItem(subMenuOrder - 1).setChecked(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.popular_movies:
            case R.id.top_rated_movies:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                    refreshMovies(getSortCriteria(item.getOrder()), item.getOrder());
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshMovies(String sortCriteria, int order) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(getString(R.string.sort_criteria), sortCriteria);
        editor.putInt(getString(R.string.sort_criteria_id), order);
        editor.apply();
        performLoading(true);
    }

    private String getSortCriteria(int order) {
        switch (order) {
            case 1:
                return AppConstants.POPULAR_MOVIES;
            case 2:
                return AppConstants.TOP_RATED_MOVIES;
            default:
                return null;
        }
    }

    @Override
    public void onClick(View view, Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.movie_object), movie);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT > 21) {
            Bundle sharedBundle = ActivityOptions.makeSceneTransitionAnimation(this, view,
                    view.getTransitionName()).toBundle();
            startActivity(intent, sharedBundle);
            return;
        }
        startActivity(intent);
    }
}
