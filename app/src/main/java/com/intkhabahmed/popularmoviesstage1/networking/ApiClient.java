package com.intkhabahmed.popularmoviesstage1.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.intkhabahmed.popularmoviesstage1.utils.AppConstants.BASE_URL;

public class ApiClient {
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
