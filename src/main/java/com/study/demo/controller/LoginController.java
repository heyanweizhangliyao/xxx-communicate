package com.study.demo.controller;

import com.study.demo.model.ResultData;
import com.study.demo.model.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by heyanwei-thinkpad on 2019/2/27.
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResultData login(HttpServletRequest request, HttpServletResponse response, String userName, String password, boolean rememberMe){
        if(StringUtils.isBlank(userName) || StringUtils.isBlank(password)){
            return ResultData.error("用户名和密码不能为空");
        }

        if(userName.equals("heyanwei") && password.equals("zly761563491")){
            if(rememberMe){
                Cookie cookie1 = new Cookie("userName",userName);
                Cookie cookie2 = new Cookie("password",password);
                Cookie cookie3 = new Cookie("rememberMe","true");
                cookie1.setMaxAge(60*60*24*7);
                cookie2.setMaxAge(60*60*24*7);
                cookie3.setMaxAge(60*60*24*7);
                response.addCookie(cookie1);
                response.addCookie(cookie2);
                response.addCookie(cookie3);
            }else{
                Cookie[] cookies = request.getCookies();
                if(cookies != null && cookies.length>0){
                    for (int i = 0; i < cookies.length; i++) {
                        cookies[i].setMaxAge(0);
                        response.addCookie(cookies[i]);
                    }
                }
            }

            return ResultData.success(new SysUser(userName));
        }else{
            return ResultData.loginErr();
        }
    }


}
