package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.*;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Вебвью как браузер
 */
public class WebviewActivity extends AppCompatActivity {

    public static final int FILECHOOSER_RESULTCODE = 100;
    private WebView myWebView;
    private int clicks;
    private String url = "https://demoqa.com/automation-practice-form";
    ProgressBar progressBar; // прогресс бар загрузки ?
    private ValueCallback fileCallback;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        myWebView = findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(url);
        myWebView.setWebChromeClient(new MyWebChrome());


    }

    @Override
    public void onBackPressed() {
        System.out.println("onBackPressed WebviewActivity " + clicks);
        if (clicks == 11) {
            super.onBackPressed();
        } else {
            clicks++;
        }
    }

    private class MyWebChrome extends WebChromeClient {
        @Override
        public boolean onShowFileChooser(WebView v, ValueCallback back, WebChromeClient.FileChooserParams param) {
            //assign value
            fileCallback = back;
            //launch chooser activity
            //createIntent will automatically set intent parameters for choosing file
            startActivityForResult(param.createIntent(), FILECHOOSER_RESULTCODE);
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != FILECHOOSER_RESULTCODE || fileCallback == null) {
            return;
        }
        //send result, then it's done
        fileCallback.onReceiveValue(
                WebChromeClient.FileChooserParams.parseResult(resultCode, data)
        );
        fileCallback = null;
    }

}


