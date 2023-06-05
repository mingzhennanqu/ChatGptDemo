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
//        if (true){
//            json = "{\"model\": \"gpt-3.5-turbo\", " +
//                    "\"messages\": [{\"role\": \"user\", \"content\":  \"今天几号，星期几\"} ," +
//                    "{\"role\": \"assistant\", \"content\":  \"抱歉，我无法回答这个问题，因为我不知道当前的日期和星期几。我只是一个计算机程序，没有自己的感知能力。\"} ," +
//                    "{\"role\": \"user\", \"content\":  \"" + text + "\"} ] , " +
//                    "\"stream\" : true}";

//            json = "{\"model\": \"gpt-3.5-turbo\", " +
//                    "\"messages\": [{\"role\": \"user\", \"content\":  \"今天几号，星期几\"} ," +
//                    "{\"role\": \"assistant\", \"content\":  \"抱歉，我无法回答这个问题，因为我不知道当前的日期和星期几。我只是一个计算机程序，没有自己的感知能力。\"}] , " +
//                    "\"stream\" : true}";



//            try {
//                // 将json字符串转化为json对象
//                JSONObject jsonObject = new JSONObject(json);
//                // 获取messages字段的json数组对象
//                JSONArray messagesArray = jsonObject.getJSONArray("messages");
//                // 创建一个新的json对象，表示新的消息
//                Constants.newMessage  = new JSONObject();
//                Constants.newMessage.put("role", "user");
//                Constants.newMessage.put("content", text);
//                // 将新的消息对象添加到messages数组中
//                messagesArray.put(Constants.newMessage);
//                // 更新json对象中的messages字段
//                jsonObject.put("messages", messagesArray);
//                // 将json对象转化为字符串
//                json = jsonObject.toString();
//                // 发送更新后的json数据
//                // ...
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

//            try {
//                // 将json字符串转化为json对象
//                JSONObject jsonObject = new JSONObject(json);
//                // 获取messages字段的json数组对象
//                JSONArray messagesArray = jsonObject.getJSONArray("messages");
//                // 创建一个新的json对象，表示新的消息
//                JSONObject newMessage = new JSONObject();
//                newMessage.put("role", "user");
//                newMessage.put("content", text);
//                // 将新的消息对象添加到messages数组中
//                messagesArray.put(newMessage);
//                // 更新json对象中的messages字段
//                jsonObject.put("messages", messagesArray);
//                // 将json对象转化为字符串
//                json = jsonObject.toString();
//                // 发送更新后的json数据
//                // ...
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

//         }else {
//            json = "{\"model\": \"gpt-3.5-turbo\", " +
//                    "\"messages\": [{\"role\": \"user\", \"content\":  \"" + text + "\"}] , " +
//                    "\"stream\" : true}";
//        }
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
