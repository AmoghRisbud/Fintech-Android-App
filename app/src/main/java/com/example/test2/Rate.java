package com.example.test2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rate {
    @SerializedName("Realtime Currency Exchange Rate")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Post{" +
                "data=" + data +
                '}';
    }
}
