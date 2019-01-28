package com.longyan.distribution.controller.api;

import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.rest.view.ResponseView;
import com.sug.core.rest.view.SuccessView;
import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.service.CustomerService;
import com.longyan.distribution.request.CustomerCreateForm;
import com.longyan.distribution.request.CustomerUpdateForm;
import com.longyan.distribution.request.CustomerListForm;
import com.longyan.distribution.response.CustomerListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Objects;

import static com.longyan.distribution.constants.CommonConstants.*;

@RestController
@RequestMapping(value = "/api/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = LIST,method = RequestMethod.POST)
    public CustomerListView list(@Valid @RequestBody CustomerListForm form){
        return new CustomerListView(customerService.selectList(form.getQueryMap()));
    }

    @RequestMapping(value = DETAIL,method = RequestMethod.GET)
    public Customer detail(@PathVariable Integer id){
        return customerService.getById(id);
    }

    @RequestMapping(value = CREATE,method = RequestMethod.POST)
    public ResponseView create(@Valid @RequestBody CustomerCreateForm form){
        Customer customer = new Customer();
        BeanUtils.copyProperties(form,customer);
        customer.setLevel(1);
        if(Objects.isNull(customer.getParentId())){
            customer.setParentId(0);
        }
        customerService.create(customer);
        return new ResponseView();
    }

    @RequestMapping(value = UPDATE,method = RequestMethod.PUT)
    public SuccessView update(@Valid @RequestBody CustomerUpdateForm form){
        Customer customer = customerService.getById(form.getId());
        if(Objects.isNull(customer)){
            throw new ResourceNotFoundException("customer not exists");
        }
        BeanUtils.copyProperties(form,customer);
        customerService.update(customer);
        return new SuccessView();
    }
}
