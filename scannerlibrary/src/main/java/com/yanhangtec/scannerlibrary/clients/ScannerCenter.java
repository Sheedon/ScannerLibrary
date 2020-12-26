package com.yanhangtec.scannerlibrary.clients;

/**
 * 扫码枪中心类
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/5/14 14:31
 */
public interface ScannerCenter {
    // 初始化配置
    void initConfig();

    // 增加监听器
    void addListener(OnScannerListener listener);

    // 核实
    void check();

    // 移除监听器
    void removeListener(OnScannerListener listener);

    // 获取当前告警状态
    boolean getCurrentAlarm();

    // 销毁
    void destroy();
}
