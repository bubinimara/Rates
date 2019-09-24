package com.github.bubinimara.rates.data.utils;

/**
 * Created by davide.
 */
public class ThreadUtils {

    public static void sleep(long seconds){
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
