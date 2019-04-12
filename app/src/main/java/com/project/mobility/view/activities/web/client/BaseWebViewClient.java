package com.project.mobility.view.activities.web.client;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import javax.inject.Inject;

import timber.log.Timber;

public class BaseWebViewClient extends WebViewClient {

    @Inject
    public BaseWebViewClient() {
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        view.loadUrl(request.getUrl().toString());
        return true;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        Timber.i("WebView error " + error);
        view.stopLoading();

        if (view.canGoBack()) {
            view.goBack();
        }
    }
}
