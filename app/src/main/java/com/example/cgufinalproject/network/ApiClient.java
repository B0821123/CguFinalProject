package com.example.cgufinalproject.network;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClients() {
        if (retrofit == null) {

            // Initialize Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://fcm.googleapis.com/fcm/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}