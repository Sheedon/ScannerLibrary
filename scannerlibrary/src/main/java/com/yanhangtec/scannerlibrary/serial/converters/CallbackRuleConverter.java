package com.yanhangtec.scannerlibrary.serial.converters;


import com.yanhangtec.scannerlibrary.utils.CharsUtils;

import org.sheedon.serial.DataConverter;

/**
 * 反馈规则转换器
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/3/11 0:45
 */
public class CallbackRuleConverter implements DataConverter<byte[], Long> {

    CallbackRuleConverter() {

    }

    @Override
    public Long convert(byte[] value) {
        if (value == null || value.length < 3)
            return 48L;

        if(value[0] != 0x55){
            return 48L;
        }

        return (long) CharsUtils.byteToHexInteger(value[2]);
    }
}
