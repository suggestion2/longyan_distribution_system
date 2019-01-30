package com.longyan.distribution.controller.management;

import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.domain.GoldRecord;
import com.longyan.distribution.request.CustomerAddGoldRecordForm;
import com.longyan.distribution.request.GoldRecordCreateForm;
import com.longyan.distribution.request.GoldRecordListForm;
import com.longyan.distribution.request.GoldRecordUpdateForm;
import com.longyan.distribution.response.CustomerListView;
import com.longyan.distribution.response.GoldRecordListView;
import com.longyan.distribution.service.CustomerService;
import com.longyan.distribution.service.GoldRecordService;
import com.longyan.distribution.service.SystemParamsService;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.rest.view.SuccessView;
import com.sug.core.util.BigDecimalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.longyan.distribution.constants.CommonConstants.*;

@RestController
@RequestMapping(value = "management/goldRecord")
public class GoldRecordController {

    private static final Logger logger = LoggerFactory.getLogger(GoldRecordController.class);

    @Autowired
    private GoldRecordService goldRecordService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SystemParamsService systemParamsService;

    @RequestMapping(value = LIST,method = RequestMethod.POST)
    public GoldRecordListView list(@Valid @RequestBody GoldRecordListForm form){
        return new GoldRecordListView(goldRecordService.selectList(form.getQueryMap()));
    }

    @RequestMapping(value = DETAIL,method = RequestMethod.GET)
    public GoldRecord detail(@PathVariable Integer id){
        return goldRecordService.getById(id);
    }

    @RequestMapping(value = CREATE,method = RequestMethod.POST)
    public SuccessView create(@Valid @RequestBody GoldRecordCreateForm form){
        GoldRecord goldRecord = new GoldRecord();
        BeanUtils.copyProperties(form,goldRecord);
        goldRecordService.create(goldRecord);
        return new SuccessView();
    }

    @RequestMapping(value = "/customerAddGoldRecord",method = RequestMethod.POST)
    public SuccessView customerAddGoldRecord(@Valid @RequestBody CustomerAddGoldRecordForm form){
        Customer customer = customerService.getById(form.getId());
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        customerService.updateCustomerGold(form);
        //判断是否有上级
        if(!Objects.equals(customer.getParentId(),0)){
            Customer parentCustomer = customerService.getById(customer.getParentId());
            //判断上级等级是否大于当前会员，大于当前会员,给上级添加充值奖励
            if(parentCustomer.getLevel()>customer.getLevel()){
//                BigDecimalUtils.multiply(form.getAmount(),systemParamsService.getById(1))

            }
        }

        GoldRecord goldRecord = new GoldRecord();
        BeanUtils.copyProperties(form,goldRecord);
        goldRecordService.create(goldRecord);
        return new SuccessView();
    }

    @RequestMapping(value = UPDATE,method = RequestMethod.PUT)
    public SuccessView update(@Valid @RequestBody GoldRecordUpdateForm form){
        GoldRecord goldRecord = goldRecordService.getById(form.getId());
        if(Objects.isNull(goldRecord)){
            throw new ResourceNotFoundException("goldRecord not exists");
        }
        BeanUtils.copyProperties(form,goldRecord);
        goldRecordService.update(goldRecord);
        return new SuccessView();
    }
}
