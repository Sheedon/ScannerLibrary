package com.yanhangtec.scanlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.yanhangtec.scannerlibrary.ScannerFactory;
import com.yanhangtec.scannerlibrary.clients.OnScannerListener;
import com.yanhangtec.scannerlibrary.clients.ScannerCenter;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements OnScannerListener {

    private ScannerCenter center = ScannerFactory.getScannerCenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        timer.schedule(task, 0, 2000);
        ScannerFactory.init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        center.addListener(this);
    }

    public void onTouchClick(View view) {
        Log.v("SXD", "isAlarm:" + center.getCurrentAlarm());
    }

    @Override
    protected void onPause() {
        super.onPause();
        center.removeListener(this);
    }

    @Override
    public void onScanResult(String code) {
        Log.v("SXD", "code:" + code);
    }

    @Override
    public void onScanAlarm(boolean isAlarm) {
        Log.v("SXD", "isAlarm:" + isAlarm);
    }
}