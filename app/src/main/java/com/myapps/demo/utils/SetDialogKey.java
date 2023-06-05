package com.myapps.demo.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;

import yalantis.com.sidemenu.sample.R;


/**
 * @author liaoqg
 * @brief description
 * @date 2023-04-21
 */
public class SetDialogKey extends Dialog {

    EditText editText;

    Button button;

    //获取SharedPreferences对象
    SharedPreferences sharedPreferences = getContext().getSharedPreferences("user",Context.MODE_PRIVATE);
    //获取Editor对象的引用
    SharedPreferences.Editor editor = sharedPreferences.edit();

    public SetDialogKey(@NonNull @NotNull Context context) {
        super(context);
    }

    public SetDialogKey(@NonNull @NotNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SetDialogKey(@NonNull @NotNull Context context, boolean cancelable, @Nullable @org.jetbrains.annotations.Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_key);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
    }
    private void initView() {
        editText = findViewById(R.id.edit_key);

        button = findViewById(R.id.key_sub);


    }
    private void initEvent() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String key = editText.getText().toString().trim();
                if (!key.isEmpty()) {
                    editor.putString("Key" , "Bearer " + key);
                    Log.d("TAG", "onClick: key");
                    editor.commit();
                }

                dismiss();
            }
        });

    }

    private void initData() {

        editText.setText(sharedPreferences.getString("Key" , ""));



    }


}
