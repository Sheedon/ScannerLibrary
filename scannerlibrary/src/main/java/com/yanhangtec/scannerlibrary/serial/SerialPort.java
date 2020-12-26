package com.yanhangtec.scannerlibrary.serial;

import android.util.Log;

import com.yanhangtec.scannerlibrary.clients.ScannerClient;
import com.yanhangtec.scannerlibrary.serial.converters.DataConverterFactory;
import com.yanhangtec.scannerlibrary.serial.converters.SerialConverterFactory;
import com.yanhangtec.scannerlibrary.utils.CharsUtils;

import org.sheedon.serial.ResponseBody;
import org.sheedon.serial.SerialClient;
import org.sheedon.serial.retrofit.Retrofit;
import org.sheedon.serial.serialport.SerialRealCallback;


/**
 * 串口请求封装
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/4/7 21:24
 */
public class SerialPort {
    private static final String STARTBIT = "55AA";
    private static final String ENDBIT = "";

    private static SerialPort instance;
    private Retrofit retrofit;
    private SerialClient client;
    private boolean isNeedConvert = false;

    static {
        instance = new SerialPort();
    }

    private SerialPort() {
    }

    public static SerialClient getClient() {
        if (instance.client != null)
            return instance.client;

        // 存储起来
        instance.client = new SerialClient.Builder()
                // 串口
                .path("/dev/ttyS2")
                // 波特率
                .baudRate(9600)
                // 名称 随意
                .name("bridge_main")
                // 线程取值间隔
                .threadSleepTime(300)
                // 信息超时时长
                .messageTimeout(3)
                .addConverterFactory(DataConverterFactory.create())
                .callback(instance.callback)
                .build();

        return instance.client;
    }

    // 构建一个Retrofit
    private static Retrofit getRetrofit() {
        if (instance.retrofit != null) {
            return instance.retrofit;
        }

        // 得到一个SerialPort Client
        SerialClient client = getClient();

        // 设置链接
        instance.retrofit = new Retrofit.Builder()
                // 设置client
                .client(client)
                // 设置默认起始位
                .baseStartBit(STARTBIT)
                // 设置默认停止位
                .baseEndBit(ENDBIT)
                // 设置串口解析器
                .addConverterFactory(SerialConverterFactory.create())
                .build();

        return instance.retrofit;
    }

    public static void clear() {
//        if (instance.client != null)
//            instance.client.destroy();
//        instance.retrofit = null;
//        instance.client = null;
    }

    /**
     * 返回一个请求代理
     *
     * @return RemoteService
     */
    public static RemoteService remote() {
        return SerialPort.getRetrofit().create(RemoteService.class);
    }

    private SerialRealCallback callback = new SerialRealCallback() {
        @Override
        public void onCallback(ResponseBody data) {
            if (data == null)
                return;

            if ((data.getStartBit() == null || data.getStartBit().length == 0) && isNeedConvert) {
                isNeedConvert = false;
                ScannerClient.getInstance().updateConvert(false);
            } else if (data.getStartBit() != null && data.getStartBit().length == 2 && !isNeedConvert) {
                isNeedConvert = true;
                ScannerClient.getInstance().updateConvert(true);
            }
        }

        @Override
        public void onRequest(String message) {
        }
    };
}
