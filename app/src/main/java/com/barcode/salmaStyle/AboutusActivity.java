package com.barcode.salmaStyle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.utol.Logger;
import com.barcode.salmaStyle.utol.Originator;
import com.barcode.salmaStyle.utol.SharedPrefClass;

import java.util.Locale;

public class AboutusActivity extends Originator {

    TextView toolbar_text;
    ConstraintLayout back;
    WebView webView;
    String value = "", url = "";
    SharedPrefClass sharedPrefClass;
    // public ProgressDialog progressDialog;
    private FragmentActivity context;
    Locale locale2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar_text = findViewById(R.id.toolbar_text);
        back = findViewById(R.id.cons_tool_img);
        context = AboutusActivity.this;
        sharedPrefClass = new SharedPrefClass(this);
        backListener();

        webView = findViewById(R.id.webview_comman);
        webView.setWebViewClient(new AboutusActivity.MyBrowser());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportMultipleWindows(false);
        webView.getSettings().setSupportZoom(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);

        if (sharedPrefClass.getString(SharedPrefClass.LANGUAGE).equals("ps")) {
            toolbar_text.setText("درباره ما");
            locale2 = new Locale("ps");
            Locale.setDefault(locale2);
            Configuration config2 = new Configuration();
            config2.locale = locale2;
            AboutusActivity.this.getBaseContext().getResources().updateConfiguration(config2, AboutusActivity.this.getBaseContext().getResources().getDisplayMetrics());
            url = "http://69.49.235.253:8000/en/farsi/aboutus/";
            webView.loadUrl(url);
            Logger.line(Logger.LoggerMessage.product_image_url, true, Logger.getThread(Thread.currentThread().getStackTrace()[2]),
                    "View_On_Website_Activity",
                    url);
        } else {

            toolbar_text.setText("About Us");
            locale2 = new Locale("en");
            Locale.setDefault(locale2);
            Configuration config2 = new Configuration();
            config2.locale = locale2;
            AboutusActivity.this.getBaseContext().getResources().updateConfiguration(config2, AboutusActivity.this.getBaseContext().getResources().getDisplayMetrics());

            url = "http://69.49.235.253:8000/en/aboutus/";
            webView.loadUrl(url);
            Logger.line(Logger.LoggerMessage.product_image_url, true, Logger.getThread(Thread.currentThread().getStackTrace()[2]),
                    "View_On_Website_Activity",
                    url);
        }
    }

    private void backListener() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
            if (!((FragmentActivity) context).isFinishing()) {
              /*  if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }*/
                //  CustomProgressbar.showProgressBar(context, false);
            }

            Logger.line(Logger.LoggerMessage.product_image_url, true, Logger.getThread(Thread.currentThread().getStackTrace()[2]),
                    "View_On_Website_Activity",
                    url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO hide your progress image
            super.onPageFinished(view, url);

            if (!((FragmentActivity) context).isFinishing()) {
               /* if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/
                //   CustomProgressbar.hideProgressBar();
            }

            Logger.analyser(context, Logger.LoggerMessage.PAYMENT_ACTIVITY, true, Logger.getThread(Thread.currentThread().getStackTrace()[2]),
                    url
            );

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (!((FragmentActivity) context).isFinishing()) {
               /* if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/
                //  CustomProgressbar.hideProgressBar();
            }
            Logger.line(Logger.LoggerMessage.product_image_url, true, Logger.getThread(Thread.currentThread().getStackTrace()[2]),
                    "View_On_Website_Activity",
                    request.getUrl());
        }
    }
}