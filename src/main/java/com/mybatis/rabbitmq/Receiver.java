package com.mybatis.rabbitmq;

import com.mybatis.domain.user.CalmWangUserModel;
import com.mybatis.service.user.CalmWangUserServiceI;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by yunkai on 2017/7/6.
 */
@Component
public class Receiver {

    @Autowired
    private CalmWangUserServiceI calmWangUserService;

    enum Action {
        ACCEPT,  // 处理成功
        RETRY,   // 可以重试的错误
        REJECT,  // 无需重试的错误
    }

    private Integer a = 2;

    //@RabbitListener(queues = "my-queue")
    public void receiveMessage(String message, Message me, Channel channel){
        try {
            CalmWangUserModel user = new CalmWangUserModel();
            user.setUser_name("pointyun");
            user.setUser_phone("88888888");
            Short userID = calmWangUserService.saveUser("pointyun", "88888888");
            System.out.println("userID ============== " + userID);
            System.out.println("Received < " + message + ">");
            channel.basicAck(me.getMessageProperties().getDeliveryTag(), false);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void topicOneReceiver(String message){
        System.out.println("one begin  message = === === == " + message);
    }

//    @RabbitListener(queues = "topic.two")
//    public void topicTwoReceiver(Message message, Channel channel) throws IOException {
//        //Action action = Action.RETRY;
//        String action = "RETRY";
//        long tag = 1;
//        try {
//            a = a + 1;
//            System.out.println("a=============================" + a);
//            action = "ACCEPT";
//        }catch (Exception e) {
//            e.printStackTrace();
//            action = "REJECT";
//            throw new IOException();
//        }finally {
//            boolean autoAck = false;
//            String queueName = channel.queueDeclare().getQueue();
//            String finalAction = action;
//            channel.basicConsume("topic.two", false, "" + a,
//                    new DefaultConsumer(channel) {
//                        @Override
//                        public void handleDelivery(String consumerTag,
//                                                   Envelope envelope,
//                                                   AMQP.BasicProperties properties,
//                                                   byte[] body)
//                                throws IOException {
//                            String routingKey = envelope.getRoutingKey();
//                            String contentType = properties.getContentType();
//                            long deliveryTag = envelope.getDeliveryTag();
//                            // (process the message components here ...)
//                            // channel.basicAck(deliveryTag, false);
//                            if (finalAction.equals("ACCEPT")) {
//                                channel.basicAck(deliveryTag, false);
//                            } else if (finalAction.equals("RETRY")) {
//                                channel.basicNack(deliveryTag, false, false);
//                            } else {
//                                channel.basicNack(deliveryTag, false, false);
//                            }
//                        }
//                    });
//
//
////            if (action == Action.ACCEPT) {
////                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
////            } else if (action == Action.RETRY) {
////                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
////            } else {
////                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
////            }
//            //       }
////            Short userID = miaoGeUserService.saveUser("pointyun", "88888888");
//
//            //          channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//            //      channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false); // ack返回false，
//            //          channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
////            System.out.println("userId =====" + userID);
//        }
//
//    }

    public void confirm(){
        System.out.println("washdjahsdhajksd");
    }
}
