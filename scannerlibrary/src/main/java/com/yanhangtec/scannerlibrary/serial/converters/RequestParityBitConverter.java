package com.yanhangtec.scannerlibrary.serial.converters;

import android.annotation.SuppressLint;

import com.yanhangtec.scannerlibrary.utils.CharsUtils;

import org.sheedon.serial.retrofit.Converter;
import org.sheedon.serial.retrofit.SerialMessage;


/**
 * 请求校验码转化器
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/4/20 17:12
 */
public final class RequestParityBitConverter implements Converter<SerialMessage, String> {


    @SuppressLint("DefaultLocale")
    @Override
    public String convert(SerialMessage value) {
        if (value == null)
            return "00";

        if (value.getParityBit() != null && !value.getParityBit().equals("")) {
            return value.getParityBit().toUpperCase();
        }

        if (value.getMessageBit() == null || value.getMessageBit().isEmpty()) {
            return "00";
        }

        String contentStr = value.getStartBit() + value.getMessageBit();
        StringBuilder checkResult = new StringBuilder(CharsUtils.XOR(CharsUtils.hexStringToBytes(contentStr)));
        checkResult = checkResult.delete(0, contentStr.length());
        for (int index = checkResult.length(); index < 2; index++) {
            checkResult.insert(0, "0");
        }
        return checkResult.substring(0, 2);
    }

}
