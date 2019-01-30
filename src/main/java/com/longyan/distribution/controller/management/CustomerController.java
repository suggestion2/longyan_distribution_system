package com.longyan.distribution.controller.management;

import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.CustomerListForm;
import com.longyan.distribution.request.CustomerManagementlistForm;
import com.longyan.distribution.request.ResetCustomerPasswordForm;
import com.longyan.distribution.response.CustomerListView;
import com.longyan.distribution.service.CustomerService;
import com.sug.core.platform.crypto.MD5;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.rest.view.ResponseView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Map;
import java.util.Objects;

import static com.longyan.distribution.constants.CommonConstants.DETAIL;
import static com.longyan.distribution.constants.CommonConstants.LIST;
import static com.longyan.distribution.constants.CommonConstants.MD5_SALT;
import static com.longyan.distribution.constants.CustomerConstants.*;

@RestController("customerManagementController")
@RequestMapping(value = "/management/customer")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = LIST,method = RequestMethod.POST)
    @UserLoginRequired
    public CustomerListView list(@Valid @RequestBody CustomerManagementlistForm form){
        Map<String,Object> query = form.getQueryMap();
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

    @RequestMapping(value = DETAIL,method = RequestMethod.GET)
    @UserLoginRequired
    public Customer detail(@PathVariable Integer id){
        Customer customer=customerService.getById(id);
        if(Objects.isNull(customer)){
            throw new ResourceNotFoundException("customer not found");
        }
        return customer;
    }

    //重置登陆密码
    @RequestMapping(value = "/loginPassword", method = RequestMethod.PUT)
    @UserLoginRequired
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
    @UserLoginRequired
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
