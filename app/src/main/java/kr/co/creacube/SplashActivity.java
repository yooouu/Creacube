package kr.co.creacube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import kr.co.creacube.component.ConfirmDialog;
import kr.co.creacube.util.CommonUtil;

import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
import static kr.co.creacube.util.CommonUtil.SPLASH_TIME;
import static kr.co.creacube.util.CommonUtil.permissionResultCallBack;

public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        if (requestPermission()) {
            startApp();
        }
    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(runnable);
        finish();
    }

    private boolean requestPermission() {
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

    //권한체크 후
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        final CommonUtil.PermissionCheck permissionCheck = permissionResultCallBack(this, requestCode, permissions, grantResults);

        if (permissionCheck == CommonUtil.PermissionCheck.PERMISSION_DENY || permissionCheck == CommonUtil.PermissionCheck.PERMISSION_N) {
            final ConfirmDialog dialog = new ConfirmDialog(this);
            dialog.setTitle(R.string.dialog_permission_title);
            dialog.setContent(R.string.dialog_permission_content);
            dialog.setConfirmListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (permissionCheck == CommonUtil.PermissionCheck.PERMISSION_DENY) {
                        Intent intent = new Intent(ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivityForResult(intent, 123);
                    } else {
                        requestPermission();
                    }
                }
            });
            dialog.setCancelListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            dialog.show();
        } else {
            startApp();
        }
    }

    private void startApp() {
        handler.postDelayed(runnable, SPLASH_TIME);
    }
}