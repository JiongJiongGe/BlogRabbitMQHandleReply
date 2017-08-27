package com.mybatis.rabbitmq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbit 管理Bean
 *
 * Created by yunkai on 2017/7/7.
 */
//@Configuration  自动注入，启动项目时会启动创建一个消息
public class QueueManagerBean {

    // @Bean 自动注入，启动项目时会启动创建一个消息
    public Queue wiselyQueue(){
        return new Queue("my-queue");
    }
}
