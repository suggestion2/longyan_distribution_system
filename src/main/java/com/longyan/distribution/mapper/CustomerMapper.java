package com.longyan.distribution.mapper;

import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.response.BusinessListView;
import com.longyan.distribution.response.BusinessView;
import com.longyan.distribution.response.CustomerShortView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CustomerMapper {

    Customer selectById(Integer id);

    Customer select(Map<String, Object> map);

    List<Customer> selectList(Map<String, Object> map);

    List<CustomerShortView> selectShortViewList(Map<String, Object> map);

    List<BusinessView> selectBusinessList(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    int insert(Customer customer);

    int update(Customer customer);

    int updateLoginPassword(Customer customer);

    int updatePaymentPassword(Customer customer);

    int deleteById(Integer id);
}