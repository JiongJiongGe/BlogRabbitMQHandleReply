package com.mybatis.thread;

import com.mybatis.domain.user.CalmWangUserModel;
import com.mybatis.service.user.CalmWangUserServiceI;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.List;

/**
 * Created by yunkai on 2017/6/12.
 */

@Configurable
public class UserThread implements Runnable{


    @Override
    public void run() {
        System.out.println("zzzzzzzzzzzzzzzzzzz");
        long starTime=System.currentTimeMillis();
        CalmWangUserServiceI miaoGeUserService = (CalmWangUserServiceI)SpringContextUtil.getBean(CalmWangUserServiceI.class);
        for(int i=0; i<10000; i++) {
            List<CalmWangUserModel> users = miaoGeUserService.findUserList();
        }
        long endTime=System.currentTimeMillis();
        long time = endTime -starTime;
        System.out.println("time===========" + time);
    }

}
