package com.zuanshi.dao;

import com.zuanshi.bean.User;

import java.util.List;

public interface UserDao {
    List<User> findAll();
}
