package com.study.demo.util;

/**
 * Created by 80263843 on 2019/7/31.
 */
public class A extends B implements C{



    public static void main(String[] args) {
        A a = new A();
        a.log();
    }



//    public void log() {
//        System.out.println("this A log");
//        super.log();
//    }
}
interface C {
    default void log(){
        System.out.println("interface C log");
    }
}

class B implements C{
    public void log(){
        System.out.println("parent class B log");
    }
}