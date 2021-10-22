package kr.co.creacube.component;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.co.creacube.ConnectActivity;
import kr.co.creacube.R;
import kr.co.creacube.adapter.WifiListAdapter;

import static kr.co.creacube.util.CommonUtil.showToast;

public class WifiListPopup extends Activity implements View.OnClickListener {

    RelativeLayout layout_loading;
    RecyclerView rvWifiList;
    EditText etPw;
    Button btnClose, btnConfirm;

    LinearLayoutManager layoutManager;
    WifiListAdapter wifiListAdapter;

    private WifiManager wifiManager;

    ArrayList<String> wifiList = new ArrayList<>();

    final static int WIFI_GET_SUCCESS = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wifi_list_popup);

        layout_loading = findViewById(R.id.layout_loading);
        rvWifiList = findViewById(R.id.rv_wifi_list);
        etPw = findViewById(R.id.et_pw);
        btnClose = findViewById(R.id.btn_popup_close);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnClose.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvWifiList.setLayoutManager(layoutManager);
        wifiListAdapter = new WifiListAdapter();
        rvWifiList.setAdapter(wifiListAdapter);

        layout_loading.setVisibility(View.VISIBLE);

        wifiScan();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_popup_close:
                finish();
                break;

            case R.id.btn_confirm:
                if (wifiListAdapter.getSelectedPosition() != -1) {
                    String pw = String.valueOf(etPw.getText());

                    if (pw.isEmpty()) {
                        showToast(R.string.toast_input_wifi_pw);
                        return;
                    }

                    Intent intent = new Intent();
                    String ssid = wifiListAdapter.getWifiList().get(wifiListAdapter.getSelectedPosition());
                    intent.putExtra("ssid", ssid);
                    intent.putExtra("pw", pw);
                    setResult(WIFI_GET_SUCCESS, intent);
                    finish();
                } else {
                    showToast(R.string.toast_select_wifi);
                }
                break;
        }
    }

    public void wifiScan() {
        wifiManager = (WifiManager)this.getApplicationContext().getSystemService(WIFI_SERVICE);

        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);

                if (success) {
                    scanSuccess();
                } else {
                    scanFailure();
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        this.registerReceiver(wifiScanReceiver, intentFilter);

        boolean success = wifiManager.startScan();
        if (!success) {
            scanFailure();
        }
    }

    private void scanSuccess() {
        List<ScanResult> results = wifiManager.getScanResults();

        StringBuffer st = new StringBuffer();
        for(ScanResult r : results) {
            wifiList.add(r.SSID);
        }
        wifiListAdapter.setWifiList(wifiList);
        wifiListAdapter.notifyDataSetChanged();

        layout_loading.setVisibility(View.GONE);
    }

    private void scanFailure() {
        showToast(R.string.toast_error_wifi);
    }
}
