package com.example.dict;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.IOException;

public class DictActivity extends Activity {

    private String configFile = "DictApp/config";

    private String searchUrl = "http://dict.kekenet.com/en/%s";

    private WebView webView;

    private SdcardTool sdcardTool;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        sdcardTool = new SdcardTool(this);
        setContentView(R.layout.dict_main);
        try {
            String config = sdcardTool.readFromSD(configFile);
            if (config.trim().length() == 0) {
                sdcardTool.savaFileToSD(configFile, searchUrl);
            } else {
                searchUrl = config.trim();
            }
        } catch (IOException e) {
            Toast.makeText(getApplication(), "sdcard permission denied", Toast.LENGTH_SHORT).show();
        }
        dictIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.EXTRA_PROCESS_TEXT.equals(intent.getAction())) {
            dictIntent();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void dictIntent() {
        initWebView();
        String text = getIntent().getStringExtra(Intent.EXTRA_PROCESS_TEXT);
        String url = "https://baidu.com";
        if (text != null) {
            text = text.trim();
            if (text.length() < 1024) {
                url = String.format(searchUrl, text);
            }
        }
        webView.loadUrl(url);
    }


    private WebView initWebView() {
        if (webView != null) {
            return webView;
        }
        webView = findViewById(R.id.web_view);
        // https://www.jianshu.com/p/4564be81a108
        WebSettings webSettings = webView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        //支持插件
        //     webSettings.setPluginsEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        // webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //webSettings.setMinimumFontSize(22);
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("WebView", "onPageStarted");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("WebView", "onPageFinished");
            }

            // 链接跳转都会走这个方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("WebView", "url：" + url);
                view.loadUrl(url);// 强制在当前 WebView 中加载 url
                return true;
            }
        });
        return webView;
    }
}
