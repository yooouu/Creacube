package kr.co.creacube;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import kr.co.creacube.adapter.CubeListAdapter;

public class ConnectActivity extends AppCompatActivity implements View.OnClickListener {

    private CubeListAdapter cubeListAdapter;
    private List<String> deviceList = new ArrayList<>();

    RecyclerView recyclerCube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        recyclerCube = findViewById(R.id.rv_cube_list);

        cubeListAdapter = new CubeListAdapter(ConnectActivity.this, deviceList);
        recyclerCube.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerCube.setAdapter(cubeListAdapter);
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
    }
}