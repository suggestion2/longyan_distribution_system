package com.longyan.distribution.mapper;

import com.longyan.distribution.domain.OrderItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OrderItemMapper {

    OrderItem selectById(Integer id);

    OrderItem select(Map<String, Object> map);

    List<OrderItem> selectList(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    int insert(OrderItem orderItem);

    int update(OrderItem orderItem);

    int deleteById(Integer id);
}