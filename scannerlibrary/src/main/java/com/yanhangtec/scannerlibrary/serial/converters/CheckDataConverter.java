package com.yanhangtec.scannerlibrary.serial.converters;

import com.yanhangtec.scannerlibrary.utils.CharsUtils;

import org.sheedon.serial.DataCheckBean;
import org.sheedon.serial.DataConverter;
import org.sheedon.serial.ResponseBody;
import org.sheedon.serial.SafetyByteBuffer;

import java.util.Arrays;

/**
 * 数据校验转化器
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2020/3/11 0:45
 */
public class CheckDataConverter implements DataConverter<SafetyByteBuffer, DataCheckBean> {

    private static final byte[] STARTBIT = new byte[]{0x55, (byte) 0xAA};
    private static final byte[] ENDBIT = new byte[0];
    private static final byte[] EMPTY = new byte[4];

    CheckDataConverter() {

    }

    // 数据格式
    // 协议头  数据长度位  子控设备地址  命令类型    消息体    CRC16校验
    // 7A      0800         01              03         01       B07A
    @Override
    public DataCheckBean convert(SafetyByteBuffer value) {
        if (value == null || value.length() == 0) {
            return DataCheckBean.build(null, 0);
        }

        int index = value.indexOf(STARTBIT[0]);
        if (index == -1) {
            return DataCheckBean.build(ResponseBody.build(new byte[0],
                    CharsUtils.addBytes(EMPTY,value.getBytes()),
                    new byte[0],
                    ENDBIT, value.getBytes()), value.length());
        }

        if (index + 6 >= value.length()) {
            return DataCheckBean.build(null, index);
        }

        // 一个内容到总长度
        byte[] lengthStr = value.substring(index + 4, index + 6);
        int length = calcLength(lengthStr);
        if (length < 0 || index + length + 7 > value.length()) {
            return DataCheckBean.build(null, index);
        }

        byte[] content = value.substring(index, index + length + 7);
        boolean check = checkContent(content);
        if (check) {

            byte[] startBit = Arrays.copyOfRange(content, 0, 2);
            byte[] messageBit = Arrays.copyOfRange(content, 2, content.length - 1);
            byte[] parityBit = Arrays.copyOfRange(content, content.length - 1, content.length);

            ResponseBody body = ResponseBody.build(startBit,
                    messageBit,
                    parityBit,
                    ENDBIT, content);

            return DataCheckBean.build(body, index + length + 7);
        } else {
            return DataCheckBean.build(null, index + length + 7);
        }
    }

    /**
     * 获取总长度
     *
     * @param str 字符
     */
    private int calcLength(byte[] str) {
        byte lowPosition = str[0];
        byte highPosition = str[1];

        int low = lowPosition & 0xFF;
        int high = highPosition & 0xFF;

        return high * 16 * 16 + low;

    }


    /**
     * 核实内容校验码
     * 拿到校验码 后四位
     * 拿到内容 除了后四位外的数据
     *
     * @param content 内容
     * @return 校验是否一致
     */
    private boolean checkContent(byte[] content) {
        if (content.length <= 1)
            return false;

        byte[] checkBytes = Arrays.copyOfRange(content, content.length - 1, content.length);
        byte[] contentBytes = Arrays.copyOf(content, content.length - 1);
        byte resultBytes = CharsUtils.XOR(contentBytes);


        return checkBytes[0] == resultBytes;
    }
}
