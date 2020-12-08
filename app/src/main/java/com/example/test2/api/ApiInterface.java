package com.example.test2.api;

import com.example.test2.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("top-headlines")
    Call<News> getNews(

            //@Query("image") String image,
            @Query("country") String country,
            @Query("category") String category,
            @Query("apiKey") String apiKey
    );
}
