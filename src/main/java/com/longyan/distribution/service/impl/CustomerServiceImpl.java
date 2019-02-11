package com.longyan.distribution.service.impl;

import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.mapper.TransferParams;
import com.longyan.distribution.response.BusinessListView;
import com.longyan.distribution.response.BusinessView;
import com.longyan.distribution.response.CustomerShortView;
import com.longyan.distribution.service.CustomerService;
import com.longyan.distribution.mapper.CustomerMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public Customer getById(Integer id){
        return customerMapper.selectById(id);
    }



    @Override
    public Customer select(Map<String, Object> map){
        return customerMapper.select(map);
    }

    @Override
    public Customer selectByPhone(String phone){
        Map<String, Object> map = new HashMap<>();
        map.put("phone",phone);
        return customerMapper.select(map);
    }

    @Override
    public List<Customer> selectList(Map<String, Object> map){
        return customerMapper.selectList(map);
    }

    @Override
    public List<CustomerShortView> selectShortViewList(Map<String, Object> map){
        return customerMapper.selectShortViewList(map);
    }

    @Override
    public List<BusinessView> selectBusinessList(Map<String, Object> map){
        return customerMapper.selectBusinessList(map);
    }



    @Override
    public int selectCount(Map<String, Object> map){
        return customerMapper.selectCount(map);
    }

    @Override
    public int create(Customer customer){
        return customerMapper.insert(customer);
    }

    @Override
    public int update(Customer customer){
        return customerMapper.update(customer);
    }

    @Override
    public int updateBusiness(Customer customer){
        return customerMapper.updateBusiness(customer);
    }

    @Override
    public int updateLevel(Customer customer){
        return customerMapper.updateLevel(customer);
    }

    @Override
    public int updateAddCustomerGold(Customer customer){
        return customerMapper.updateAddCustomerGold(customer);
    }

    @Override
    public int updateAddCustomerCoin(Customer customer){
        return customerMapper.updateAddCustomerCoin(customer);
    }

    @Override
    public int updateAddBusinessGold(Customer customer){
        return customerMapper.updateAddBusinessGold(customer);
    }

    @Override
    public int updateReduceCustomerGold(Customer customer){
        return customerMapper.updateReduceCustomerGold(customer);
    }

    @Override
    public int updateReduceCustomerOilDrill(Customer customer){
        return customerMapper.updateReduceCustomerOilDrill(customer);
    }

    @Override
    public int updateAddCustomerOilDrill(Customer customer){
        return customerMapper.updateAddCustomerOilDrill(customer);
    }

    @Override
    public int updateReduceBusinessGold(Customer customer){
        return customerMapper.updateReduceBusinessGold(customer);
    }

    @Override
    public int updateLoginPassword(Customer customer){
        return customerMapper.updateLoginPassword(customer);
    }

    @Override
    public int updatePaymentPassword(Customer customer){
        return customerMapper.updatePaymentPassword(customer);
    }

    @Override
    public int subtractBusinessGold(TransferParams transferParams) {
        return customerMapper.subtractBusinessGold(transferParams);
    }

    @Override
    public int addCustomerGold(TransferParams transferParams) {
        return customerMapper.addCustomerGold(transferParams);
    }

    @Override
    public int subtractBusinessOilDrill(TransferParams transferParams) {
        return customerMapper.subtractBusinessOilDrill(transferParams);
    }

    @Override
    public int addCustomerOilDrill(TransferParams transferParams) {
        return customerMapper.addCustomerOilDrill(transferParams);
    }

    @Override
    public int deleteById(Integer id){
        return customerMapper.deleteById(id);
    }
}