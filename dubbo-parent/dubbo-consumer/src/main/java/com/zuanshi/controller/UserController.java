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


    @Reference
    private UserService userService;

    @RequestMapping("/userList")
    public List<User> findAll(){
        return userService.findAll();
    }
}
