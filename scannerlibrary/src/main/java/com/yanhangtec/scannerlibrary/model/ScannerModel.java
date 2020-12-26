package com.yanhangtec.scannerlibrary.model;

import com.yanhangtec.scannerlibrary.utils.CharsUtils;

import org.sheedon.serial.retrofit.serialport.RULES;

/**
 * 扫码枪数据
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/5/14 14:52
 */
public class ScannerModel {

    @RULES(end = 1)
    private byte[] command;// 命令
    @RULES(begin = 1, end = 2)
    private byte[] mark;// 标示
    @RULES(begin = 2, end = 4)
    private byte[] length;// 长度
    @RULES(begin = 4,end = -100)
    private byte[] code;// 标签编号

    public byte[] getCode() {
        return code;
    }

    public String getDecodeCode() {
        return CharsUtils.byte2Str(code);
    }
}
