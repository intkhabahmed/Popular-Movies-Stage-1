package com.intkhabahmed.popularmoviesstage1.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.intkhabahmed.popularmoviesstage1.R;
import com.intkhabahmed.popularmoviesstage1.databinding.ActivityDetailBinding;
import com.intkhabahmed.popularmoviesstage1.model.Movie;
import com.intkhabahmed.popularmoviesstage1.utils.AppConstants;
import com.intkhabahmed.popularmoviesstage1.utils.DateUtils;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding mDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        Intent intent = getIntent();
        if(intent != null && intent.getExtras() != null) {
            Movie movie = (Movie) intent.getExtras().getSerializable(getString(R.string.movie_object));
            if(movie != null) {
                populateUi(movie);
            }
        }
    }

    private void populateUi(Movie movie) {
        setTitle(movie.getOriginalTitle());
        String imageUrl = AppConstants.BASE_IMAGE_URL_500 + movie.getBackdropPath();
        Glide.with(this).asDrawable().load(imageUrl).into(mDetailBinding.backdropIv);
        mDetailBinding.originalTitleTv.setText(movie.getOriginalTitle());
        mDetailBinding.plotSynopsisTv.setText(movie.getOverview());
        mDetailBinding.voteAverageTv.setText(String.format("%s/10", movie.getVoteAverage()));
        mDetailBinding.releaseDateTv.setText(DateUtils.getFormattedDate(movie.getReleaseDate()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
