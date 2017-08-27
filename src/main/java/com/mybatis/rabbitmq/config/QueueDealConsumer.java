package com.mybatis.rabbitmq.config;

import com.mybatis.domain.log.CalmWangLogModel;
import com.mybatis.domain.user.CalmWangUserModel;
import com.mybatis.service.log.CalmWangLogServiceI;
import com.mybatis.service.user.CalmWangUserServiceI;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yunkai on 2017/8/14.
 */
@Service
public class QueueDealConsumer implements ChannelAwareMessageListener {

    enum DealResult {
        ACCEPT,  // 处理成功
        RETRY,   // 可以重试的错误
        REJECT,  // 无需重试的错误
    }

    @Autowired
    private CalmWangUserServiceI calmWangUserService;

    @Autowired
    private CalmWangLogServiceI calmWangLogService;

    private static final Logger logger = LoggerFactory.getLogger(QueueDealConsumer.class);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        byte[] bodyBytes = message.getBody();
        logger.info("message body second: {}", new String(message.getBody()));
        String userId = new String(message.getBody());
        Short id = Short.parseShort(userId.trim());
        DealResult dealResult = DealResult.ACCEPT;
        try{
            CalmWangUserModel user = calmWangUserService.getById(id);
            user.setUser_name(user.getUser_name() + "_NEW");
            calmWangUserService.update(user);
         } catch(Exception e){
            CalmWangLogModel log = new CalmWangLogModel();
            calmWangLogService.saveLog(id);
            dealResult = DealResult.REJECT;
         } finally {
            if(dealResult == DealResult.ACCEPT){
                //通知队列消息已消费
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }else if(dealResult == DealResult.REJECT){
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            }else{
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,true);
            }
         }
    }
}
