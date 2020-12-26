package com.yanhangtec.scannerlibrary.handler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 循环定时器
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 12/26/20 3:06 PM
 */
public class CycleTimer {

    private Timer pingTimer;
    private TimerTask pingTask;
    private int delayTime;

    private OnTimeListener listener;


    public CycleTimer(int delayTime, OnTimeListener listener) {
        if (delayTime <= 10000) {
            this.delayTime = 30000;
        } else {
            this.delayTime = delayTime;
        }
        this.listener = listener;
    }

    /**
     * 启动定时器
     */
    public void startTimer() {
        stopTimer();
        pingTimer = new Timer();
        pingTask = new TimerTask() {

            @Override
            public void run() {
                if (listener != null) {
                    listener.onTaskHandle();
                }
            }
        };
        pingTimer.schedule(pingTask, 0, delayTime);
    }

    /**
     * 关闭定时器
     */
    public void stopTimer() {
        if (pingTimer != null) {
            pingTimer.cancel();
        }
    }

    public interface OnTimeListener {
        void onTaskHandle();
    }
}
