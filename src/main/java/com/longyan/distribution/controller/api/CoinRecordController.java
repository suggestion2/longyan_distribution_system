package com.longyan.distribution.controller.api;

import com.longyan.distribution.constants.CoinRecordConstants;
import com.longyan.distribution.constants.OilDrillConstants;
import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.domain.GoldRecord;
import com.longyan.distribution.domain.OilDrillRecord;
import com.longyan.distribution.interceptor.BusinessRequired;
import com.longyan.distribution.interceptor.CustomerLoginRequired;
import com.longyan.distribution.request.*;
import com.longyan.distribution.response.GoldRecordListView;
import com.longyan.distribution.response.OilDrillRecordListView;
import com.longyan.distribution.service.*;
import com.sug.core.platform.crypto.MD5;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.web.pagination.PaginationForm;
import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.rest.view.ResponseView;
import com.sug.core.rest.view.SuccessView;
import com.longyan.distribution.domain.CoinRecord;
import com.longyan.distribution.response.CoinRecordListView;
import com.sug.core.util.BigDecimalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.longyan.distribution.constants.CoinRecordConstants.*;
import static com.longyan.distribution.constants.CommonConstants.*;
import static com.longyan.distribution.constants.GoldRecordConstans.EXCHANGE;
import static com.longyan.distribution.constants.GoldRecordConstans.NOTBUSINESS;
import static com.longyan.distribution.constants.GoldRecordConstans.NOTCUSTOMER;
import static com.longyan.distribution.constants.SystemParamsConstants.COINCHANGEGOLD;
import static com.longyan.distribution.constants.SystemParamsConstants.COINCHANGEOIL;
import static com.longyan.distribution.constants.SystemParamsConstants.VIPINVITECOMMONRECHARGEGOLDCOIN;

@RestController("CoinRecordApiController")
@RequestMapping(value = "/api/coinRecord")
@CustomerLoginRequired
public class CoinRecordController {

    private static final Logger logger = LoggerFactory.getLogger(CoinRecordController.class);

    @Autowired
    private CoinRecordService coinRecordService;

    @Autowired
    private GoldRecordService goldRecordService;

    @Autowired
    private OilDrillRecordService oilDrillRecordService;

    @Autowired
    private SystemParamsService systemParamsService;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = LIST,method = RequestMethod.POST)
    public CoinRecordListView list(@Valid @RequestBody CoinRecordListForm form){
        return new CoinRecordListView(coinRecordService.selectList(form.getQueryMap()));
    }
    //钢镚收支记录
    @RequestMapping(value = "/coinRecordList",method = RequestMethod.POST)
    public CoinRecordListView coinRecordList(@Valid @RequestBody PaginationForm form){
        Map<String,Object> query = form.getQueryMap();
        query.put("customerId",sessionContext.getCustomerId());
        return new CoinRecordListView(coinRecordService.selectList(query),coinRecordService.selectCount(query));
    }

    @RequestMapping(value = "/exchangeGold",method = RequestMethod.PUT)
    @Transactional
    public ResponseView exchangeGold(@Valid @RequestBody CoinRecordExchangeGoldForm form){
        //判断密码
        Customer customer = customerService.getById(sessionContext.getCustomerId());
        if(Objects.isNull(customer) ||
                !MD5.encrypt(form.getPaymentPassword() + MD5_SALT).equalsIgnoreCase(customer.getPaymentPassword())){
            throw new ResourceNotFoundException("用户名没有找到或者密码错误");
        }
        //扣除钢蹦
        customer.setCustomerCoin(form.getAmount());
        if(Objects.equals(customer.getCustomerCoin().compareTo(form.getAmount()),-1)){
            throw new InvalidRequestException("兑换错误","用户钢镚不足");
        }
        int status = customerService.updateReduceCustomerCoin(customer);
        if(Objects.equals(status,REDUCE_FAIL)){
            throw new InvalidRequestException("兑换错误","用户钢镚不足");
        }
        //拿出手续费计算，兑换金币
        BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",COINCHANGEGOLD)).getValue());
        BigDecimal amount = form.getAmount().subtract(BigDecimalUtils.multiply(value,form.getAmount()));
        customer.setCustomerGold(amount);
        customerService.updateAddCustomerGold(customer);
        //兑换记录
        GoldRecord goldRecord = new GoldRecord();
        goldRecord.setCustomerId(customer.getId());
        goldRecord.setBusinessId(NOTBUSINESS);
        goldRecord.setBusinessName("");
        goldRecord.setBusinessAccount("");
        goldRecord.setCustomerPhone(customer.getPhone());
        goldRecord.setCreateBy(CREATE_BY_SERVER);
        goldRecord.setType(EXCHANGE);
        //添加增加金币记录
        goldRecord.setAmount(amount);
        goldRecordService.create(goldRecord);
        //钢蹦记录
        CoinRecord coinRecord = new CoinRecord();
        coinRecord.setCustomerId(customer.getId());
        coinRecord.setSourceCustomerId(NOT_SOURCE_CUSTOMER_LEVEL);
        coinRecord.setSourceCustomerLevel(NOT_SOURCE_CUSTOMERID);
        coinRecord.setSourceCustomerPhone(NOT_SOURCE_CUSTOMER_PHONE);
        coinRecord.setCustomerPhone(customer.getPhone());
        //添加减少钢蹦记录
        coinRecord.setAmount(form.getAmount().multiply(new BigDecimal(-1)));
        coinRecord.setType(EXCHANGE_GOLD);
        coinRecord.setCreateBy(CREATE_BY_SERVER);
        coinRecordService.create(coinRecord);
        return new ResponseView();
    }


    @RequestMapping(value = "/exchangeOilDrill",method = RequestMethod.PUT)
    @Transactional
    public ResponseView exchangeOilDrill(@Valid @RequestBody OilDrillRecordExchangeGoldForm form){
        //判断密码
        Customer customer = sessionContext.getCustomer();
        if(Objects.isNull(customer) ||
                !MD5.encrypt(form.getPaymentPassword() + MD5_SALT).equalsIgnoreCase(customer.getPaymentPassword())){
            throw new ResourceNotFoundException("用户名或者密码错误");
        }
        //扣除钢蹦
        customer.setCustomerCoin(form.getAmount());
        if(Objects.equals(customer.getCustomerCoin().compareTo(form.getAmount()),-1)){
            throw new InvalidRequestException("兑换错误","用户油钻不足");
        }
        int status = customerService.updateReduceCustomerCoin(customer);
        if(Objects.equals(status,REDUCE_FAIL)){
            throw new InvalidRequestException("兑换错误","用户油钻不足");
        }
        //拿出手续费计算，兑换油钻
        BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",COINCHANGEOIL)).getValue());
        BigDecimal amount = form.getAmount().subtract(BigDecimalUtils.multiply(value,form.getAmount()));
        customer.setCustomerOilDrill(amount);
        customerService.updateAddCustomerOilDrill(customer);
        //兑换油钻记录
        OilDrillRecord oilDrillRecord = new OilDrillRecord();
        oilDrillRecord.setCustomerId(customer.getId());
        oilDrillRecord.setBusinessId(OilDrillConstants.NOTBUSINESS);
        oilDrillRecord.setBusinessName("");
        oilDrillRecord.setBusinessAccount("");
        oilDrillRecord.setCustomerPhone(customer.getPhone());
        oilDrillRecord.setCreateBy(CREATE_BY_SERVER);
        oilDrillRecord.setType(OilDrillConstants.EXCHANGE);
        //添加增加油钻记录
        oilDrillRecord.setAmount(amount);
        oilDrillRecordService.create(oilDrillRecord);
        //钢蹦记录
        CoinRecord coinRecord = new CoinRecord();
        coinRecord.setCustomerId(customer.getId());
        coinRecord.setSourceCustomerId(NOT_SOURCE_CUSTOMER_LEVEL);
        coinRecord.setSourceCustomerLevel(NOT_SOURCE_CUSTOMERID);
        coinRecord.setSourceCustomerPhone(NOT_SOURCE_CUSTOMER_PHONE);
        coinRecord.setCustomerPhone(customer.getPhone());
        //添加减少钢蹦记录
        coinRecord.setAmount(form.getAmount().multiply(new BigDecimal(-1)));
        coinRecord.setType(EXCHANGE_OILDRILL);
        coinRecord.setCreateBy(CREATE_BY_SERVER);
        coinRecordService.create(coinRecord);
        return new ResponseView();
    }

    //提现
    @RequestMapping(value = "/coinWithdraw",method = RequestMethod.POST)
    public ResponseView coinWithdraw(@Valid @RequestBody CoinRecordWithdrawForm form){
        Customer customer = sessionContext.getCustomer();
        if(!customer.getPaymentPassword().equalsIgnoreCase(MD5.encrypt(form.getPaymentPassword() + MD5_SALT))){
            throw new InvalidRequestException("密码错误","错误的支付密码");
        }

        Map<String,Object> map = new HashMap<>();
        map.put("status",CoinRecordConstants.WAITCHECK);
        map.put("customerId",customer.getId());
        if(!Objects.equals(coinRecordService.selectList(map).size(), WAITCHECKNUMBER)){
            throw new InvalidRequestException("有提现单待审核","不能再进行提现，先处理当前提现单");
        }
        if(Objects.equals(customer.getCustomerCoin().compareTo(form.getAmount()),-1)){
            throw new InvalidRequestException("钢镚不足","钢镚不足");
        }
        coinRecordService.selectList(map);
        CoinRecord coinRecord = new CoinRecord();
        coinRecord.setCustomerId(customer.getId());
        coinRecord.setSourceCustomerId(NOT_SOURCE_CUSTOMER_LEVEL);
        coinRecord.setSourceCustomerLevel(NOT_SOURCE_CUSTOMERID);
        coinRecord.setSourceCustomerPhone(NOT_SOURCE_CUSTOMER_PHONE);
        coinRecord.setCustomerPhone(customer.getPhone());
        coinRecord.setAmount(form.getAmount().multiply(new BigDecimal(-1)));
        coinRecord.setType(WITHDRAW);
        //设置状态为待审核
        coinRecord.setStatus(WAITCHECK);
        coinRecord.setCreateBy(CREATE_BY_SERVER);
        coinRecordService.create(coinRecord);
        return new ResponseView();
    }


    @RequestMapping(value = DETAIL,method = RequestMethod.GET)
    public CoinRecord detail(@PathVariable Integer id){
        return coinRecordService.getById(id);
    }

    @RequestMapping(value = CREATE,method = RequestMethod.POST)
    public SuccessView create(@Valid @RequestBody CoinRecordCreateForm form){
        CoinRecord coinRecord = new CoinRecord();
        BeanUtils.copyProperties(form,coinRecord);
        coinRecordService.create(coinRecord);
        return new SuccessView();
    }

    @RequestMapping(value = UPDATE,method = RequestMethod.PUT)
    public SuccessView update(@Valid @RequestBody CoinRecordUpdateForm form){
        CoinRecord coinRecord = coinRecordService.getById(form.getId());
        if(Objects.isNull(coinRecord)){
            throw new ResourceNotFoundException("coinRecord not exists");
        }
        BeanUtils.copyProperties(form,coinRecord);
        coinRecordService.update(coinRecord);
        return new SuccessView();
    }
}
