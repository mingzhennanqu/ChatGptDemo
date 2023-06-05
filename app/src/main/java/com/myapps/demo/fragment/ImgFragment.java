package com.myapps.demo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.myapps.demo.bean.ChatCompletionChunk2;
import com.myapps.demo.bean.ChatImg;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import yalantis.com.myapps.interfaces.ScreenShotable;
import yalantis.com.sidemenu.sample.R;

import com.myapps.demo.utils.Api;
import com.myapps.demo.utils.Constants;

public class ImgFragment extends Fragment implements ScreenShotable, View.OnClickListener {


    @Inject
    Api api;
    ChatImg chatImg;
    ChatCompletionChunk2 chatCompletionChunk2;


    private OkHttpClient client;
    private EditText editText;
    private Button button;
    private LinearLayout mLinearLayout;
    private LinearLayout mLinearLayoutAdd;
    private View layoutAdd;
    private String message;

    private TextView textView;

    private ScrollView scrollView;

    private ImageView imageView;

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
//                    sendMsg(msg.obj.toString());
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    break;

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_img, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editText = getView().findViewById(R.id.edit_text);
        button = getView().findViewById(R.id.send_button);
        mLinearLayout = getView().findViewById(R.id.linear_layout);
        scrollView = getView().findViewById(R.id.scrollView);

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

    @Override
    public void onClick(View v) {
        String message = editText.getText().toString().trim();
        if (!message.isEmpty()) {
//            chatResp(message);
            chatImg(message);
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
                    textView = new TextView(getContext());
                    textView.setPadding(0, 0, 0, 20);
                    textView.setText(text);
//                    if (textView.getParent() != null) {
//                        ((ViewGroup)textView.getParent()).removeView(textView);
//                    }
                    mLinearLayout.addView(textView);
                    button.setEnabled(false);
                    button.setText("别急");
                }
            }
        });
    }

        //获取图像
    public void chatImg(String text){
        sendMsg("   Me : " + text);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Api api = new Api();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                api.chatImage(sharedPreferences.getBoolean("radioGroupUsedP" , false) ,
                        Constants.URL_IMAGE,
                        sharedPreferences.getString("editTextIp" , null),
                        sharedPreferences.getString("editTextPort" , null),
                        sharedPreferences.getString("Key" , Constants.KEY),
                        text, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d("onFailure", "onResponse: 请求失败" );
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String respStr = response.body().string();
                        Log.d("respStr", "onResponse: " + respStr);
                        Gson gson = new Gson();
                        chatImg = gson.fromJson(respStr , ChatImg.class);
//                        Log.d("chatImgUrl", "onResponse: " + chatImg.getData().get(0).getUrl());
                        ImageView imageView = new ImageView(getContext());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLinearLayout.addView(imageView);
                                Glide.with(imageView).load(chatImg.getData().get(0).getUrl()).into(imageView);

                                Message message = Message.obtain();
                                message.what = 1;
                                message.obj = "发送";
                                handler.sendMessage(message);
                            }
                        });
                    }
                });
            }
        }).start();
    }
}