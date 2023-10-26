package com.example.myapplication.Listeners;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {
    private final String url = "https://developer.android.com";
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (url.equals(request.getUrl().getHost())) {
            // Ограничение на домен. Если страницы не с этого домена, то грузить не будет.
            return false;
        }
        // В противном случае выберете действие.
        // Открывается браузер в этом случае.
//        Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
//        view.getContext().startActivity(intent);
        view.loadUrl(url);
        return true;
    }


}
