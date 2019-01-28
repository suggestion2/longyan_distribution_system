package com.longyan.distribution.service.impl;

import com.longyan.distribution.domain.User;
import com.longyan.distribution.service.UserService;
import com.longyan.distribution.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getById(Integer id){
        return userMapper.selectById(id);
    }

    @Override
    public User getByName(String name) {
        return userMapper.select(Collections.singletonMap("name",name));
    }

    @Override
    public User select(Map<String, Object> map){
        return userMapper.select(map);
    }

    @Override
    public List<User> selectList(Map<String, Object> map){
        return userMapper.selectList(map);
    }

    @Override
    public int selectCount(Map<String, Object> map){
        return userMapper.selectCount(map);
    }

    @Override
    public int create(User user){
        return userMapper.insert(user);
    }

    @Override
    public int update(User user){
        return userMapper.update(user);
    }

    @Override
    public int deleteById(Integer id){
        return userMapper.deleteById(id);
    }
}