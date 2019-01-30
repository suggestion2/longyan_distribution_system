package com.longyan.distribution.response;

import com.longyan.distribution.domain.Customer;

import java.util.List;

public class CustomerInviteListView {
    private Customer customer;
    private List<Customer> customerList;

    public CustomerInviteListView(Customer customer, List<Customer> customerList) {
        this.customer = customer;
        this.customerList = customerList;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }
}
