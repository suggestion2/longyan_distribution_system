package com.longyan.distribution.mapper;

import com.longyan.distribution.domain.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserMapper {

    User selectById(Integer id);

    User select(Map<String, Object> map);

    List<User> selectList(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    int insert(User user);

    int update(User user);

    int deleteById(Integer id);
}