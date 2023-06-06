package com.myapps.demo.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author liaoqg
 * @brief description
 * @date 2023-04-14
 */
public class Api {
    private OkHttpClient client;

    public void chatCreate(boolean usedP , String url , String urlProxy , String port , String key , String text ,String json, Callback callback) {
//        String json ;
        Proxy proxy;
        if (usedP){
             proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(
                    urlProxy, Integer.parseInt(port)));
        }else {
             proxy = null;
        }
        RequestBody requestBodyJson =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBodyJson)
                .addHeader("content-type", "application/json")
                .addHeader("Authorization", "Bearer " + key)
                .build();
        client = new OkHttpClient.Builder()
                .connectTimeout(10 , TimeUnit.SECONDS)
                .readTimeout(50 , TimeUnit.SECONDS)
                .proxy(proxy)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void chatImage(boolean usedP , String url , String urlProxy , String port , String key , String text , Callback callback){
        String json = "{\"prompt\": \"" + text + "\", \"n\": 1, \"size\" : \"512x512\"}";
//        String json = "{\"prompt\":\"A cute baby sea otter\",\"n\":1,\"size\":\"1024x1024\"}";
        Proxy proxy;
        if (usedP){
            proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(
                    urlProxy, Integer.parseInt(port)));
        }else {
            proxy = null;
        }
        RequestBody requestBodyJson =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBodyJson)
                .addHeader("content-type", "application/json")
                .addHeader("Authorization", key)
                .build();
        client = new OkHttpClient.Builder()
                .connectTimeout(10 , TimeUnit.SECONDS)
                .readTimeout(50 , TimeUnit.SECONDS)
                .proxy(proxy)
                .build();
        client.newCall(request).enqueue(callback);
    }


}
