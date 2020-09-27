package com.shiqi.lianjieonenet.util;

import com.google.gson.Gson;
        import com.google.gson.GsonBuilder;

public class Json {
    public static Gson gson;

    static {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }
}
