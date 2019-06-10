package com.study.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class IndexController extends AbstractController{

    public String index(){
        return "welcome to heyanwei-thinkpad";
    }

    /**
     * 不能在同个项目中使用
     */
//    @Reference(version="1.0.0")
//    private SysUserService sysUserService;
}
