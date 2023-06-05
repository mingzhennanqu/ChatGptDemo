package com.myapps.demo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import yalantis.com.myapps.interfaces.ScreenShotable;
import yalantis.com.sidemenu.sample.R;


import com.myapps.demo.bean.ChatCompletionChunk2;
import com.myapps.demo.utils.Api;
import com.myapps.demo.utils.Constants;

public class ChatFragment extends Fragment implements ScreenShotable, View.OnClickListener {

    @Inject
    Api api;

    ChatCompletionChunk2 chatCompletionChunk2;

    private OkHttpClient client;
    private EditText editText;
    private Button button;
    private LinearLayout mLinearLayout;
    private LinearLayout mLinearLayoutAdd;
    private FrameLayout frameLayout;


    StringBuffer buffer;

    String json;


    private TextView textView;

    private ScrollView scrollView;

    private ImageView imageView;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //button按钮文字及状态
                    button.setEnabled(true);
                    button.setText((String)msg.obj);

                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    break;
                case 2:
                    //response返回拼接
                    textView.setText(msg.obj.toString());

                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    break;

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_chat, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editText = getView().findViewById(R.id.edit_text);
        button = getView().findViewById(R.id.send_button);
        mLinearLayout = getView().findViewById(R.id.linear_layout);
        scrollView = getView().findViewById(R.id.scrollView);
        frameLayout = getView().findViewById(R.id.frag_add);

        mLinearLayoutAdd = LayoutInflater.from(getContext()).inflate(R.layout.chat_textview , null).findViewById(R.id.chat_layout);

        setChatInf("你好，我是Ai小助手，需要帮助吗？" , R.mipmap.chat_img , R.color.c_f2f3f5);

        json = "{\"model\": \"gpt-3.5-turbo\", " +
                "\"messages\": [] , " +
                "\"stream\" : true}";

        button = getView().findViewById(R.id.send_button);
        button.setOnClickListener(this);
    }

    @Override
    public void takeScreenShot() {

    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        //设置背景透明度
//        frameLayout.getBackground().setAlpha(128);
        String message = editText.getText().toString().trim();
        if (!message.isEmpty()) {

            //收起软键盘
            // 这将获取当前Fragment所依附的Activity的InputMethodManager实例，
            // 然后使用getCurrentFocus()方法获取当前具有焦点的View。
            // 如果有任何View具有焦点，它将使用hideSoftInputFromWindow()方法将软键盘收起。如果没有任何View具有焦点，软键盘将保持不变。
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            chatResp(message);
        }else {
            Toast.makeText(getContext(), "请输入内容", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint({"ResourceAsColor", "MissingInflatedId"})
    public void sendMsg(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!text.isEmpty()) {
                    editText.getText().clear();
                    setChatInf(text , R.mipmap.user_img , R.color.c_f7f8fa);

                    setToken(1 , text);

                    button.setEnabled(false);
                    button.setText("别急");
                }
            }
        });
    }

    public void chatResp(final String text){
        sendMsg(text);
        new Thread(new Runnable() {
            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setChatInf("正在输入中..." , R.mipmap.chat_img , R.color.c_f2f3f5);

                    }
                });
                Api api = new Api();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

                api.chatCreate(sharedPreferences.getBoolean("radioGroupUsedP" , false) ,
                        Constants.URL_CREATE,
                        sharedPreferences.getString("editTextIp" , null),
                        sharedPreferences.getString("editTextPort" , null),
                        sharedPreferences.getString("Key" , Constants.KEY),
                        text,
                        json,
                        new Callback() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        //流式输出
                        buffer = new StringBuffer();
                        Gson gson = new Gson();
                        // 获取response输入流
                        InputStream inputStream = response.body().byteStream();
                        // 读取响应数据
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                // 处理每一行数据
                                Log.d("line", "onResponse: " + line);
                                //判断是否返回了数据，去除response前data关键字，不然解析不了
                                if (line.length() > 6) {
                                    Log.d("line.substring", "onResponse: " + line.substring(6));
                                    try {
                                        chatCompletionChunk2 = gson.fromJson(line.substring(5) , ChatCompletionChunk2.class);
                                        Log.d("getContent", "onResponse: " + chatCompletionChunk2.getChoices().get(0).getDelta().getContent());
                                        if (chatCompletionChunk2.getChoices().get(0).getDelta().getContent() != null){
                                            addNewlineAfterPeriod(chatCompletionChunk2.getChoices().get(0).getDelta().getContent());
                                            buffer.append(chatCompletionChunk2.getChoices().get(0).getDelta().getContent());

                                            setMessage(2 , buffer);
                                        }
                                        if (chatCompletionChunk2.getChoices().get(0).getFinishReason() != null) {
                                            break;
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        buffer.append("请求有误，请检查key或稍后重试，当然，如果你上下文足够长也会出现，请自行找寻原因，嗯，我懒得写 /doge");

                                        setMessage(2 , buffer);
                                        setToken(2 , buffer.toString());
                                        break;
                                    }
                                }
                            }

                            setMessage(1 , "发送");

                            Log.d("buffer", "onResponse: " + buffer.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.d("onFailure", "onFailure: 请求失败" );

                        setMessage(2 , "请求超时，请检查网络并重试");

                        setMessage(1 , "发送");

                    }

                });

            }
        }).start();
    }


    /**
     * @author liaoqg
     * @date ${YEAR}-${MONTH}-${DAY}
     * 描述
     *      对字符串进行处理
     */

    public String addNewlineAfterPeriod(String str) {
        StringBuilder sb = new StringBuilder();
        boolean periodFound = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '.' || c == '。') {
                periodFound = true;
                sb.append(c);
                sb.append('\n');
            } else if (c == '\n') {
                continue;
            } else {
                sb.append(c);
            }
        }
        if (!periodFound) {
            return str;
        }
        return sb.toString();
    }

    /**
     * @author liaoqg
     * @date ${YEAR}-${MONTH}-${DAY}
     * 描述
     *      设置头像和聊天框样式
     */

    public void setChatInf(String text , int img , int color){
        LinearLayout mLinearLayoutAdd = LayoutInflater.from(getContext()).inflate(R.layout.chat_textview , null).findViewById(R.id.chat_layout);

        mLinearLayoutAdd.setBackground(ContextCompat.getDrawable(getContext(), color));
        imageView = mLinearLayoutAdd.findViewById(R.id.chat_iv);
        imageView.setBackground(getResources().getDrawable(img));
        textView = mLinearLayoutAdd.findViewById(R.id.chat_tv);
        textView.setText(text);
        if (mLinearLayoutAdd.getParent() != null) {
            ((ViewGroup)mLinearLayoutAdd.getParent()).removeView(mLinearLayoutAdd);
        }

        mLinearLayout.addView(mLinearLayoutAdd);
    }

    /**
     * @author liaoqg
     * @date ${YEAR}-${MONTH}-${DAY}
     * 描述
     *      handle
     */
    public void setMessage(int what , Object object){
        Message message = Message.obtain();
        message.what = what;
        message.obj = object;
        handler.sendMessage(message);
    }

    /**
     * @author liaoqg
     * @date ${YEAR}-${MONTH}-${DAY}
     * 描述
     *      将上下文信息保存
     *          测试出，只需要用户信息，不需要将chat的回答添加到请求中，也可以保持gpt上下文连贯
     */

    public void setToken(int status , String text){
        try {
            if (Constants.IsTOKEN){
                // 将json字符串转化为json对象
                JSONObject jsonObject = new JSONObject(json);
                // 获取messages字段的json数组对象
                JSONArray messagesArray = jsonObject.getJSONArray("messages");
                if (status == 1){
                    Constants.newMessage  = new JSONObject();
                    Constants.newMessage.put("role", "user");
                    Constants.newMessage.put("content", text);
                }
//            else if (status == 2){
//                Constants.newMessage  = new JSONObject();
//                Constants.newMessage.put("role", "assistant");
//                Constants.newMessage.put("content", text);
//            }
                // 将新的消息对象添加到messages数组中
                messagesArray.put(Constants.newMessage);
                // 更新json对象中的messages字段
                jsonObject.put("messages", messagesArray);
                // 将json对象转化为字符串
                json = jsonObject.toString();
            }else {
                json = "{\"model\": \"gpt-3.5-turbo\", " +
                    "\"messages\": [{\"role\": \"user\", \"content\":  \"" + text + "\"}] , " +
                    "\"stream\" : true}";
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("TAG", "setToken: " + json);
    }

}