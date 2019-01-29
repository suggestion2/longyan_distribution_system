package com.longyan.distribution.context;


import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class SessionContext {
    private static final long CAPTCHA_EXPIRED_TIME = 120000L;

    @Autowired
    private HttpSession httpSession;

    public void setUser(User user){
        httpSession.setAttribute("user",user);
    }

    public User getUser(){
        return httpSession.getAttribute("user") == null ? null : (User)httpSession.getAttribute("user");
    }
    public void setCustomer(Customer customer){
        httpSession.setAttribute("customer",customer);
        httpSession.setAttribute("customerId",customer.getId());
    }

    public Customer getCustomer(){
        return httpSession.getAttribute("customer") == null ? null : (Customer)httpSession.getAttribute("customer");
    }
    public Integer getCustomerId(){
        return httpSession.getAttribute("customerId") == null ? null : (Integer) httpSession.getAttribute("customerId");
    }

    public void logout(){
        httpSession.invalidate();
    }
}
