package com.yanhangtec.scannerlibrary.clients;

import com.yanhangtec.scannerlibrary.handler.CycleTimer;
import com.yanhangtec.scannerlibrary.help.ScannerHelper;
import com.yanhangtec.scannerlibrary.model.DataSource;
import com.yanhangtec.scannerlibrary.serial.SerialPort;

import org.sheedon.serial.SerialClient;
import org.sheedon.serial.retrofit.Observable;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 扫码枪客户端
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/5/14 14:27
 */
public class ScannerClient implements ScannerCenter,
        DataSource.SucceedCallback<String>, CycleTimer.OnTimeListener {

    private Set<OnScannerListener> listeners = new LinkedHashSet<>();

    private Observable bindObservable;
    // 心跳包
    private CycleTimer pingTimer;

    // 是否扫码告警
    private boolean isScanAlarm = false;


    public static ScannerClient getInstance() {
        return ScannerHolder.INSTANCE;
    }

    private static class ScannerHolder {
        private static final ScannerClient INSTANCE = new ScannerClient();
    }

    private ScannerClient() {

    }

    /**
     * 初始化
     */
    @Override
    public void initConfig() {
        bindScanner();
        pingTimer = new CycleTimer(30000, this);
    }

    /**
     * 新增监听器
     *
     * @param listener 监听器
     */
    @Override
    public void addListener(OnScannerListener listener) {
        if (listener == null)
            return;

        if (listeners.contains(listener))
            return;

        listeners.add(listener);
    }

    /**
     * 核实状态
     */
    @Override
    public void check() {

        SerialClient client = SerialPort.getClient();
        if (client == null || client.dispatcher() == null) {
            bindScanner();
            return;
        }

        if (client.dispatcher().hasCallback(0)) {
            return;
        }

        bindScanner();
    }

    /**
     * 绑定扫码枪
     */
    private void bindScanner() {
        if (bindObservable != null) {
            if (!bindObservable.isCanceled()) {
                bindObservable.cancel();
            }
        }

        bindObservable = ScannerHelper.bindScanner(this);
    }


    @Override
    public void removeListener(OnScannerListener listener) {
        if (listener == null)
            return;

        listeners.remove(listener);
    }

    @Override
    public boolean getCurrentAlarm() {
        return isScanAlarm;
    }

    /**
     * 更新转化效果
     *
     * @param isNeedConvert 是否需要转化
     */
    public void updateConvert(boolean isNeedConvert) {
        if (isNeedConvert) {
            startTimer();
            notifyAlarm(false);
        } else {
            stopTimer();
        }
    }

    /**
     * 启动定时器
     */
    private void startTimer() {
        if (pingTimer == null) {
            pingTimer = new CycleTimer(30000, this);
        }

        pingTimer.startTimer();
    }

    /**
     * 关闭定时器
     */
    private void stopTimer() {
        if (pingTimer == null) {
            return;
        }

        pingTimer.stopTimer();
    }

    /**
     * 任务处理
     */
    @Override
    public void onTaskHandle() {
        ScannerHelper.checkPing(callback);
    }

    /**
     * 二维码反馈
     *
     * @param code 二维码
     */
    @Override
    public void onDataLoaded(String code) {
        noticeScanResult(code);

        if (isScanAlarm) {
            isScanAlarm = false;
            notifyAlarm(false);
        }
    }

    /**
     * 通知扫码枪数据
     *
     * @param code 扫码枪数据
     */
    private void noticeScanResult(String code) {
        for (OnScannerListener listener : listeners) {
            if (listener != null) {
                listener.onScanResult(code);
            }
        }
    }

    /**
     * 通知告警
     *
     * @param isAlarm 是否扫码告警
     */
    private void notifyAlarm(boolean isAlarm) {
        for (OnScannerListener listener : listeners) {
            if (listener != null) {
                listener.onScanAlarm(isAlarm);
            }
        }
    }

    /**
     * 是否告警反馈
     */
    private DataSource.SucceedCallback<Boolean> callback = new DataSource.SucceedCallback<Boolean>() {

        @Override
        public void onDataLoaded(Boolean aBoolean) {
            isScanAlarm = aBoolean;
        }
    };

    @Override
    public void destroy() {
        stopTimer();
        SerialPort.clear();
    }
}
