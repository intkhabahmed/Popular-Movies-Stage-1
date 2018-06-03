package com.intkhabahmed.popularmoviesstage1.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.intkhabahmed.popularmoviesstage1.R;
import com.intkhabahmed.popularmoviesstage1.model.Movie;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter {

    private OnItemClick mOnItemClick;
    private List<Movie> movies;

    public MoviesAdapter(OnItemClick onItemClick) {
        this.mOnItemClick = onItemClick;
    }

    interface OnItemClick {
        void onClick(int position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_layout, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (movies == null) {
            return 0;
        }
        return movies.size();
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView movieThumbnail;

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

    public void setMovies (List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}
