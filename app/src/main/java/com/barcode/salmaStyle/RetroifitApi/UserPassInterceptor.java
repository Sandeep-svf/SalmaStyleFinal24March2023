package com.barcode.salmaStyle.RetroifitApi;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class UserPassInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String credential = Credentials.basic("mohitr786@gmail.com", "123");
        //rewrite the request to add bearer token
        Request newRequest=chain.request().newBuilder()
                .header("Authorization",credential)
                .build();

        return chain.proceed(newRequest);
    }
}
