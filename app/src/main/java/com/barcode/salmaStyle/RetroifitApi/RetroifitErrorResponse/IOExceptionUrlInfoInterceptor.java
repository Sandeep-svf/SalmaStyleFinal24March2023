package com.barcode.salmaStyle.RetroifitApi.RetroifitErrorResponse;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class IOExceptionUrlInfoInterceptor implements Interceptor {

    @Override
    public Response intercept(final Chain chain) throws IOException {
        final Response response;

        try {
            response = chain.proceed(chain.request());
        } catch (IOException e) {
            throw new HttpIOException(chain.request(), e);
        }
        return response;
    }
}