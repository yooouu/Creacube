package kr.co.creacube.util;

import android.util.Log;
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
    public void CubeConnect(String type) {
        Log.e("aaa", type);
        act.requestToConnect(type);
    }

}
