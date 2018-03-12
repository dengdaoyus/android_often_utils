package com.util.utilslibrary;

import java.util.Random;

/**
 * 随机生成小数工具类
 * Created by Administrator
 * on 2017/3/3 0003.
 */

public class RandomUtil {

    /**
     * 根据最小和最大值随机生成带两位小数的值
     * @param min
     * @param max
     * @return
     */
    public static String getRandom(double min,double max) {
        double d;
        Random r = new Random();
        while (true) {
            d = r.nextDouble() * max;//小于最大
            if (d > min) {//大于最小
                break;
            }
        }
        d = d / 100D;

        return  String.format("%.2f", d);
    }
}
