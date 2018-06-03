package com.intkhabahmed.popularmoviesstage1.networking;

import com.intkhabahmed.popularmoviesstage1.model.Movie;
import com.intkhabahmed.popularmoviesstage1.model.MovieResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebService {

    @GET("movie/{filter}")
    Call<MovieResult> getMoviesByPreference(@Path("filter") String filter, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(@Path("movie_id") int movieId);
}
