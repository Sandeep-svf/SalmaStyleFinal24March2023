package com.barcode.salmaStyle.RetroifitApi.RetroifitErrorResponse;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Request;

public class HttpIOException extends IOException {

    HttpIOException(final Request request, final IOException e) {
        super(String.format(Locale.US, "IOException during http request: %s", request), e);
    }
}