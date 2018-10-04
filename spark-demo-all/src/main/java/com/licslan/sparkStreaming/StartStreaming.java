package com.licslan.sparkStreaming;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Created by Administrator on 2018/10/2.
 */
public class StartStreaming implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext ac = event.getApplicationContext();
        SparkKafkaStreamExecutor sparkKafkaStreamExecutor= ac.getBean(SparkKafkaStreamExecutor.class);
        Thread thread = new Thread(sparkKafkaStreamExecutor);
        thread.start();
    }

    public static void main(String[] args) {

    }

}
