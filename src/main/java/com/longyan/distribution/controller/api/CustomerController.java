package com.longyan.distribution.controller.api;

import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.User;
import com.longyan.distribution.interceptor.CustomerLoginRequired;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.*;
import com.longyan.distribution.response.*;
import com.sug.core.platform.crypto.MD5;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.rest.view.ResponseView;
import com.sug.core.rest.view.SuccessView;
import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.longyan.distribution.constants.CommonConstants.*;
import static com.longyan.distribution.constants.CustomerConstants.*;
import static com.longyan.distribution.constants.CustomerConstants.INVITETWOLEVEL;

@RestController("customerApiController")
@RequestMapping(value = "/api/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SessionContext sessionContext;

//    @RequestMapping(value = LIST,method = RequestMethod.POST)
//    public CustomerListView list(@Valid @RequestBody CustomerListForm form){
//        return new CustomerListView(customerService.selectList(form.getQueryMap()));
//    }

    @RequestMapping(value = "/inviteList",method = RequestMethod.POST)
    @CustomerLoginRequired
    public CustomerListShortView businessList(@Valid @RequestBody InviteListForm form){
        Map<String,Object> query = form.getQueryMap();
        Customer customer = sessionContext.getCustomer();
        //查找邀请列表
        query.put("parentId",customer.getId());
        query.put("superParentId",customer.getId());
        List<CustomerShortView> list=customerService.selectShortViewList(query);
        //下下级设置type为2
        for(int i =0;i<list.size();i++){
            if(Objects.equals(list.get(i).getSuperParentPhone(),customer.getPhone())){
                list.get(i).setType(INVITETWOLEVEL);
            }else{
                list.get(i).setType(INVITEONELEVEL);
            }
        }
        return new CustomerListShortView(list,customerService.selectCount(query));
    }


    @RequestMapping(value = "/businessList",method = RequestMethod.POST)
    @CustomerLoginRequired
    public BusinessListView businessList(@Valid @RequestBody BusinessListForm form){
        Map<String,Object> query = form.getQueryMap();
        //查找商户
        query.put("business",BUSINESS);
        return new BusinessListView(customerService.selectBusinessList(query),customerService.selectCount(query));
    }

    @RequestMapping(value = DETAIL,method = RequestMethod.GET)
    public Customer detail(@PathVariable Integer id){
        return customerService.getById(id);
    }

    @RequestMapping(value = CREATE,method = RequestMethod.POST)
    public ResponseView create(@Valid @RequestBody CustomerCreateForm form){
        Customer customer = new Customer();
        if(Objects.nonNull(customerService.selectByPhone(form.getPhone()))){
            throw new InvalidRequestException("duplicatePhone","duplicate phone");
        }
        BeanUtils.copyProperties(form,customer);
        customer.setLevel(COMMONLEVEL);
        customer.setLoginPassword(MD5.encrypt(INIT_PASSWORD + MD5_SALT));
        customer.setPaymentPassword(MD5.encrypt(INIT_PASSWORD + MD5_SALT));
        //设置默认为空
        customer.setParentRealName("");
        customer.setSuperParentRealName("");
        customer.setParentPhone("");
        customer.setSuperParentPhone("");
        customer.setSuperParentId(0);
        //判断是否有上级，是否是别人邀请的，添加进记录
        if(Objects.isNull(customer.getParentId())) {
            customer.setParentId(NOTINVITE);
            customerService.create(customer);
            return new ResponseView();
        }
        Customer parentCustomer = customerService.getById(customer.getParentId());
        customer.setParentPhone(parentCustomer.getPhone());
        customer.setParentRealName(parentCustomer.getRealName());
        //是否有上上级，添加进记录
        if(!Objects.equals(parentCustomer.getParentId(),0)) {
            Customer superParentCustomer = customerService.getById(parentCustomer.getParentId());
            customer.setSuperParentId(superParentCustomer.getId());
            customer.setSuperParentPhone(superParentCustomer.getPhone());
            customer.setSuperParentRealName(superParentCustomer.getRealName());
        }
        customerService.create(customer);
        return new ResponseView();
    }

    @RequestMapping(value = LOGIN, method = RequestMethod.POST)
    public ResponseView login(@Valid @RequestBody CustomerLoginForm form) {
        Customer customer = customerService.selectByPhone(form.getPhone());
        if(Objects.isNull(customer) ||
                !MD5.encrypt(form.getLoginPassword() + MD5_SALT).equalsIgnoreCase(customer.getLoginPassword())){
            throw new ResourceNotFoundException("user not found or invalid password");
        }
        sessionContext.setCustomer(customer);
        return new ResponseView();
    }

    @RequestMapping(value = UPDATE,method = RequestMethod.PUT)
    @CustomerLoginRequired
    public ResponseView update(@Valid @RequestBody CustomerUpdateForm form){
        Customer customer = sessionContext.getCustomer();
        BeanUtils.copyProperties(form,customer);
        customerService.update(customer);
        sessionContext.setCustomer(customer);
        return new ResponseView();
    }

    @RequestMapping(value = "businessApplication",method = RequestMethod.PUT)
    @CustomerLoginRequired
    public ResponseView businessApplication(@Valid @RequestBody BusinessApplicationForm form){
        Customer customer = sessionContext.getCustomer();
        BeanUtils.copyProperties(form,customer);
        customer.setBusiness(businessApplication);
        customerService.update(customer);
        sessionContext.setCustomer(customer);
        return new ResponseView();
    }


    @RequestMapping(value = "/loginPassword", method = RequestMethod.PUT)
    @CustomerLoginRequired
    public ResponseView resetLoginPassword(@Valid @RequestBody CustomerLoginPasswordForm form) {
        Customer customer = sessionContext.getCustomer();
        if(!MD5.encrypt(form.getOriginPassword() + MD5_SALT).equalsIgnoreCase(customer.getLoginPassword())){
            throw new InvalidRequestException("invalidPassword","invalid origin password");
        }
        customer.setLoginPassword(MD5.encrypt(form.getNewPassword() + MD5_SALT));
        customerService.updateLoginPassword(customer);
        sessionContext.setCustomer(customer);
        return new ResponseView();
    }

    @RequestMapping(value = "/paymentPassword", method = RequestMethod.PUT)
    @CustomerLoginRequired
    public ResponseView resetPaymentPassword(@Valid @RequestBody CustomerPaymentPasswordForm form) {
        Customer customer = sessionContext.getCustomer();
        if(!MD5.encrypt(form.getOriginPassword() + MD5_SALT).equalsIgnoreCase(customer.getPaymentPassword())){
            throw new InvalidRequestException("invalidPassword","invalid origin password");
        }
        customer.setPaymentPassword(MD5.encrypt(form.getNewPassword() + MD5_SALT));
        customerService.updatePaymentPassword(customer);
        sessionContext.setCustomer(customer);
        return new ResponseView();
    }
    @RequestMapping(value = LOGOUT, method = RequestMethod.GET)
    public ResponseView logout() {
        sessionContext.logout();
        return new ResponseView();
    }

    @RequestMapping(value = "/inviteCode", method = RequestMethod.GET)
    @CustomerLoginRequired
    public CodeView inviteCode() {
        return new CodeView("http://localhost:8110/testTool?id="+sessionContext.getCustomerId());
    }

}
