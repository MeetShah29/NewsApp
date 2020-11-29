package com.example.newsapp;

import com.example.newsapp.Model.Headlines;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("everything")
    Call<Headlines> getHeadlines(
      @Query("q") String query,
      @Query("apikey") String apiKey
    );
}
