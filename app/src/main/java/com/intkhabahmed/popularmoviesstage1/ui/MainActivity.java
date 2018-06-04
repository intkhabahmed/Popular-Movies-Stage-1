package com.intkhabahmed.popularmoviesstage1.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
import com.intkhabahmed.popularmoviesstage1.viewmodels.MoviesViewModel;
import com.intkhabahmed.popularmoviesstage1.viewmodels.MoviesViewModelFactory;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnItemClick {

    private ActivityMainBinding mMainBinding;
    private MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        RecyclerView recyclerView = mMainBinding.moviesRv;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 10;
                if (parent.getChildLayoutPosition(view) % 2 != 0) {
                    outRect.left = 10;
                }
            }
        });
        mMoviesAdapter = new MoviesAdapter(this, this);
        recyclerView.setAdapter(mMoviesAdapter);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortCriteria = sharedPreferences.getString(getString(R.string.sort_criteria), AppConstants.POPULAR_MOVIES);
        setupViewModel(sortCriteria, false);
    }

    private void setupViewModel(String sortCriteria, boolean isMovieCriteriaChanged) {
        mMainBinding.loadingPb.setVisibility(View.VISIBLE);
        MoviesViewModelFactory factory = new MoviesViewModelFactory(sortCriteria, AppConstants.API_KEY);
        MoviesViewModel moviesViewModel = ViewModelProviders.of(this, factory).get(MoviesViewModel.class);
        if (isMovieCriteriaChanged) {
            moviesViewModel.loadFromNetwork(sortCriteria, AppConstants.API_KEY);
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
        setupViewModel(sortCriteria, true);
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
    public void onClick(int position, Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.movie_object), movie);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
