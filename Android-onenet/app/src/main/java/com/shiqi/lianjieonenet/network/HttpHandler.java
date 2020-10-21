package com.shiqi.lianjieonenet.network;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public enum HttpHandler {
    INSTANCE();

    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    public static final Headers API_HEADERS = new Headers.Builder()
            .add("api-key", "LoEEarS4lSeRRLeuwtlK19arUlY=")
            .add("Content-Type","application/json")
            .build();
    public static final String API_URL = "http://api.heclouds.com/devices/629063002/datapoints?datastream_id=%s&limit=5";
    public static final Request API_TEMPERATURE = INSTANCE.get(String.format(API_URL, "3303_0_5700"));
    public static final Request API_HUMIDITY = INSTANCE.get(String.format(API_URL, "3304_0_5700"));

    private OkHttpClient client = new OkHttpClient();

    public Request get(String url) {
        return new Request.Builder()
                .url(url)
                .headers(API_HEADERS)
                .build();
    }

    public Request post(String url, RequestBody body) {
        return new Request.Builder()
                .url(url)
                .headers(API_HEADERS)
                .post(body)
                .build();
    }

    public Response getResponse(Request request) throws IOException {
        return client.newCall(request).execute();
    }

}
