package com.zuanshi.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zuanshi.bean.User;
import com.zuanshi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


    //可以添加属性version，有时我们需要更改Service的功能时，可以在服务提供者那边定义版本号，这边注入的版本号就是那边的版本号
    //@Reference(version="1.0")
    @Reference
    private UserService userService;

    @RequestMapping("/userList")
    public List<User> findAll(){
        return userService.findAll();
    }
}
