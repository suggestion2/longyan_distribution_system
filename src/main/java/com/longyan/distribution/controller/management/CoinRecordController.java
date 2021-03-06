package com.longyan.distribution.controller.management;

import com.longyan.distribution.constants.GoldRecordConstans;
import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.CoinRecord;
import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.domain.GoldRecord;
import com.longyan.distribution.domain.OilDrillRecord;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.*;
import com.longyan.distribution.response.CoinRecordHandleView;
import com.longyan.distribution.response.CoinRecordListView;
import com.longyan.distribution.response.GoldRecordHandleView;
import com.longyan.distribution.response.GoldRecordListView;
import com.longyan.distribution.service.CoinRecordService;
import com.longyan.distribution.service.CustomerService;
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

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.longyan.distribution.constants.CoinRecordConstants.*;
import static com.longyan.distribution.constants.CommonConstants.*;
import static com.longyan.distribution.constants.SystemParamsConstants.BUSINESSGOLDCASH;
import static com.longyan.distribution.constants.SystemParamsConstants.COINCASH;

@RestController("CoinRecordManagementController")
@RequestMapping(value = "/management/coinRecord")
@UserLoginRequired
public class CoinRecordController {

    private static final Logger logger = LoggerFactory.getLogger(CoinRecordController.class);

    @Autowired
    private CoinRecordService coinRecordService;

    @Autowired
    private SystemParamsService systemParamsService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SessionContext sessionContext;

    @RequestMapping(value = "/cashList",method = RequestMethod.POST)
    public CoinRecordListView cashList(@Valid @RequestBody CoinRecordCashListForm form){
        Map<String,Object> map = form.getQueryMap();
        map.put("type",WITHDRAW);
        return new CoinRecordListView(coinRecordService.selectCoinRecordAndCustomerList(map),coinRecordService.selectCount(map));
    }


    //打款展示处理
    @RequestMapping(value = "/moneyHandle",method = RequestMethod.POST)
    public CoinRecordHandleView moneyHandle(@Valid @RequestBody CoinRecordMoneyHandleForm form){
        CoinRecord coinRecord = coinRecordService.getById(form.getId());
        if (Objects.isNull(coinRecord)) {
            throw new ResourceNotFoundException("记录不存在");
        }
        Customer customer = customerService.getById(coinRecord.getCustomerId());
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("用户不存在");
        }
        if(!Objects.equals(coinRecord.getType(),WITHDRAW)) {
            throw new InvalidRequestException("提现状态错误", "提现状态错误");
        }
        //计算手续费，拿出系统参数手续费,减掉手续费
        BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",COINCASH)).getValue());
        BigDecimal handleMoney = BigDecimalUtils.multiply(coinRecord.getAmount(),value);
        BigDecimal cash = coinRecord.getAmount().subtract(handleMoney);
        CoinRecordHandleView coinRecordHandleView = new CoinRecordHandleView();
        BeanUtils.copyProperties(customer, coinRecordHandleView);
        coinRecordHandleView.setApplyCount(coinRecord.getAmount());
        coinRecordHandleView.setHandleMoney(handleMoney);
        coinRecordHandleView.setExchangeCash(cash);
        return coinRecordHandleView;
    }
    //改变状态
    @Transactional
    @RequestMapping(value = "/resetStatus", method = RequestMethod.PUT)
    public ResponseView resetStatus(@Valid @RequestBody CoinRecordStatusForm form) {
        CoinRecord coinRecord = coinRecordService.getById(form.getId());
        if (Objects.isNull(coinRecord)) {
            throw new ResourceNotFoundException("记录不存在");
        }
        //审核通过减少用户钢镚
        if(Objects.equals(form.getStatus(),PASS)&&Objects.equals(coinRecord.getStatus(),WAITCHECK)){
            Customer customer = customerService.getById(coinRecord.getCustomerId());
            if (Objects.isNull(customer)) {
                throw new ResourceNotFoundException("用户没有找到");
            }

            //判断要减少的钢镚会不会大于用户钢镚
            if(Objects.equals(customer.getCustomerCoin().compareTo(coinRecord.getAmount()),-1)){
                throw new InvalidRequestException("钢镚不足","用户钢镚不足");
            }
            coinRecord.setStatus(PASS);
            coinRecordService.updateStatus(coinRecord);
            customer.setCustomerCoin(BigDecimalUtils.multiply(coinRecord.getAmount(),-1));
            int status= customerService.updateReduceCustomerCoin(customer);
            if(Objects.equals(status,REDUCE_FAIL)){
                throw new InvalidRequestException("钢镚不足","用户钢镚不足");
            }
        }
        //审核没通过添加拒绝理由
        if(Objects.equals(form.getStatus(),REFUSE)){
            coinRecord.setStatus(form.getStatus());
            coinRecord.setRefuseReason(form.getRefuseReason());
            coinRecord.setUpdateBy(sessionContext.getUser().getId());
            coinRecordService.updateStatus(coinRecord);
        }
        return new ResponseView();
    }

    @RequestMapping(value = LIST,method = RequestMethod.POST)
    public CoinRecordListView list(@Valid @RequestBody CoinRecordListForm form){
        Map<String,Object> query = form.getQueryMap();
        query.put("customerId",form.getCustomerId());
        return new CoinRecordListView(coinRecordService.selectList(query),coinRecordService.selectCount(query));
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
            throw new ResourceNotFoundException("记录不存在");
        }
        BeanUtils.copyProperties(form,coinRecord);
        coinRecordService.update(coinRecord);
        return new SuccessView();
    }
}
