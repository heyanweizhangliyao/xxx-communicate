package com.study.demo.hook;

import java.util.concurrent.TimeUnit;

/**
 * Created by heyanwei-thinkpad on 2019/7/8.
 */
public class HookPrimary {


    public static void main(String[] args) {

        exitNormal();
//        exitOutOfMemory();

    }

    /**
     * 程序正常退出
     */
    public static void exitNormal(){
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("----shutdown-normal---");
        }));
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("----shutdown-outofmemory---");
        }));
        System.out.println("abc");
        try
        {
            TimeUnit.MILLISECONDS.sleep(5000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 堆溢出
     */
    public static void exitOutOfMemory(){

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("----shutdown-normal---");
        }));
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("----shutdown-outofmemory---");
        }));

        System.out.println("The Application is doing something");
        byte[] b = new byte[2000*1024*1024];
        try
        {
            TimeUnit.MILLISECONDS.sleep(5000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }
}
