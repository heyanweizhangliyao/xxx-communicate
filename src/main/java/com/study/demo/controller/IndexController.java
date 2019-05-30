package com.study.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class IndexController extends AbstractController{

    public String index(){
        return "welcome to heyanwei-thinkpad";
    }


    @RequestMapping("/sayhi")
    public Object sayHi(String name){
        return null;}


}
