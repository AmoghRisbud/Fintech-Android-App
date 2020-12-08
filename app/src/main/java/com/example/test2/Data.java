package com.example.test2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("5. Exchange Rate")
    @Expose
    private String rate;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Data{" +
                "rate='" + rate + '\'' +
                '}';
    }
}
