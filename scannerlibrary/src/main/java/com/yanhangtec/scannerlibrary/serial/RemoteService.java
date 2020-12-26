package com.yanhangtec.scannerlibrary.serial;

import com.yanhangtec.scannerlibrary.model.PingModel;
import com.yanhangtec.scannerlibrary.model.ScannerModel;

import org.sheedon.serial.retrofit.Call;
import org.sheedon.serial.retrofit.Observable;
import org.sheedon.serial.retrofit.serialport.BACKNAME;
import org.sheedon.serial.retrofit.serialport.DELAYMILLISECOND;
import org.sheedon.serial.retrofit.serialport.MESSAGEBIT;
import org.sheedon.serial.retrofit.serialport.PARITYBIT;

/**
 * 串口接口
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/4/7 12:28
 */
public interface RemoteService {


    /**
     * 绑定扫码枪反馈
     */
    @BACKNAME("30")
    Observable<ScannerModel> bindScanner();

    @MESSAGEBIT("010000")
    @PARITYBIT("FE")
    @BACKNAME("01")
    @DELAYMILLISECOND(20000)
    Call<PingModel> checkPing();
}
