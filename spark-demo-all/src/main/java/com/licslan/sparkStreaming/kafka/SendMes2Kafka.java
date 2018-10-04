package com.licslan.sparkStreaming.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.*;


/**
 * Created by licslan on 2018/10/2.
 */



@Component
@EnableScheduling
public class SendMes2Kafka {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Scheduled(cron = "00/1 * * * * ?")
    public void send(){
        String key = UUID.randomUUID().toString();
        List<Map<String,String>> mapList = new ArrayList<>();
        Map map = new HashMap();
        map.put("hello","world");
        map.put("licslan","hwl");
        mapList.add(map);
        ListenableFuture future = kafkaTemplate.send("licslan",key, mapList.toString());
        future.addCallback(o -> System.out.println("send-消息发送成功：" + mapList.toString()), throwable -> System.out.println("消息发送失败：" + key));
    }

}
