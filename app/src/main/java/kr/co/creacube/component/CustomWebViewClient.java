package kr.co.creacube.component;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class CustomWebViewClient extends WebViewClient {
    private WebView webView;
    private RelativeLayout layout_loading;

    public CustomWebViewClient(WebView webView, RelativeLayout layout_loading) {
        this.webView = webView;
        this.layout_loading = layout_loading;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

        webView.setVisibility(View.VISIBLE);
        layout_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        layout_loading.setVisibility(View.GONE);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        webView.setVisibility(View.GONE);
        layout_loading.setVisibility(View.GONE);
    }
}
