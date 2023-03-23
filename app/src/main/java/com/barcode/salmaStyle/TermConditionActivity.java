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

public class TermConditionActivity extends Originator {

    TextView toolbar_text;
    ConstraintLayout back;
    String value = "", url = "";
    WebView webView;
    SharedPrefClass sharedPrefClass;
    private FragmentActivity context;
    Locale locale2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_condition);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar_text = findViewById(R.id.toolbar_text);
        back = findViewById(R.id.cons_tool_img);
        context = TermConditionActivity.this;
        sharedPrefClass = new SharedPrefClass(this);
        webView = findViewById(R.id.webview_term_condition);
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportMultipleWindows(false);
        webView.getSettings().setSupportZoom(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);

        if (sharedPrefClass.getString(SharedPrefClass.LANGUAGE).equals("ps")) {
            toolbar_text.setText("شرایط و ضوابط");

            locale2 = new Locale("ps");
            Locale.setDefault(locale2);
            Configuration config2 = new Configuration();
            config2.locale = locale2;
            TermConditionActivity.this.getBaseContext().getResources().updateConfiguration(config2, TermConditionActivity.this.getBaseContext().getResources().getDisplayMetrics());
            url = "http://69.49.235.253:8000/en/farsi/termsandconditions/";
            webView.loadUrl(url);
            Logger.line(Logger.LoggerMessage.product_image_url, true, Logger.getThread(Thread.currentThread().getStackTrace()[2]),
                    "View_On_Website_Activity",
                    url);
        } else {
            toolbar_text.setText("Term and Condition");

            locale2 = new Locale("en");
            Locale.setDefault(locale2);
            Configuration config2 = new Configuration();
            config2.locale = locale2;
            TermConditionActivity.this.getBaseContext().getResources().updateConfiguration(config2, TermConditionActivity.this.getBaseContext().getResources().getDisplayMetrics());


            url = "http://69.49.235.253:8000/en/termsandconditions/";
            webView.loadUrl(url);
            Logger.line(Logger.LoggerMessage.product_image_url, true, Logger.getThread(Thread.currentThread().getStackTrace()[2]),
                    "View_On_Website_Activity",
                    url);
        }

        backListener();


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
                //  CustomProgressbar.hideProgressBar();
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
                //   CustomProgressbar.hideProgressBar();
            }
            Logger.line(Logger.LoggerMessage.product_image_url, true, Logger.getThread(Thread.currentThread().getStackTrace()[2]),
                    "View_On_Website_Activity",
                    request.getUrl());
        }
    }
}