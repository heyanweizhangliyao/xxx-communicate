package com.study.demo.pieces;

/**
 * Created by 80263843 on 2019/7/23.
 */
public class CaseDemo {

    /**
     * System.err与System.out共用时输出乱序
     */
    public static void testPrintWithoutOrder(){


        System.out.println("start...");
        System.err.println("middle...");
        System.out.println("end...");
    }
}
