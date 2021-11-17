package com.zuanshi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zuanshi.bean.User;
import com.zuanshi.dao.UserDao;
import com.zuanshi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


//添加版本号（version = "1.0"）
/*
*   添加负载均衡的策略
*   1.随机  random
*   2.轮询  roundrobin
*   3.最少活跃调用数 leastactive
*   4.一致性哈希 consistenhash
*   既可以在服务提供者一方配置（@Service(loadbalance = "roundrobin")）
*   也可以在服务消费者一方配置（@Reference(loadbalance = "roundrobin")）
*   两者取一
*
* */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
}
