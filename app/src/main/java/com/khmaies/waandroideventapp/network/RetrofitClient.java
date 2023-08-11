package com.khmaies.waandroideventapp.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Khmaies Hassen on 09,August,2023
 */
public class RetrofitClient {

    private static final String BASE_URL = "https://18da-41-226-20-67.ngrok-free.app/"; // Update with your local API URL
    static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .build();
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();

    public static ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
}
