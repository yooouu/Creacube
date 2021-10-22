package kr.co.creacube;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import kr.co.creacube.adapter.CubeListAdapter;
import kr.co.creacube.component.ConfirmDialog;
import kr.co.creacube.component.MessagePopup;
import kr.co.creacube.component.WifiListPopup;
import kr.co.creacube.util.CommonUtil;

import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
import static kr.co.creacube.util.CommonUtil.showToast;

public class ConnectActivity extends AppCompatActivity implements View.OnClickListener {

    private CubeListAdapter cubeListAdapter;
    private List<String> cubeList = new ArrayList<>();

    RecyclerView recyclerCube;
    String type = "S";  // S : 학생 , T : 선생님 , W : 없음

    // Bluetooth
    BluetoothAdapter bluetoothAdapter;
    Set<BluetoothDevice> pairedDevices;
    List<String> pairedDevicesList;

    Handler bluetoothHandler;
    ConnectedBluetoothThread connectedBluetoothThread;
    BluetoothDevice bluetoothDevice;
    BluetoothSocket bluetoothSocket;

    final static int BT_REQUEST_ENABLE = 1;
    final static int BT_MESSAGE_READ = 2;
    final static int BT_CONNECTING_STATUS = 3;
    final static UUID BT_UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");

    final static int WIFI_REQUEST_LIST = 1000;
    final static int WIFI_GET_SUCCESS = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        type = getIntent().getStringExtra("type");

        Button closeButton = findViewById(R.id.btn_close);
        Button wifiButton = findViewById(R.id.btn_wifi);
        recyclerCube = findViewById(R.id.rv_cube_list);

        closeButton.setOnClickListener(this);
        wifiButton.setOnClickListener(this);

        // Bluetooth
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothHandler = new Handler(){
            public void handleMessage(android.os.Message msg){
                if(msg.what == BT_MESSAGE_READ){
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.e("aaa", "receiveData : " + readMessage);
                }
            }
        };

        cubeListAdapter = new CubeListAdapter(ConnectActivity.this, cubeList);
        recyclerCube.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerCube.setAdapter(cubeListAdapter);

        bluetoothOn();
        listPairedDevices();
        searchDevice();
    }

    @Override
    protected void onResume() {
        super.onResume();

        cubeListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
            case R.id.btn_wifi:
                if (cubeList.size() == 0) {
                    final MessagePopup popup = new MessagePopup(this);
                    popup.setTitle(R.string.popup_power_on);
                    popup.setCloseListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.dismiss();
                        }
                    });
                    popup.show();
                } else {
                    //TODO ble connect and wifi setup
                    Intent intent = new Intent(ConnectActivity.this, WifiListPopup.class);
                    startActivityForResult(intent, WIFI_REQUEST_LIST);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case BT_REQUEST_ENABLE:
                if (resultCode == RESULT_OK) {
                    // 블루투스 활성화
                    listPairedDevices();
                } else if (resultCode == RESULT_CANCELED) {
                    // 블루투스 비활성화
                }
                break;

            case WIFI_REQUEST_LIST:
                if (resultCode == WIFI_GET_SUCCESS) {
                    String ssid = data.getStringExtra("ssid");
                    String pw = data.getStringExtra("pw");
                    Log.e("aaa", "ssid : " + ssid + " pw : " + pw);
                    //TODO wifi setup
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setDeviceList() {
        cubeListAdapter.setList(cubeList);
    }

    // Bluetooth
    public void bluetoothOn() {
        if (bluetoothAdapter == null) {
            showToast(R.string.toast_invaild_bt);
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                showToast(R.string.toast_on_bt);
                Intent intent_bt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent_bt, BT_REQUEST_ENABLE);
            }
        }
    }

    public void bluetoothOff() {
        if (bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        }
    }

    public void listPairedDevices() {
        if (bluetoothAdapter.isEnabled()) {
            pairedDevices = bluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                pairedDevicesList = new ArrayList<String>();
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().startsWith("CREACUBE")) {
                        pairedDevicesList.add(device.getName());
                        cubeList.add(device.getName());
                    }
                }
                setDeviceList();

            } else {

            }
        } else {
            showToast(R.string.toast_on_bt);
        }
    }

    public void searchDevice() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();

        } else {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.startDiscovery();
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(receiver, filter);

            } else {
                showToast(R.string.toast_on_bt);
            }
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String address = device.getAddress();   // MAC Address

                if (deviceName != null && deviceName.startsWith("CREACUBE")) {
                    cubeList.add(deviceName);
                    setDeviceList();
                }
            }
        }
    };

    public void connectDevice(String deviceName) {
        for (BluetoothDevice tempDevice : pairedDevices) {
            if (deviceName.equals(tempDevice.getName())) {
                bluetoothDevice = tempDevice;
                break;
            }
        }
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID);
            bluetoothSocket.connect();
            connectedBluetoothThread = new ConnectedBluetoothThread(bluetoothSocket);
            connectedBluetoothThread.start();
            bluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();
        } catch (IOException e) {
            showToast(R.string.toast_error_connect);
        }
    }

    private class ConnectedBluetoothThread extends Thread {
        private final BluetoothSocket tbluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public ConnectedBluetoothThread(BluetoothSocket socket) {
            tbluetoothSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream = tmpIn;
            outputStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = inputStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = inputStream.available();
                        bytes = inputStream.read(buffer, 0, bytes);
                        bluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
        public void write(String str) {
            byte[] bytes = str.getBytes();
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void cancel() {
            try {
                tbluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}