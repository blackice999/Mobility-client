package com.project.mobility.view.activities.web;

import android.os.Bundle;
import android.webkit.WebView;

import com.project.mobility.R;
import com.project.mobility.di.injection.Injection;
import com.project.mobility.view.activities.web.client.BaseWebViewClient;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class WebViewActivity extends AppCompatActivity {

    public static final String KEY_URL = "url";


    @BindView(R.id.webview_notification_promotions) WebView webView;

    @Inject BaseWebViewClient baseWebViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        ButterKnife.bind(this);
        Injection.inject(this);

        webView.getSettings().setBuiltInZoomControls(true);
        String url = getIntent().getExtras().getString(KEY_URL);

        if (url != null) {
            webView.setWebViewClient(baseWebViewClient);
            webView.loadUrl(url);
            Timber.d("URL is %s", url);
        }
    }
}
