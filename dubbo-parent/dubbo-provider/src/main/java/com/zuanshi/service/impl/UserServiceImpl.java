package com.zuanshi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zuanshi.bean.User;
import com.zuanshi.dao.UserDao;
import com.zuanshi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
}
