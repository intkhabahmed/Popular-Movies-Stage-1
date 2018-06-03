package com.intkhabahmed.popularmoviesstage1.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.intkhabahmed.popularmoviesstage1.model.MovieResult;
import com.intkhabahmed.popularmoviesstage1.networking.ApiClient;
import com.intkhabahmed.popularmoviesstage1.networking.WebService;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class MovieRepository {
    @Inject
    public MovieRepository() {
    }

    public LiveData<MovieResult> getMovies (String filter, String apiKey) {
        final MutableLiveData<MovieResult> result = new MutableLiveData<>();
        ApiClient.getInstance().create(WebService.class)
                .getMoviesByPreference(filter, apiKey)
                .enqueue(new Callback<MovieResult>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieResult> call, @NonNull Response<MovieResult> response) {
                        Log.v(MovieRepository.class.getName(), "response code: "+response.raw().request().url());
                        result.setValue(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieResult> call, @NonNull Throwable t) {
                        call.cancel();
                        Log.v(MovieRepository.class.getName(), "error: " + t.getMessage());
                    }
                });
        return result;
    }
}
