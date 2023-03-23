package com.barcode.salmaStyle.RetroifitApi.RetroifitErrorResponse;

import com.barcode.salmaStyle.RetroifitApi.TokenInterceptor;
import com.barcode.salmaStyle.RetroifitApi.UrlClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClientNoToken {

    private static Retrofit retrofit = null;

    TokenInterceptor interceptor=new TokenInterceptor();

  /*  OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build();*/

    public static Retrofit getClient() {
        if (retrofit == null) {
              HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
             interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            // TokenInterceptor interceptor=new TokenInterceptor();
          //  UserPassInterceptor userPassInterceptor=new UserPassInterceptor();
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .build();


            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
          /*  OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.
                    addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            Response response = chain.proceed(request);
                            if (response.code() == 403) {
                                handleForbiddenResponse();
                            }
                            return response;
                        }
                    });*/

            retrofit = new Retrofit.Builder()
                    .baseUrl(UrlClass.BaseUrl)
                    // .baseUrl(BuildConfig.API_URL)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(LenientGsonConverterFactory.create(gson))
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}
