package com.myapps.demo.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jetbrains.annotations.NotNull;

import yalantis.com.myapps.interfaces.ScreenShotable;
import yalantis.com.sidemenu.sample.R;

import com.myapps.demo.utils.Constants;


public class HomeFragment extends Fragment implements ScreenShotable, SwipeRefreshLayout.OnRefreshListener {

    private boolean isBackPressed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void takeScreenShot() {

    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

    @Override
    public void onRefresh() {

    }


    //内部类 自定义WebViewClient
    private class MyWebViewClient extends WebViewClient {
        private String startUrl;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            startUrl = url;
        }



        //设置网页内后续打开的url，为app内打开
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            if (url == null) return false;

            try{
                //因为webview只能识别http, https这样的协议，
                // 像一些微信(weixin://)、去哪儿(qunaraphone://)，他们自定义的协议webView是无法识别的，
                // 因此就会出现：ERR_UNKNOWN_URL_SCHEME这样的错误。
                if(!url.startsWith("http://") && !url.startsWith("https://")){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            }catch (Exception e) {//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)

                //没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                return true;
            }
//            view.loadUrl(url);
            if(startUrl!=null && startUrl.equals(url))
            {
                view.loadUrl(url);
            }
            else
            {
                //交给系统处理
                return super.shouldOverrideUrlLoading(view, url);
            }
            if (isBackPressed) {
                // 如果是返回事件触发的页面跳转，不做处理
                isBackPressed = false;
            } else {
                // 手动处理页面跳转
                view.loadUrl(url);
            }


            //如果返回值为true，表示需要自己处理URL，不需要WebView加载；
            // 如果返回值为false，表示由WebView加载URL。
            return true;
        }

    }

}