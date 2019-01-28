package com.longyan.distribution.service;

import com.longyan.distribution.domain.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    User getById(Integer id);

    User getByName(String name);

    User select(Map<String, Object> map);

    List<User> selectList(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    int create(User user);

    int update(User user);

    int deleteById(Integer id);
}