package com.intkhabahmed.popularmoviesstage1.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
        if (intent != null && intent.getExtras() != null) {
            Movie movie = (Movie) intent.getExtras().getSerializable(getString(R.string.movie_object));
            if (movie != null) {
                populateUi(movie);
            }
        }
    }

    private void populateUi(Movie movie) {
        setTitle(movie.getOriginalTitle());
        Glide.with(this).asDrawable().apply(new RequestOptions().placeholder(R.drawable.placeholder_movieimage)
                .error(R.drawable.error_placeholder))
                .load(AppConstants.BASE_IMAGE_URL_500 + movie.getBackdropPath()).into(mDetailBinding.backdropIv);
        Glide.with(this).asDrawable().apply(new RequestOptions().placeholder(R.drawable.placeholder_movieimage)
                .error(R.drawable.error_placeholder))
                .load(AppConstants.BASE_IMAGE_URL_185 + movie.getPosterUrl()).into(mDetailBinding.movieThumbnailIv);
        mDetailBinding.originalTitleTv.setText(movie.getOriginalTitle());
        mDetailBinding.originalTitleTv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom));
        mDetailBinding.plotSynopsisTv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom));
        mDetailBinding.plotSynopsisTv.setText(movie.getOverview());
        Drawable textDrawable = TextDrawable.builder()
                .buildRound(String.valueOf(movie.getVoteAverage()), ContextCompat.getColor(this, R.color.colorPrimaryDark));
        mDetailBinding.voteAverageIv.setImageDrawable(textDrawable);
        mDetailBinding.voteAverageIv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_from_right));
        mDetailBinding.releaseDateTv.setText(DateUtils.getFormattedDate(movie.getReleaseDate()));
        mDetailBinding.releaseDateTv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_from_right));
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
