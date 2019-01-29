package com.longyan.distribution.service;

import com.longyan.distribution.domain.Order;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public interface OrderService {
    Order getById(Integer id);

    Order select(Map<String, Object> map);

    List<Order> selectList(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    int create(Order order);

    int update(Order order);

    int updateStatus(Order order);

    int deleteById(Integer id);
}