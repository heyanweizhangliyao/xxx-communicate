package com.study.demo.model;

import java.io.Serializable;

/**
 * Created by heyanwei-thinkpad on 2019/2/27.
 */
public class SysUser implements Serializable{


    private String userName;
    private String email;
    private String mobile;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public SysUser(String userName) {
        this.userName = userName;
    }

    public SysUser() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
