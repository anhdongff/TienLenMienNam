package com.gamebai.tienlenmiennam.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String API_BASE_URL = "https://api-2i3u7alegq-as.a.run.app";
    private static final String API_FOR_ZALO_PAY_BASE_URL = "https://apiforzalopay-2i3u7alegq-as.a.run.app";
    private static Retrofit retrofit;
    private static Retrofit retrofitForZaloPay;
    public static Retrofit getAPIInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static Retrofit getAPIForZaloPayInstance() {
        if (retrofitForZaloPay == null) {
            retrofitForZaloPay = new Retrofit.Builder()
                    .baseUrl(API_FOR_ZALO_PAY_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitForZaloPay;
    }
}
