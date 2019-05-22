package com.study.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by heyanwei-thinkpad on 2019/2/27.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ResultData {
    private int code;
    private String msg;
    private Object data;

    public ResultData(int code, String msg,Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResultData loginErr() {
        return new ResultData(201,"用户名或密码错误",null);
    }

    public static ResultData success(Object data) {
        return new ResultData(200,"登录成功",data);
    }

    public static ResultData error(String msg) {
        return new ResultData(500,msg,null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
