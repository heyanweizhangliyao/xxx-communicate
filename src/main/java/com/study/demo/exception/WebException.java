package com.study.demo.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by heyanwei-thinkpad on 2019/5/23.
 */
@ControllerAdvice
public class WebException {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String,Object> exceptionHandler(Exception e){

        //可自定义异常，然后分类处理
        if(e instanceof IllegalArgumentException){

        }else if( e instanceof IllegalStateException){

        }else if( e instanceof IndexOutOfBoundsException){

        }

        Map<String,Object> result = new HashMap<String,Object>();
        result.put("respCode", "9999");
        result.put("respMsg", e.getMessage());
        //正常开发中，可创建一个统一响应实体，如CommonResp
        return result;
    }
}