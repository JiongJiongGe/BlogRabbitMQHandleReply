package com.mybatis.controller;

import com.google.gson.Gson;
import com.mybatis.rabbitmq.config.TopicRabbitConfig;
import com.mybatis.service.user.CalmWangUserServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * rabbitmq
 *
 * Created by yunkai on 2017/7/7.
 */
@RestController
@RequestMapping("rabbit")
public class RabbitMQController implements RabbitTemplate.ConfirmCallback{

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQController.class);

    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CalmWangUserServiceI calmWangUserService;

    @Autowired
    public RabbitMQController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this);
    }

    /**
     * 模拟消息发送成功
     *
     * @return
     */
    @GetMapping(value = "message")
    public String message(){
        for(Short i = 1; i < 7; i++){
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend(TopicRabbitConfig.TOPIC_EXCHANGE,"topic.two", new Gson().toJson(i), correlationData);
        }
        return "message success";
    }

    /**
     * 模拟消息进入交互机时错误
     *
     * @return
     */
    @GetMapping(value = "messagefalse")
    public String messagefalse(){
        String uuid = UUID.randomUUID().toString();
        CorrelationData correlationId = new CorrelationData(uuid);
        rabbitTemplate.convertAndSend("my-queue1","topic.two","来自星星的RabbitMQ的问候",correlationId);
        return "not rabbit success";
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info("回调id : {}", correlationData);
        if(ack){
            logger.info("消息进入交换机成功");
        }else{
            logger.info("消息进入交换机失败，原因：{}", cause);
        }
    }
}
