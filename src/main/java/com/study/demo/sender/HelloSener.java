package com.study.demo.sender;

import com.study.demo.model.Person;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

//@Component
public class HelloSener {

    @Autowired
    private AmqpTemplate amqpTemplate;

    private static final String DEFAULT_QUEUE_NAME="hello";

    public boolean sendMsg(String queueName,Object message){
        if(StringUtils.isEmpty(queueName)){
            queueName = DEFAULT_QUEUE_NAME;
        }
        try {
          /*  SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sendMsg = message + time.format(new Date()) + " hello1 ";*/
            Person person =
                    new Person();
            person.setName("张三");
            person.setAddr("上海");
            System.out.println("Sender1 : " + person);
            this.amqpTemplate.convertAndSend(queueName, person);
            return true;
        } catch (AmqpException e) {
            e.printStackTrace();
        }
        return false;
    }
}
