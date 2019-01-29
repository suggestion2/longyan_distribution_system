package com.longyan.distribution.mapper;

import com.longyan.distribution.domain.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OrderMapper {

    Order selectById(Integer id);

    Order select(Map<String, Object> map);

    List<Order> selectList(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    int insert(Order order);

    int update(Order order);

    int updateStatus(Order order);

    int deleteById(Integer id);
}