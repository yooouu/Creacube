package kr.co.creacube;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import kr.co.creacube.component.CustomWebViewClient;
import kr.co.creacube.util.AndroidBridge;
import kr.co.creacube.util.HTTPUtil;

import static kr.co.creacube.util.CommonUtil.finishApp;

public class MainActivity extends AppCompatActivity {

    private final String URL = HTTPUtil.ip;
    private WebView webView;
    private RelativeLayout layout_loading;

    public static boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isRunning = true;

        webView = findViewById(R.id.webview);
        layout_loading = findViewById(R.id.layout_loading);

        webView.addJavascriptInterface(new AndroidBridge(this, webView), "AndroidBridge");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setTextZoom(100);
        String userAgent = webView.getSettings().getUserAgentString();
//        webView.getSettings().setUserAgentString(userAgent + "menovel_and");
        webView.setWebViewClient(new CustomWebViewClient(webView, layout_loading));
//        webView.setWebChromeClient(new CustomChromeClient(this));
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        String url = getIntent().getStringExtra("url");
        if (url != null && !url.equals("")) {
            url = (!url.startsWith("https") ? "https://" : "") + url;
            webView.loadUrl(url);
        } else {
            webView.loadUrl(URL);
        }
    }

    @Override
    public void onBackPressed() {
        if (layout_loading.getVisibility() == View.GONE) {
            String url = webView.getUrl();
            if (!webView.canGoBack() || url.endsWith("p1_login.html")) {
                finishApp(MainActivity.this);
            } else {
                webView.goBack();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    // 권한 체크
    public boolean requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 권한을 획득하지 않았다면
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.BLUETOOTH,
                                Manifest.permission.BLUETOOTH_ADMIN,
                        },
                        123);
                return false;
            }
        }
        return true;
    }

    // 큐브 연결 리스트 화면 이동 요청
    public void requestToConnect() {
        Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
        startActivity(intent);
    }
}