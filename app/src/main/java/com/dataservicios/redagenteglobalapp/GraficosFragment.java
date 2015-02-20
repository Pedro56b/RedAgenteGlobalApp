package com.dataservicios.redagenteglobalapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * Created by usuario on 29/11/2014.
 */
public class GraficosFragment  extends Fragment {
    private WebView mWebview;
    final Activity activity = getActivity();
    public GraficosFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_grafico, container, false);
        WebView webView = (WebView) v.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

//        webView.setWebChromeClient(new WebChromeClient() {
//            public void onProgressChanged(WebView view, int progress)
//            {
//                activity.setTitle("Loading...");
//                activity.setProgress(progress * 100);
//
//                if(progress == 100)
//                    activity.setTitle(R.string.app_name);
//            }
//        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                // Handle the error
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });

        // webView.loadUrl("http://developer.android.com");
        webView.loadUrl("http://www.fionny.net/be/");
        return v;
    }
}