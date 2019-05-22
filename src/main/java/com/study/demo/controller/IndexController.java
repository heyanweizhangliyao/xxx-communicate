package com.study.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/index")
public class IndexController extends AbstractController{

    public String index(){
        return "welcome to heyanwei-thinkpad";
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/test1")
    public List<Map<String,Object>> testIndex(){
        return jdbcTemplate.queryForList("SELECT * FROM account_flow LIMIT 10");
    }



}
