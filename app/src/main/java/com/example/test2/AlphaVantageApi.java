package com.example.test2;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AlphaVantageApi {
    @GET("query?function=CURRENCY_EXCHANGE_RATE&from_currency=USD&to_currency=INR&apikey=9YM4KU27V9RUG02Y")
    Call<Rate> getPosts();
}
