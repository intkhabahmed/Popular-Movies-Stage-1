package com.intkhabahmed.popularmoviesstage1.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.intkhabahmed.popularmoviesstage1.database.MovieRepository;
import com.intkhabahmed.popularmoviesstage1.model.MovieResult;

public class MoviesViewModel extends ViewModel {
    private LiveData<MovieResult> results;
    MovieRepository movieRepository = new MovieRepository();

    MoviesViewModel(String filter, String apiKey) {
        if (results != null) {
            return;
        }
        results = movieRepository.getMovies(filter, apiKey);
    }

    public LiveData<MovieResult> getResults() {
        return results;
    }
}
