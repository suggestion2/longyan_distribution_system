package com.longyan.distribution.controller.api;

import com.longyan.distribution.constants.OilDrillConstants;
import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.domain.GoldRecord;
import com.longyan.distribution.domain.OilDrillRecord;
import com.longyan.distribution.interceptor.BusinessRequired;
import com.longyan.distribution.interceptor.CustomerLoginRequired;
import com.longyan.distribution.mapper.TransferParams;
import com.longyan.distribution.request.*;
import com.longyan.distribution.response.*;
import com.longyan.distribution.service.CustomerService;
import com.longyan.distribution.service.GoldRecordService;
import com.longyan.distribution.service.OilDrillRecordService;
import com.sug.core.platform.crypto.MD5;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.web.pagination.PaginationForm;
import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.rest.view.ResponseView;
import com.sug.core.util.BigDecimalUtils;
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
import static com.longyan.distribution.constants.GoldRecordConstans.*;

@RestController("businessApiController")
@RequestMapping(value = "/api/business")
public class BusinessController {

    private static final Logger logger = LoggerFactory.getLogger(BusinessController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private GoldRecordService goldRecordService;

    @Autowired
    private OilDrillRecordService oilDrillRecordService;

    @Autowired
    private SessionContext sessionContext;

    @RequestMapping(value = LOGIN, method = RequestMethod.POST)
    public ResponseView login(@Valid @RequestBody CustomerLoginForm form) {
        Customer customer = customerService.selectByPhone(form.getPhone());
        if(Objects.isNull(customer) || !customer.getBusiness().equals(BUSINESS) ||
                !MD5.encrypt(form.getLoginPassword() + MD5_SALT).equalsIgnoreCase(customer.getLoginPassword())){
            throw new ResourceNotFoundException("user not found or invalid password");
        }
        sessionContext.setCustomer(customer);
        return new ResponseView();
    }

    @RequestMapping(value = CURRENT,method = RequestMethod.GET)
    @BusinessRequired
    public Customer detail(){
        return sessionContext.getCustomer();
    }

    @RequestMapping(value = UPDATE,method = RequestMethod.PUT)
    @BusinessRequired
    public ResponseView update(@Valid @RequestBody BusinessUpdateForm form){
        Customer customer = sessionContext.getCustomer();
        BeanUtils.copyProperties(form,customer);
        customerService.update(customer);
        return new ResponseView();
    }

    @RequestMapping(value = "/customerList",method = RequestMethod.POST)
    @BusinessRequired
    public CustomerListShortView customerList(@Valid @RequestBody CustomerListForm form){
        Map<String,Object> query = form.getQueryMap();
        query.remove("type");
        query.remove("customerContent");

        return new CustomerListShortView(customerService.selectShortViewList(query),customerService.selectCount(query));
    }

    @RequestMapping(value = "/goldRecordList",method = RequestMethod.POST)
    @BusinessRequired
    public GoldRecordListView goldRecordList(@Valid @RequestBody PaginationForm form){
        Map<String,Object> query = form.getQueryMap();
        query.put("businessId",sessionContext.getCustomerId());
        List<GoldRecord> list = goldRecordService.selectList(query);
        for (GoldRecord record:list){
            if(record.getType().equals(TRANSFER)){
                record.setAmount(BigDecimalUtils.multiply(record.getAmount(),-1));
            }
        }
        return new GoldRecordListView(list,goldRecordService.selectCount(query));
    }

    @RequestMapping(value = "/goldWithdrawList",method = RequestMethod.POST)
    @BusinessRequired
    public GoldRecordListView goldWithdrawList(@Valid @RequestBody PaginationForm form){
        Map<String,Object> query = form.getQueryMap();
        query.put("businessId",sessionContext.getCustomerId());
        query.put("type",WITHDRAW);
        return new GoldRecordListView(goldRecordService.selectList(query),goldRecordService.selectCount(query));
    }

    @RequestMapping(value = "/goldWithdraw",method = RequestMethod.POST)
    @BusinessRequired
    public ResponseView goldWithdraw(@Valid @RequestBody WithdrawForm form){
        Customer customer = sessionContext.getCustomer();
        if(!customer.getPaymentPassword().equalsIgnoreCase(MD5.encrypt(form.getPaymentPassword() + MD5_SALT))){
            throw new InvalidRequestException("invalidPassword","invalid payment password");
        }

        if(customer.getBusinessGold().compareTo(form.getAmount()) < 0 ){
            throw new InvalidRequestException("invalidAmount","insufficient Balance");
        }

        GoldRecord goldRecord = new GoldRecord();
        goldRecord.setBusinessId(customer.getId());
        goldRecord.setCustomerId(WITHDRAW_CUSTOMERID);
        goldRecord.setAmount(form.getAmount());
        goldRecord.setType(WITHDRAW);
        goldRecord.setStatus(WITHDRAW_CREATE_STATUS);
        goldRecord.setBusinessAccount(customer.getBusinessAccount());
        goldRecord.setBusinessName(customer.getBusinessName());
        goldRecord.setCustomerPhone(customer.getPhone());
        goldRecord.setCreateBy(CREATE_BY_SERVER);

        goldRecordService.create(goldRecord);

        return new ResponseView();
    }

    @RequestMapping(value = "/goldTransfer",method = RequestMethod.POST)
    @BusinessRequired
    @Transactional
    public ResponseView goldTransfer(@Valid @RequestBody TransferForm form){
        Customer customer = sessionContext.getCustomer();
        if(!customer.getPaymentPassword().equalsIgnoreCase(MD5.encrypt(form.getPaymentPassword() + MD5_SALT))){
            throw new InvalidRequestException("invalidPassword","invalid payment password");
        }

        Customer target = customerService.getById(form.getCustomerId());
        if(Objects.isNull(target)){
            throw new ResourceNotFoundException("customer not found");
        }

        if(target.getId().equals(customer.getId())){
            throw new InvalidRequestException("invalidTarget","can not transfer to yourself");
        }

        if(customer.getBusinessGold().compareTo(form.getAmount()) < 0 ){
            throw new InvalidRequestException("invalidAmount","insufficient Balance");
        }

        GoldRecord goldRecord = new GoldRecord();
        goldRecord.setBusinessId(customer.getId());
        goldRecord.setCustomerId(target.getId());
        goldRecord.setAmount(form.getAmount());
        goldRecord.setType(TRANSFER);
        goldRecord.setStatus(NORMAL_TYPE_DEFAULT_STATUS);
        goldRecord.setBusinessAccount(customer.getBusinessAccount());
        goldRecord.setBusinessName(customer.getBusinessName());
        goldRecord.setCustomerPhone(target.getPhone());
        goldRecord.setCreateBy(CREATE_BY_SERVER);

        goldRecordService.create(goldRecord);

        TransferParams params = new TransferParams(customer.getId(),form.getAmount());

        if(customerService.subtractBusinessGold(params) == 0){
            throw new InvalidRequestException("invalidAmount","insufficient Balance");
        }

        params.setId(target.getId());

        customerService.addCustomerGold(params);

        return new ResponseView();
    }

    @RequestMapping(value = "/oilDrillRecordList",method = RequestMethod.POST)
    @BusinessRequired
    public OilDrillRecordListView oilDrillRecordList(@Valid @RequestBody PaginationForm form){
        Map<String,Object> query = form.getQueryMap();
        query.put("businessId",sessionContext.getCustomerId());
        List<OilDrillRecord> list = oilDrillRecordService.selectList(query);
        for (OilDrillRecord record:list){
            if(record.getType().equals(TRANSFER)){
                record.setAmount(BigDecimalUtils.multiply(record.getAmount(),-1));
            }
        }
        return new OilDrillRecordListView(list,oilDrillRecordService.selectCount(query));
    }

    @RequestMapping(value = "/oilDrillWithdrawList",method = RequestMethod.POST)
    @BusinessRequired
    public OilDrillRecordListView oilDrillWithdrawList(@Valid @RequestBody PaginationForm form){
        Map<String,Object> query = form.getQueryMap();
        query.put("businessId",sessionContext.getCustomerId());
        query.put("type", OilDrillConstants.WITHDRAW);
        return new OilDrillRecordListView(oilDrillRecordService.selectList(query),oilDrillRecordService.selectCount(query));
    }

    @RequestMapping(value = "/oilDrillWithdraw",method = RequestMethod.POST)
    @BusinessRequired
    public ResponseView oilDrillWithdraw(@Valid @RequestBody WithdrawForm form){
        Customer customer = sessionContext.getCustomer();
        if(!customer.getPaymentPassword().equalsIgnoreCase(MD5.encrypt(form.getPaymentPassword() + MD5_SALT))){
            throw new InvalidRequestException("invalidPassword","invalid payment password");
        }

        if(customer.getBusinessOilDrill().compareTo(form.getAmount()) < 0 ){
            throw new InvalidRequestException("invalidAmount","insufficient Balance");
        }

        OilDrillRecord oilDrillRecord = new OilDrillRecord();
        oilDrillRecord.setBusinessId(customer.getId());
        oilDrillRecord.setCustomerId(OilDrillConstants.WITHDRAW_CUSTOMERID);
        oilDrillRecord.setAmount(form.getAmount());
        oilDrillRecord.setType(OilDrillConstants.WITHDRAW);
        oilDrillRecord.setStatus(OilDrillConstants.WITHDRAW_CREATE_STATUS);
        oilDrillRecord.setBusinessAccount(customer.getBusinessAccount());
        oilDrillRecord.setBusinessName(customer.getBusinessName());
        oilDrillRecord.setCustomerPhone(customer.getPhone());
        oilDrillRecord.setCreateBy(CREATE_BY_SERVER);

        oilDrillRecordService.create(oilDrillRecord);

        return new ResponseView();
    }

    @RequestMapping(value = "/oilDrillTransfer",method = RequestMethod.POST)
    @BusinessRequired
    @Transactional
    public ResponseView oilDrillTransfer(@Valid @RequestBody TransferForm form){
        Customer customer = sessionContext.getCustomer();
        if(!customer.getPaymentPassword().equalsIgnoreCase(MD5.encrypt(form.getPaymentPassword() + MD5_SALT))){
            throw new InvalidRequestException("invalidPassword","invalid payment password");
        }

        Customer target = customerService.getById(form.getCustomerId());
        if(Objects.isNull(target)){
            throw new ResourceNotFoundException("customer not found");
        }

        if(target.getId().equals(customer.getId())){
            throw new InvalidRequestException("invalidTarget","can not transfer to yourself");
        }

        if(customer.getBusinessOilDrill().compareTo(form.getAmount()) < 0 ){
            throw new InvalidRequestException("invalidAmount","insufficient Balance");
        }

        OilDrillRecord oilDrillRecord = new OilDrillRecord();
        oilDrillRecord.setBusinessId(customer.getId());
        oilDrillRecord.setCustomerId(target.getId());
        oilDrillRecord.setAmount(form.getAmount());
        oilDrillRecord.setType(OilDrillConstants.TRANSFER);
        oilDrillRecord.setStatus(OilDrillConstants.NORMAL_TYPE_DEFAULT_STATUS);
        oilDrillRecord.setBusinessAccount(customer.getBusinessAccount());
        oilDrillRecord.setBusinessName(customer.getBusinessName());
        oilDrillRecord.setCustomerPhone(target.getPhone());
        oilDrillRecord.setCreateBy(CREATE_BY_SERVER);

        oilDrillRecordService.create(oilDrillRecord);

        TransferParams params = new TransferParams(customer.getId(),form.getAmount());

        if(customerService.subtractBusinessOilDrill(params) == 0){
            throw new InvalidRequestException("invalidAmount","insufficient Balance");
        }

        params.setId(target.getId());

        customerService.addCustomerOilDrill(params);

        return new ResponseView();
    }
}
