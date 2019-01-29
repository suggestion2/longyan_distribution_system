package com.longyan.distribution.controller.management;

import com.longyan.distribution.request.CustomerListForm;
import com.longyan.distribution.response.CustomerListView;
import com.longyan.distribution.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.Map;

import static com.longyan.distribution.constants.CommonConstants.LIST;
import static com.longyan.distribution.constants.CustomerConstants.CUSTOMER;

@RestController("customerMApiController")
@RequestMapping(value = "/management/customer")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = LIST,method = RequestMethod.POST)
    public CustomerListView list(@Valid @RequestBody CustomerListForm form){
        Map<String,Object> query = form.getQueryMap();
        query.put("business",CUSTOMER);
        return new CustomerListView(customerService.selectList(query),customerService.selectCount(query));
    }
}
