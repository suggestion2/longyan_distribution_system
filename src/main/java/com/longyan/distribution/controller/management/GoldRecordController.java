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
import com.sug.core.rest.view.ResponseView;
import com.sug.core.rest.view.SuccessView;
import com.sug.core.util.BigDecimalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

import static com.longyan.distribution.constants.CommonConstants.*;
import static com.longyan.distribution.constants.CustomerConstants.CUSTOPMERONELEVEL;
import static com.longyan.distribution.constants.CustomerConstants.CUSTOPMERTHREELEVEL;
import static com.longyan.distribution.constants.CustomerConstants.CUSTOPMERTWOLEVEL;
import static com.longyan.distribution.constants.GoldRecordConstans.RECHARGE;
import static com.longyan.distribution.constants.SystemParamsConstants.*;

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

    //用户添加金币
    @RequestMapping(value = "/customerAddGoldRecord",method = RequestMethod.POST)
    public ResponseView customerAddGoldRecord(@Valid @RequestBody CustomerAddGoldRecordForm form){
        Customer customer = customerService.getById(form.getId());
        Map<String,Objects> map = new HashMap<>();
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        //进行当前用户按等级分配折扣充值
        if(Objects.equals(customer.getLevel(), CUSTOPMERONELEVEL)){
            Integer value= Integer.parseInt(systemParamsService.getValueByKey(Collections.singletonMap("key",COMMONGOLDCHARGE)).getValue());
            customer.setCustomerGold(BigDecimalUtils.divide(form.getAmount(), value));
            customerService.updateCustomerGold(customer);
        }
        if(Objects.equals(customer.getLevel(), CUSTOPMERTWOLEVEL)){
            Integer value= Integer.parseInt(systemParamsService.getValueByKey(Collections.singletonMap("key",VIPREGOLDCHARGE)).getValue());
            customer.setCustomerGold(BigDecimalUtils.divide(form.getAmount(), value));
            customerService.updateCustomerGold(customer);
        }
        //如果当前用户会员等级是最高，充值就不用给其他人分红
        if(Objects.equals(customer.getLevel(), CUSTOPMERTWOLEVEL)){
            Integer value= Integer.parseInt(systemParamsService.getValueByKey(Collections.singletonMap("key",PARTNEGOLDRRECHARGE)).getValue());
            customer.setCustomerGold(BigDecimalUtils.divide(form.getAmount(), value));
            customerService.updateCustomerGold(customer);
            return new ResponseView();
        }
        //如果是后台手动添加，不是前台充值，就直接返回,不用给其他人分红
        if(Objects.equals(form.getType(), RECHARGE)){
            return new ResponseView();
        }
        //判断是否有上级
        if(!Objects.equals(customer.getParentId(),0)){
            Customer parentCustomer = customerService.getById(customer.getParentId());
            //判断上级等级是否大于当前用户，大于当前会员,给上级按等级添加充值分红奖励
            if(parentCustomer.getLevel()>customer.getLevel()){
                //上级是vip并且大于当前用户
                if(Objects.equals(parentCustomer.getLevel(), CUSTOPMERTWOLEVEL)){
                    BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",VIPINVITECOMMONRECHARGEGOLDCOIN)).getValue());
                    customer.setId(customer.getParentId());
                    customer.setCustomerGold(BigDecimalUtils.multiply(value,form.getAmount()));
                    customerService.updateCustomerGold(customer);
                }
                //上级是合伙人并且大于当前用户，当前用户等级为普通用户
                if((Objects.equals(parentCustomer.getLevel(), CUSTOPMERTHREELEVEL)&&Objects.equals(customer.getLevel(), CUSTOPMERONELEVEL))){
                    BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",INVITECOMMONRECHARGEGOLDCOIN)).getValue());
                    customer.setId(customer.getParentId());
                    customer.setCustomerGold(BigDecimalUtils.multiply(value,form.getAmount()));
                    customerService.updateCustomerGold(customer);
                //上级是合伙人并且大于当前用户，当前用户等级为vip
                }else{
                    BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",INVITEVIPRECHARGEGOLDCOIN)).getValue());
                    customer.setId(customer.getParentId());
                    customer.setCustomerGold(BigDecimalUtils.multiply(value,form.getAmount()));
                    customerService.updateCustomerGold(customer);
                }


            }
        }

        GoldRecord goldRecord = new GoldRecord();
        BeanUtils.copyProperties(form,goldRecord);
        goldRecordService.create(goldRecord);
        return new ResponseView();
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
