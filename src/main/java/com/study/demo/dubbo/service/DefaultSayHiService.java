package com.study.demo.dubbo.service;

import com.heyanwei.rpc.service.SayHiService;
import org.apache.dubbo.config.annotation.Service;

/**
 * Created by heyanwei-thinkpad on 2019/5/29.
 */
@Service(version = "1.0.0")
public class DefaultSayHiService implements SayHiService {
    @Override
    public String sayHi(String name) {
        return "hi,"+name;
    }
}
