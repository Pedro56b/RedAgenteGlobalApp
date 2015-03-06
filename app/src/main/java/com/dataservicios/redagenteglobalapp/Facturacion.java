package com.dataservicios.redagenteglobalapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/**
 * Created by usuario on 26/02/2015.
 */
public class Facturacion extends Activity {
    private WebView mWebview;
    final Activity MyActivity = this;
    String id_agente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transacciones);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        MyActivity.getActionBar().setTitle("Facturación");


        Bundle bundle = getIntent().getExtras();
        id_agente = bundle.getString("id");

        WebView webView = (WebView)  findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        final ProgressDialog progressDialog = new ProgressDialog(MyActivity);
        //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(true);

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
        webView.loadUrl("http://redagentesyglobalnet.com/admin/invoicesForAgent/"+id_agente);

        webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {

                progressDialog.show();
                // progressDialog.setProgress(0);
                // MyActivity.setProgress(progress * 1000);

                //progressDialog.incrementProgressBy(progress);

                //if (progress == 100 && progressDialog.isShowing())
                if (progress == 100 )
                    progressDialog.dismiss();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = getIntent().getExtras();
        String id_agente = bundle.getString("id");

        // getting values from selected ListItem
        String aid = id_agente;
        switch (item.getItemId()) {
            case android.R.id.home:
                // go to previous screen when app icon in action bar is clicked
                Intent intent = new Intent(this, AgenteDetailActivity.class);
                Bundle bolsa = new Bundle();
                bolsa.putString("id", aid);
                intent.putExtras(bolsa);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}