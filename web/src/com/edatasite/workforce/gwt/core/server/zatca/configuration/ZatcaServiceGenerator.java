package com.edatasite.workforce.gwt.core.server.zatca.configuration;

import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class ZatcaServiceGenerator {

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    private static final Retrofit.Builder builder
            = new Retrofit.Builder()
//            .baseUrl("http://localhost:8089/api/")
            .baseUrl(SpringPropertiesUtil.getProperty("kpi.zatca.url"))
            .addConverterFactory(GsonConverterFactory.create(gson));
    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit retrofit = builder.build();

    public static  <S> S createService(Class<S> serviceClass) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().clear();
        httpClient.addInterceptor(logging);
        builder.client(httpClient
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build());
        retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}
