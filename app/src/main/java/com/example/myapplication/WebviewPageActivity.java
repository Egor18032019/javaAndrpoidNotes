package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Вебвью как открытие внутренней страницы
 * а далее
 *
 */
public class WebviewPageActivity extends AppCompatActivity {
    String url = "https://developer.android.com/develop/ui/views/layout/webapps/webview#java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

    }
}
