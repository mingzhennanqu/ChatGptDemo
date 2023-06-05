package com.myapps.demo.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import yalantis.com.sidemenu.sample.R;


/**
 * @author liaoqg
 * @brief description
 * @date 2023-04-21
 */
public class SetDialog extends Dialog {

    TextView textView;

    LinearLayout linearLayout;

    LinearLayout linearLayoutEdit;

    RadioGroup radioGroupUsedP;

    RadioGroup radioGroupProxy;

    RadioButton radioButtonY;

    RadioButton radioButtonN;

    RadioButton radioButtonHttp;

    RadioButton radioButtonSocks5;

    EditText editTextIp;

    EditText editTextPort;

    Button button;

    int radioGroupIdUsedP;

    int radioGroupIdProxy;


    //获取SharedPreferences对象
    SharedPreferences sharedPreferences = getContext().getSharedPreferences("user",Context.MODE_PRIVATE);
    //获取Editor对象的引用
    SharedPreferences.Editor editor = sharedPreferences.edit();

    public SetDialog(@NonNull @NotNull Context context) {
        super(context);
    }

    public SetDialog(@NonNull @NotNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SetDialog(@NonNull @NotNull Context context, boolean cancelable, @Nullable @org.jetbrains.annotations.Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
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

        radioGroupUsedP = findViewById(R.id.usedP);

        radioGroupProxy = findViewById(R.id.proxy);

//
        radioButtonY = findViewById(R.id.proxy_y);
        radioButtonN = findViewById(R.id.proxy_n);

        radioButtonHttp = findViewById(R.id.proxy_http);
        radioButtonSocks5 = findViewById(R.id.proxy_socks5);

        linearLayout = findViewById(R.id.proxy_set);
        linearLayoutEdit = findViewById(R.id.linear_edit);

        editTextIp = findViewById(R.id.proxy_ip);
        editTextPort = findViewById(R.id.proxy_port);

        button = findViewById(R.id.proxy_sub);


        radioGroupIdUsedP = radioGroupUsedP.getCheckedRadioButtonId();
        radioGroupIdProxy = radioGroupProxy.getCheckedRadioButtonId();

    }
    private void initEvent() {

        radioGroupUsedP.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.proxy_n:
                        editor.putInt("UsedP" , R.id.proxy_n);
                        editor.putBoolean("radioGroupUsedP" , false);
                        linearLayout.setVisibility(View.GONE);
                        break;
                    case R.id.proxy_y:
                        editor.putInt("UsedP" , R.id.proxy_y);
                        editor.putBoolean("radioGroupUsedP" , true);
                        linearLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        radioGroupProxy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.proxy_http:
                        editor.putInt("proxy" , R.id.proxy_http);
//                        editor.putString("type" , String.valueOf(Proxy.Type.HTTP));
                        linearLayoutEdit.setVisibility(View.VISIBLE);
                        break;
                    case R.id.proxy_socks5:
                        editor.putInt("proxy" , R.id.proxy_socks5);
//                        editor.putString("type" , String.valueOf(Proxy.Type.SOCKS));
                        linearLayoutEdit.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                editor.putString("type", String.valueOf(Constants.type));
                editor.putString("editTextIp" , editTextIp.getText().toString());
                editor.putString("editTextPort" , editTextPort.getText().toString());
                // 提交数据
                editor.commit();

                dismiss();
            }
        });

    }

    private void initData() {

        editTextIp.setText(sharedPreferences.getString("editTextIp" , ""));
        editTextPort.setText(sharedPreferences.getString("editTextPort" , ""));


        if (radioGroupIdUsedP != -1){
            radioGroupUsedP.check(sharedPreferences.getInt("UsedP" , -1));
            if (radioButtonY.isChecked()){
                linearLayout.setVisibility(View.VISIBLE);

            }
        }
        radioGroupProxy.check(sharedPreferences.getInt("proxy" , -1));
        linearLayoutEdit.setVisibility(View.VISIBLE);


    }


}
