package com.yanhangtec.scannerlibrary.model;

import com.yanhangtec.scannerlibrary.utils.CharsUtils;

import org.sheedon.serial.retrofit.serialport.RULES;

/**
 * 心跳包
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 12/26/20 3:24 PM
 */
public class PingModel {

    @RULES(end = 1)
    private byte[] command;// 命令
    @RULES(begin = 1, end = 2)
    private byte[] mark;// 标示
    @RULES(begin = 2, end = 4)
    private byte[] length;// 长度
    @RULES(begin = 4, end = 6)
    private byte[] data;// 数据

    public byte[] getMark() {
        return mark;
    }

    public boolean getDecodeMark() {
        if (mark == null || mark.length == 0) {
            return false;
        }
        return mark[0] == 0;
    }
}
