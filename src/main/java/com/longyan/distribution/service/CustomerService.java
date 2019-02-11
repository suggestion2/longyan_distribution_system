package com.longyan.distribution.service;

import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.response.BusinessListView;
import com.longyan.distribution.response.BusinessView;
import com.longyan.distribution.response.CustomerShortView;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CustomerService {
    Customer getById(Integer id);

    Customer select(Map<String, Object> map);

    Customer selectByPhone(String phone);

    List<Customer> selectList(Map<String, Object> map);

    List<BusinessView> selectBusinessList(Map<String, Object> map);

    public List<CustomerShortView> selectShortViewList(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    int create(Customer customer);

    int update(Customer customer);

    int updateBusiness(Customer customer);

    int updateLevel(Customer customer);

    int updateAddCustomerGold(Customer customer);

    int updateAddCustomerCoin(Customer customer);

    int updateAddBusinessGold(Customer customer);

    int updateReduceCustomerGold(Customer customer);

    int updateReduceCustomerOilDrill(Customer customer);

    int updateAddCustomerOilDrill(Customer customer);

    int updateReduceBusinessGold(Customer customer);

    int updateReduceBusinessOilDrill(Customer customer);

    int updateLoginPassword(Customer customer);

    int updatePaymentPassword(Customer customer);

    int deleteById(Integer id);
}