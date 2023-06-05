package com.myapps.demo.utils;

import org.json.JSONObject;

/**
 * @author liaoqg
 * @brief description
 * @date 2023-04-14
 */
public class Constants {

    // Chat3.5 聊天接口
    public static final String URL_CREATE = "https://api.openai.com/v1/chat/completions";

    // Chat3.5 图片接口
    public static final String URL_IMAGE = "https://api.openai.com/v1/images/generations";

    // 是否使用代理
//    public static Boolean usedP = false;

    // 代理ip
//    public static  String PROXY = "";
//
    //端口
//    public static  int PORT = ;

    // ChatGpt_Key
    public static  String KEY = "";

    //是否开启上下文
    public static boolean IsTOKEN = false;

    //上下文信息
    public static JSONObject newMessage;

}
