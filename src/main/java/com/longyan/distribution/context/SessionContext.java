package com.longyan.distribution.context;


import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.domain.User;
import com.longyan.distribution.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Objects;

import static com.longyan.distribution.constants.CustomerConstants.BUSINESS;
import static com.longyan.distribution.constants.CustomerConstants.BUSINESS_ENABLE;

@Component
public class SessionContext {
    private static final long CAPTCHA_EXPIRED_TIME = 120000L;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private CustomerService customerService;

    public void setUser(User user){
        httpSession.setAttribute("user",user);
    }

    public User getUser(){
        return httpSession.getAttribute("user") == null ? null : (User)httpSession.getAttribute("user");
    }
    public void setCustomer(Customer customer){
        httpSession.setAttribute("customerId",customer.getId());
        this.setBusiness(customer);
    }

    public Customer getCustomer(){
        return Objects.isNull(httpSession.getAttribute("customerId")) ? null : customerService.getById((int)httpSession.getAttribute("customerId"));
    }
    public Integer getCustomerId(){
        return httpSession.getAttribute("customerId") == null ? null : (Integer) httpSession.getAttribute("customerId");
    }

    public void setBusiness(Customer customer){
        httpSession.setAttribute("business",customer.getBusiness().equals(BUSINESS) && customer.getBusinessStatus().equals(BUSINESS_ENABLE));
    }

    public boolean isBusiness(){
        return Objects.nonNull(httpSession.getAttribute("business")) && (boolean)httpSession.getAttribute("business");
    }

    public void logout(){
        httpSession.invalidate();
    }
}
