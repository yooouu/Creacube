package kr.co.creacube;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ConnectActivity extends AppCompatActivity implements View.OnClickListener {

    private List<String> deviceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
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
        //TODO set list
    }
}