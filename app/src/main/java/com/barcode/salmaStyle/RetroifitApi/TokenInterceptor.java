package com.barcode.salmaStyle.RetroifitApi;

import android.content.SharedPreferences;

import com.barcode.salmaStyle.MainActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    SharedPreferences sharedPreferences;

    @Override
    public Response intercept(Chain chain) throws IOException {

        //rewrite the request to add bearer token
        Request newRequest=chain.request().newBuilder()
                .header("Authorization","Bearer "+ MainActivity.token)
                .build();

        return chain.proceed(newRequest);
    }

}
