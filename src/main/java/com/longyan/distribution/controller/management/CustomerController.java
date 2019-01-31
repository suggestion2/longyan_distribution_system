package com.longyan.distribution.controller.management;

import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.interceptor.CustomerLoginRequired;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.*;
import com.longyan.distribution.response.CustomerInviteListView;
import com.longyan.distribution.response.CustomerListShortView;
import com.longyan.distribution.response.CustomerListView;
import com.longyan.distribution.response.CustomerShortView;
import com.longyan.distribution.service.CustomerService;
import com.sug.core.platform.crypto.MD5;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.rest.view.ResponseView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.longyan.distribution.constants.CommonConstants.DETAIL;
import static com.longyan.distribution.constants.CommonConstants.LIST;
import static com.longyan.distribution.constants.CommonConstants.MD5_SALT;
import static com.longyan.distribution.constants.CustomerConstants.*;

@RestController("customerManagementController")
@RequestMapping(value = "/management/customer")
@UserLoginRequired
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SessionContext sessionContext;


    @RequestMapping(value = LIST,method = RequestMethod.POST)
    public CustomerListView list(@Valid @RequestBody CustomerManagementlistForm form){
        Map<String,Object> query = form.getQueryMap();
        //判断是商户，用户，申请商户状态
        if(Objects.equals(form.getType(),CUSTOMER)){
            query.put("business",CUSTOMER);
        }
        if(Objects.equals(form.getType(),BUSINESS)){
            query.put("business",BUSINESS);
        }
        if(Objects.equals(form.getType(),APPLYBUSINESS)){
            query.put("business",APPLYBUSINESS);
        }
        return new CustomerListView(customerService.selectList(query),customerService.selectCount(query));
    }

    //邀请关系上级
    @RequestMapping(value = "/customerParent/{id}",method = RequestMethod.GET)
    public Customer customerList(@PathVariable Integer id){
        Customer customer = customerService.getById(id);
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        return customerService.getById(customer.getParentId());
    }

    //邀请下级表
    @RequestMapping(value = "/customerLowList",method = RequestMethod.POST)
    public CustomerListView customerList(@Valid @RequestBody InviteCustomerListForm form){
        Customer customer = customerService.getById(form.getId());
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        Map<String,Object> query = Collections.singletonMap("parentId",form.getId());
        return new CustomerListView(customerService.selectList(query),customerService.selectCount(query));
    }

    //启用/禁用 商户
    @RequestMapping(value = "/resetBusinessStatus", method = RequestMethod.PUT)
    public ResponseView resetBusinessStatus(@Valid @RequestBody CustomerBusinessStatusForm form) {
        Customer customer = customerService.getById(form.getId());
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        //先判断是不是商户
        if(Objects.equals(customer.getBusiness(),CUSTOMER)){
            throw new InvalidRequestException("invalidStatus","customer not a business");
        }
        if (!form.getBusinessStatus().equals(ENABLE) && !form.getBusinessStatus().equals(DISABLE)){
            throw new InvalidRequestException("invalidStatus","invalid status");
        }
        if (!customer.getBusinessStatus().equals(form.getBusinessStatus())){
            customer.setBusinessStatus(form.getBusinessStatus());
            customer.setUpdateBy(sessionContext.getUser().getId());
            customerService.update(customer);
        }
        return new ResponseView();
    }

    //设置等级
    @RequestMapping(value = "/resetLevel", method = RequestMethod.PUT)
    public ResponseView resetLevel(@Valid @RequestBody CustomerManagementLevelForm form) {
        Customer customer = customerService.getById(form.getId());
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        if (!form.getLevel().equals(CUSTOPMERONELEVEL) && !form.getLevel().equals(CUSTOPMERTWOLEVEL)&& !form.getLevel().equals(CUSTOPMERTHREELEVEL)){
            throw new InvalidRequestException("invalidStatus","invalid status");
        }
        if (!customer.getLevel().equals(form.getLevel())){
            customer.setLevel(form.getLevel());
            customer.setUpdateBy(sessionContext.getUser().getId());
            customerService.updateLevel(customer);
        }
        return new ResponseView();
    }
    //设为商户
    @RequestMapping(value = "/resetBusiness", method = RequestMethod.PUT)
    public ResponseView resetBusiness(@Valid @RequestBody CustomerBusinessForm form) {
        Customer customer = customerService.getById(form.getId());
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        customer.setBusinessName(form.getBusinessName());
        customer.setBusiness(BUSINESS);
        customer.setUpdateBy(sessionContext.getUser().getId());
        customerService.updateBusiness(customer);
        return new ResponseView();
    }

    @RequestMapping(value = DETAIL,method = RequestMethod.GET)
    public Customer detail(@PathVariable Integer id){
        Customer customer=customerService.getById(id);
        if(Objects.isNull(customer)){
            throw new ResourceNotFoundException("customer not found");
        }
        return customer;
    }

    //重置登陆密码
    @RequestMapping(value = "/loginPassword", method = RequestMethod.PUT)
    public ResponseView resetLoginPassword(@Valid @RequestBody ResetCustomerPasswordForm form) {
        Customer customer = customerService.getById(form.getId());
        if(Objects.isNull(customer)){
            throw new ResourceNotFoundException("customer not exists");
        }
        customer.setLoginPassword(MD5.encrypt(INIT_PASSWORD + MD5_SALT));
        customerService.updateLoginPassword(customer);
        return new ResponseView();
    }
    //重置支付密码
    @RequestMapping(value = "/paymentPassword", method = RequestMethod.PUT)
    public ResponseView resetPaymentPassword(@Valid @RequestBody ResetCustomerPasswordForm form) {
        Customer customer = customerService.getById(form.getId());
        if(Objects.isNull(customer)){
            throw new ResourceNotFoundException("customer not exists");
        }
        customer.setPaymentPassword(MD5.encrypt(INIT_PASSWORD + MD5_SALT));
        customerService.updatePaymentPassword(customer);
        return new ResponseView();
    }
}
