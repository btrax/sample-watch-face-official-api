package com.btrax.newwatch;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GuideActivity extends Activity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.setWebViewClient(new GuideWebViewClient());
        setWebSettings();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mWebView.loadUrl(AppConsts.GUIDE_URL);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void setWebSettings() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private class GuideWebViewClient extends WebViewClient {

        /**
         * open only GUIDE PAGE in webvidw
         * webviewではガイドページのみを開く。その他はブラウザ起動
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.equals(AppConsts.GUIDE_URL)) {
                return false;
            }

            // launch browser
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}