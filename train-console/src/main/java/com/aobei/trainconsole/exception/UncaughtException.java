package com.aobei.trainconsole.exception;

/**
 * create by renpiming on 2018/2/5
 */
public class UncaughtException implements Thread.UncaughtExceptionHandler{
    @Override
    public void uncaughtException(Thread t, Throwable e) {
         e.printStackTrace();
    }
}
