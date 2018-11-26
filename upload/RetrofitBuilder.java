package com.xygame.hobby.home.upload;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xygame.hobby.http.RequestConstants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitBuilder {
    private static Retrofit retrofit;

    public static synchronized Retrofit buildRetrofit() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
            retrofit = new Retrofit.Builder().client(OkHttpManager.getInstance())
                    .baseUrl(RequestConstants.getApiUrl())
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
