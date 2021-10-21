package kr.co.creacube;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import kr.co.creacube.adapter.CubeListAdapter;

public class ConnectActivity extends AppCompatActivity implements View.OnClickListener {

    private CubeListAdapter cubeListAdapter;
    private List<String> cubeList = new ArrayList<>();

    RecyclerView recyclerCube;

    // Bluetooth
    BluetoothAdapter bluetoothAdapter;
    Set<BluetoothDevice> pairedDevices;
    List<String> pairedDevicesList;

    Handler bluetoothHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        recyclerCube = findViewById(R.id.rv_cube_list);

        cubeListAdapter = new CubeListAdapter(ConnectActivity.this, cubeList);
        recyclerCube.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerCube.setAdapter(cubeListAdapter);

        setDeviceList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        cubeListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
            case R.id.btn_wifi:
                //TODO
                break;
        }
    }

    private void setDeviceList() {
        //TODO get bluetooth device list
        cubeList.add("CREACUBE_PRO_0001");
        cubeList.add("CREACUBE_PRO_0002");
        cubeList.add("CREACUBE_PRO_0003");
        cubeListAdapter.setList(cubeList);
    }

    // Bluetooth
//    private class ConnectedBluetoothThread extends Thread {
//        private final BluetoothSocket bluetoothSocket;
//        private final InputStream inputStream;
//        private final OutputStream outputStream;
//
//        public ConnectedBluetoothThread(BluetoothSocket socket) {
//            socket = bluetoothSocket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//
//            try {
//                tmpIn = socket.getInputStream();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}