package com.mybatis.service.impl.user;

import com.google.gson.Gson;
import com.mybatis.dao.user.CalmWangUserDao;
import com.mybatis.domain.user.CalmWangUserModel;
import com.mybatis.rabbitmq.config.TopicRabbitConfig;
import com.mybatis.service.user.CalmWangUserServiceI;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by yunkai on 2017/5/24.
 */
@Service
@Transactional
public class CalmWangUserServiceImpl implements CalmWangUserServiceI {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public CalmWangUserServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    private CalmWangUserDao miaoGeUserDao;

    @Override
    public List<CalmWangUserModel> findUserList() {
        List<CalmWangUserModel> users = miaoGeUserDao.findAll();
        return users;
    }

    @Override
    public List<CalmWangUserModel> getByName(String name) {
        CalmWangUserModel user = new CalmWangUserModel();
        List<CalmWangUserModel> users = miaoGeUserDao.getByNameAndPhone(name, "13588313834");
        return users;
    }

    @Override
    public Short saveUser(String userName, String userPhone) {
        miaoGeUserDao.saveUser(userName, userPhone);
        Short userID = miaoGeUserDao.last_insert_id();
        return userID;
    }

    @Override
    public CalmWangUserModel getById(Short id){
        return miaoGeUserDao.getById(id);
    }

    @Override
    public void update(CalmWangUserModel user) {
        miaoGeUserDao.update(user.getUser_name(), user.getID());
    }

    @Override
    public void dealRabbitMessage(){
        //模拟给取出来的用户集合推送消息
        for(Short i = 1; i < 7; i++){
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend(TopicRabbitConfig.TOPIC_EXCHANGE,"topic.two", new Gson().toJson(i), correlationData);
        }
    }
}
