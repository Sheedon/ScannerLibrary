package com.yanhangtec.scannerlibrary.clients;

/**
 * 扫码枪监听器
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/5/14 14:28
 */
public interface OnScannerListener {
    // 扫码反馈结果
    void onScanResult(String code);

    // 扫码是否告警
    void onScanAlarm(boolean isAlarm);
}
