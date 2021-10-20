package kr.co.creacube.util;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import kr.co.creacube.MainActivity;

public class AndroidBridge {
    private MainActivity act;
    private WebView webView;

    public AndroidBridge(MainActivity act, WebView webView) {
        this.act = act;
        this.webView = webView;
    }

    // Bridge function
    @JavascriptInterface
    public void cubeconnect() {
        act.requestToConnect();
    }

}
