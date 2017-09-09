package com.piper.hackernews;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by parthamurmu on 09/09/17.
 */

@SuppressLint("JavascriptInterface")
public class ArticleDetailFragment  extends Fragment {
    WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_detail, container, false);
        webView = view.findViewById(R.id.webview);
        webView.addJavascriptInterface(getActivity(), "android");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        showArticle();
        return view;
    }

    private void showArticle(){
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl("http://www.google.com");
                return false;
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl("http://www.google.com");
                return false;
            }});

//        Handler handler = new Handler();
//
//        //mWebViewProgress.post(new Runnable() {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                mWebViewProgress.loadUrl(mUrl);
//                mWebViewProgress.addJavascriptInterface(MainActivity.this, "android");
//                orderFragment.layout.removeView(mWebViewProgress);
//                orderFragment.layout.addView(mWebViewProgress);
//            }
//        });
    }
}