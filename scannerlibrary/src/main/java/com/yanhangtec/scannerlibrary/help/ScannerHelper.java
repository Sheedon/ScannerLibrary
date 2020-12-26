package com.yanhangtec.scannerlibrary.help;

import com.yanhangtec.scannerlibrary.model.DataSource;
import com.yanhangtec.scannerlibrary.model.PingModel;
import com.yanhangtec.scannerlibrary.model.ScannerModel;
import com.yanhangtec.scannerlibrary.serial.RemoteService;
import com.yanhangtec.scannerlibrary.serial.SerialPort;

import org.sheedon.serial.retrofit.Call;
import org.sheedon.serial.retrofit.Callback;
import org.sheedon.serial.retrofit.Observable;
import org.sheedon.serial.retrofit.Response;

/**
 * 扫码枪辅助类
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/5/14 14:50
 */
public class ScannerHelper {
    /**
     * 绑定重量结果
     *
     * @param callback 反馈
     */
    public static Observable bindScanner(final DataSource.SucceedCallback<String> callback) {
        RemoteService service = SerialPort.remote();
        Observable<ScannerModel> observable = service.bindScanner();
        observable.subscribe(new Callback.Observable<ScannerModel>() {
            @Override
            public void onResponse(Observable<ScannerModel> call, Response<ScannerModel> response) {
                if (callback == null)
                    return;

                if (response == null || !response.isSuccessful() || response.body() == null) {
                    callback.onDataLoaded("");
                    return;
                }

                callback.onDataLoaded(response.body().getDecodeCode());
            }

            @Override
            public void onFailure(Observable<ScannerModel> call, Throwable t) {

            }
        });

        return observable;
    }


    /**
     * 核实心跳
     */
    public static void checkPing(final DataSource.SucceedCallback<Boolean> callback) {
        RemoteService service = SerialPort.remote();
        Call<PingModel> observable = service.checkPing();
        observable.enqueue(new Callback.Call<PingModel>() {
            @Override
            public void onResponse(Call<PingModel> call, Response<PingModel> response) {
                if (callback == null)
                    return;

                if (response == null || !response.isSuccessful() || response.body() == null) {
                    callback.onDataLoaded(false);
                    return;
                }

                callback.onDataLoaded(response.body().getDecodeMark());
            }

            @Override
            public void onFailure(Call<PingModel> call, Throwable t) {
                if (callback == null)
                    return;
                callback.onDataLoaded(false);
            }
        });
    }
}
