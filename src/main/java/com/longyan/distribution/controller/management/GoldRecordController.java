package com.longyan.distribution.controller.management;

import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.domain.GoldRecord;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.CustomerAddReduceGoldForm;
import com.longyan.distribution.request.GoldRecordCreateForm;
import com.longyan.distribution.request.GoldRecordListForm;
import com.longyan.distribution.request.GoldRecordUpdateForm;
import com.longyan.distribution.response.CustomerListView;
import com.longyan.distribution.response.GoldRecordListView;
import com.longyan.distribution.service.CustomerService;
import com.longyan.distribution.service.GoldRecordService;
import com.longyan.distribution.service.SystemParamsService;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.rest.view.ResponseView;
import com.sug.core.rest.view.SuccessView;
import com.sug.core.util.BigDecimalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.mail.Session;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

import static com.longyan.distribution.constants.CommonConstants.*;
import static com.longyan.distribution.constants.CustomerConstants.*;
import static com.longyan.distribution.constants.GoldRecordConstans.USERADD;
import static com.longyan.distribution.constants.GoldRecordConstans.USERREDUCE;
import static com.longyan.distribution.constants.SystemParamsConstants.*;

@RestController
@RequestMapping(value = "/management/goldRecord")
@UserLoginRequired
public class GoldRecordController {

    private static final Logger logger = LoggerFactory.getLogger(GoldRecordController.class);

    @Autowired
    private GoldRecordService goldRecordService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SystemParamsService systemParamsService;

    @Autowired
    private SessionContext sessionContext;

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

    //用户增减金币
    @Transactional
    @RequestMapping(value = "/customerAddReduceGold",method = RequestMethod.POST)
    public ResponseView customerAddGoldRecord(@Valid @RequestBody CustomerAddReduceGoldForm form){
        Customer customer = customerService.getById(form.getId());
        int useId= sessionContext.getUser().getId();
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        GoldRecord goldRecord = new GoldRecord();
        //amount用于当前用户充值
        BigDecimal amount = new BigDecimal(form.getAmount());
        goldRecord.setCustomerId(customer.getId());
        goldRecord.setBusinessId(CUSTOMER);
        goldRecord.setBusinessName("");
        goldRecord.setBusinessAccount("");
        goldRecord.setCustomerPhone(customer.getPhone());
        goldRecord.setCreateBy(useId);
        goldRecord.setType(form.getType());
        //判断是增加还是减少，减少不用分红
        if(Objects.equals(form.getType(),USERREDUCE)){
            //判断要减少的金币是否大于用户金币
            if(Objects.equals(customer.getCustomerGold().compareTo(amount),-1)){
                throw new InvalidRequestException("reduceError","The amount of gold to be reduced is greater than the user's gold");
            }
            customer.setCustomerGold(amount);
            customerService.updateReduceCustomerGold(customer);
            //添加减少金币记录
            goldRecord.setAmount(amount.multiply(new BigDecimal(-1)));
            goldRecordService.create(goldRecord);
            return new ResponseView();
        }
        //进行当前用户按等级分配折扣充值增加金币
        if(Objects.equals(customer.getLevel(), CUSTOPMERONELEVEL)){
//            Integer value= Integer.parseInt(systemParamsService.getValueByKey(Collections.singletonMap("key",COMMONGOLDCHARGE)).getValue());
            BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",COMMONGOLDCHARGE)).getValue());
            BigDecimal currentAmount=amount.divide(value, 2, BigDecimal.ROUND_UP);
            customer.setCustomerGold(currentAmount);
            customerService.updateAddCustomerGold(customer);
            //添加增加金币记录
            goldRecord.setAmount(currentAmount);
            goldRecordService.create(goldRecord);
        }
        if(Objects.equals(customer.getLevel(), CUSTOPMERTWOLEVEL)){
            BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",VIPREGOLDCHARGE)).getValue());

//            Integer value= Integer.parseInt(systemParamsService.getValueByKey(Collections.singletonMap("key",VIPREGOLDCHARGE)).getValue());
            BigDecimal currentAmount=amount.divide(value, 2, BigDecimal.ROUND_UP);
            customer.setCustomerGold(currentAmount);
            customerService.updateAddCustomerGold(customer);
            //添加增加金币记录
            goldRecord.setAmount(currentAmount);
            goldRecordService.create(goldRecord);
        }
        //如果当前用户会员等级是最高，充值就不用给其他人分红;
        if(Objects.equals(customer.getLevel(), CUSTOPMERTHREELEVEL)){
            BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",PARTNEGOLDRRECHARGE)).getValue());

//            Integer value= Integer.parseInt(systemParamsService.getValueByKey(Collections.singletonMap("key",PARTNEGOLDRRECHARGE)).getValue());
            BigDecimal currentAmount=amount.divide(value, 2, BigDecimal.ROUND_UP);
            customer.setCustomerGold(currentAmount);
            customerService.updateAddCustomerGold(customer);
            //添加增加金币记录
            goldRecord.setAmount(currentAmount);
            goldRecordService.create(goldRecord);
            return new ResponseView();
        }
        //如果是后台手动添加，不是前台充值，就直接返回,不用给其他人分红
        if(Objects.equals(form.getType(), USERADD)){
            return new ResponseView();
        }
        //判断当前用户是不是普通用户
        if(Objects.equals(customer.getLevel(),CUSTOPMERONELEVEL)){
            //判断是否有上级
            if(!Objects.equals(customer.getParentId(),NOTPARENT)){
                Customer parentCustomer = customerService.getById(customer.getParentId());
                //判断上级等级是不是vip
                if(Objects.equals(parentCustomer.getLevel(), CUSTOPMERTWOLEVEL)){
                    //添加vip分红
                    BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",VIPINVITECOMMONRECHARGEGOLDCOIN)).getValue());
                    BigDecimal parentAmount = BigDecimalUtils.multiply(value,form.getAmount());
                    parentCustomer.setCustomerGold(parentAmount);
                    customerService.updateAddCustomerGold(parentCustomer);
                    //添加分红记录
                    goldRecord.setCustomerId(parentCustomer.getId());
                    goldRecord.setAmount(parentAmount);
                    goldRecord.setCustomerPhone(parentCustomer.getPhone());
                    goldRecordService.create(goldRecord);
                    //判断是否有上上级
                    if(!Objects.equals(customer.getParentId(),NOTSUPERPARENT)){
                        Customer superParentCustomer = customerService.getById(customer.getSuperParentId());
                        //判断上上级是不是合伙人
                        if(Objects.equals(superParentCustomer.getLevel(), CUSTOPMERTHREELEVEL)){
                            //拿出拿出合伙人对vip分红百分比，添加分红
                            BigDecimal partnerRebate =  new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",INVITEVIPRECHARGEGOLDCOIN)).getValue());
                            BigDecimal superParentAmount = BigDecimalUtils.multiply(partnerRebate,form.getAmount());
                            superParentCustomer.setCustomerGold(superParentAmount);
                            customerService.updateAddCustomerGold(superParentCustomer);
                            //添加分红记录
                            goldRecord.setCustomerId(superParentCustomer.getId());
                            goldRecord.setAmount(superParentAmount);
                            goldRecord.setCustomerPhone(superParentCustomer.getPhone());
                            goldRecordService.create(goldRecord);
                        }
                    }
                }
                //判断上级等级是不是合伙人，是合伙人上上级没有分红
                if(Objects.equals(parentCustomer.getLevel(), CUSTOPMERTHREELEVEL)){
                    //计算合伙人对普通用户分红百分比，添加分红
                    BigDecimal vipRebate =  new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",INVITEVIPRECHARGEGOLDCOIN)).getValue());
                    BigDecimal normalRebate = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",VIPINVITECOMMONRECHARGEGOLDCOIN)).getValue());
                    BigDecimal partnerRebate=vipRebate.add(normalRebate);
                    BigDecimal superParentAmount = BigDecimalUtils.multiply(partnerRebate,form.getAmount());
                    parentCustomer.setCustomerGold(superParentAmount);
                    customerService.updateAddCustomerGold(parentCustomer);
                    //添加分红记录
                    goldRecord.setCustomerId(parentCustomer.getId());
                    goldRecord.setAmount(superParentAmount);
                    goldRecord.setCustomerPhone(parentCustomer.getPhone());
                    goldRecordService.create(goldRecord);
                }
            }
        }
        //判断当前用户是不是vip
        if(Objects.equals(customer.getLevel(),CUSTOPMERTWOLEVEL)){
            //判断是否有上级
            if(!Objects.equals(customer.getParentId(),NOTPARENT)){
                Customer parentCustomer = customerService.getById(customer.getParentId());
                //判断上级等级是不是合伙人
                if(Objects.equals(parentCustomer.getLevel(), CUSTOPMERTHREELEVEL)){
                    //拿出合伙人对vip分红百分比，添加分红
                    BigDecimal partnerRebate =  new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",INVITEVIPRECHARGEGOLDCOIN)).getValue());
                    BigDecimal parentAmount = BigDecimalUtils.multiply(partnerRebate,form.getAmount());
                    parentCustomer.setCustomerGold(parentAmount);
                    customerService.updateAddCustomerGold(parentCustomer);
                    //添加分红记录
                    goldRecord.setCustomerId(parentCustomer.getId());
                    goldRecord.setAmount(parentAmount);
                    goldRecord.setCustomerPhone(parentCustomer.getPhone());
                    goldRecordService.create(goldRecord);
                }
            }
        }
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
