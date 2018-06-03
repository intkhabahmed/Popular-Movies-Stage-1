package com.intkhabahmed.popularmoviesstage1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.intkhabahmed.popularmoviesstage1.R;
import com.intkhabahmed.popularmoviesstage1.model.Movie;
import com.intkhabahmed.popularmoviesstage1.utils.AppConstants;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private OnItemClick mOnItemClick;
    private List<Movie> movies;
    private Context mContext;

    public MoviesAdapter(Context context, OnItemClick onItemClick) {
        this.mOnItemClick = onItemClick;
        mContext = context;
    }

    public interface OnItemClick {
        void onClick(int position);
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_item_layout, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        String imageRelativePath = movies.get(position).getPosterUrl();
        String imageFullPath = AppConstants.BASE_IMAGE_URL + imageRelativePath;
        Glide.with(mContext).asDrawable().load(imageFullPath).into(holder.movieThumbnail);
    }

    @Override
    public int getItemCount() {
        if (movies == null) {
            return 0;
        }
        return movies.size();
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView movieThumbnail;

        MoviesViewHolder(View itemView) {
            super(itemView);
            movieThumbnail = itemView.findViewById(R.id.movie_thumbnail_iv);
            movieThumbnail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemClick.onClick(getAdapterPosition());
        }
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}
