package com.yanhangtec.scannerlibrary;

import android.content.Context;


import com.yanhangtec.scannerlibrary.clients.ScannerCenter;
import com.yanhangtec.scannerlibrary.clients.ScannerClient;
import com.yanhangtec.scannerlibrary.serial.SerialPort;

/**
 * 扫码枪工厂
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/4/15 14:09
 */
public class ScannerFactory {
    private static final String TAG = ScannerFactory.class.getSimpleName();
    // 单例模式
    private static final ScannerFactory instance;

    private Context context;

    static {
        instance = new ScannerFactory();
    }

    private ScannerFactory() {

    }

    /**
     * Factory中的初始化
     */
    public static void init(Context context) {
        instance.context = context;

        ScannerClient.getInstance().initConfig();
    }

    public static void destroy() {
        SerialPort.clear();
    }

    public static Context getContext() {
        return instance.context;
    }


    /**
     * 获取一个扫码枪中心的实现类
     *
     * @return 扫码枪中心的规范接口
     */
    public static ScannerCenter getScannerCenter() {
        return ScannerClient.getInstance();
    }
}
