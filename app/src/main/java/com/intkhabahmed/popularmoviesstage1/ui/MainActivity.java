package com.intkhabahmed.popularmoviesstage1.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.intkhabahmed.popularmoviesstage1.R;
import com.intkhabahmed.popularmoviesstage1.adapter.MoviesAdapter;
import com.intkhabahmed.popularmoviesstage1.databinding.ActivityMainBinding;
import com.intkhabahmed.popularmoviesstage1.model.MovieResult;
import com.intkhabahmed.popularmoviesstage1.utils.AppConstants;
import com.intkhabahmed.popularmoviesstage1.viewmodels.MoviesViewModel;
import com.intkhabahmed.popularmoviesstage1.viewmodels.MoviesViewModelFactory;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnItemClick{

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
                outRect.top = 10;
                if(parent.getChildLayoutPosition(view) % 2 != 0) {
                    outRect.left = 10;
                }
            }
        });
        mMoviesAdapter = new MoviesAdapter(this, this);
        recyclerView.setAdapter(mMoviesAdapter);
        setupViewModel();
    }

    private void setupViewModel() {
        MoviesViewModelFactory factory = new MoviesViewModelFactory("top", AppConstants.API_KEY);
        MoviesViewModel moviesViewModel = ViewModelProviders.of(this, factory).get(MoviesViewModel.class);
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
    public void onClick(int position) {

    }
}
