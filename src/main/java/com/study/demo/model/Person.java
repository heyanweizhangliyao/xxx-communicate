package com.study.demo.model;

import java.io.Serializable;

public class Person implements Serializable {
    private String name;
    private String addr;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", addr='" + addr + '\'' +
                '}';
    }

    public Person(String name, String addr) {
        this.name = name;
        this.addr = addr;
    }

    public Person() {

    }
}
