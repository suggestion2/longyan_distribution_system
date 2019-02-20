package com.longyan.distribution.controller.api;

import com.longyan.distribution.constants.OilDrillConstants;
import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.GoldRecord;
import com.longyan.distribution.domain.OilDrillRecord;
import com.longyan.distribution.domain.User;
import com.longyan.distribution.interceptor.BusinessRequired;
import com.longyan.distribution.interceptor.CustomerLoginRequired;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.mapper.TransferParams;
import com.longyan.distribution.request.*;
import com.longyan.distribution.response.*;
import com.longyan.distribution.service.GoldRecordService;
import com.longyan.distribution.service.OilDrillRecordService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.longyan.distribution.constants.CommonConstants.*;
import static com.longyan.distribution.constants.CustomerConstants.*;
import static com.longyan.distribution.constants.CustomerConstants.INVITETWOLEVEL;
import static com.longyan.distribution.constants.GoldRecordConstans.NORMAL_TYPE_DEFAULT_STATUS;
import static com.longyan.distribution.constants.GoldRecordConstans.TRANSFER;

@RestController("customerApiController")
@RequestMapping(value = "/api/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OilDrillRecordService oilDrillRecordService;

    @Autowired
    private GoldRecordService goldRecordService;

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

    @RequestMapping(value = DETAIL, method = RequestMethod.GET)
    public Customer detail(@PathVariable Integer id) {
        Customer customer = customerService.selectBusinessById(id);
        if (Objects.isNull(customer)||!Objects.equals(customer.getBusiness(), BUSINESS)) {
            throw new ResourceNotFoundException("business not found");
        }
        return customer;
    }

    @RequestMapping(value = "/businessList",method = RequestMethod.POST)
    @CustomerLoginRequired
    public BusinessListView businessList(@Valid @RequestBody BusinessListForm form){
        Map<String,Object> query = form.getQueryMap();
        //查找商户
        query.put("business",BUSINESS);
        query.put("businessStatus",BUSINESS_ENABLE);
        return new BusinessListView(customerService.selectBusinessList(query),customerService.selectCount(query));
    }

    @RequestMapping(value = CURRENT,method = RequestMethod.GET)
    @CustomerLoginRequired
    public Customer current(){
        return sessionContext.getCustomer();
    }

    @RequestMapping(value = CREATE,method = RequestMethod.POST)
    public ResponseView create(@Valid @RequestBody CustomerCreateForm form){
        Customer customer = new Customer();
        if(Objects.nonNull(customerService.selectByPhone(form.getPhone()))){
            throw new InvalidRequestException("手机号码重复","错误的手机号码");
        }
        BeanUtils.copyProperties(form,customer);
        customer.setLevel(COMMONLEVEL);
        customer.setLoginPassword(MD5.encrypt(form.getLoginPassword() + MD5_SALT));
        customer.setPaymentPassword(MD5.encrypt(form.getPaymentPassword() + MD5_SALT));
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
            throw new ResourceNotFoundException("用户没有找到或密码错误");
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
        //如果不是合伙人不让申请成为商户
        if(!Objects.equals(customer.getLevel(),CUSTOPMERTHREELEVEL)){
            throw new InvalidRequestException("invalidLevel","invalid level");
        }
        if(!Objects.equals(customer.getBusiness(),CUSTOMER)){
            throw new InvalidRequestException("invalidBusinessStatus","invalid business status");
        }
        BeanUtils.copyProperties(form,customer);
        customer.setBusiness(BUSINESSAPPLICATION);
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

    //金币转账
    @RequestMapping(value = "/goldTransfer",method = RequestMethod.POST)
    @CustomerLoginRequired
    @Transactional
    public ResponseView goldTransfer(@Valid @RequestBody CustomerTransferForm form){
        Customer customer = sessionContext.getCustomer();
        if(!customer.getPaymentPassword().equalsIgnoreCase(MD5.encrypt(form.getPaymentPassword() + MD5_SALT))){
            throw new InvalidRequestException("invalidPassword","invalid payment password");
        }

        Customer target = customerService.getById(form.getBusinessId());
        if(Objects.isNull(target)){
            throw new ResourceNotFoundException("customer not found");
        }

        if(target.getId().equals(customer.getId())){
            throw new InvalidRequestException("invalidTarget","can not transfer to yourself");
        }

        if(customer.getCustomerGold().compareTo(form.getAmount()) < 0 ){
            throw new InvalidRequestException("invalidAmount","insufficient Balance");
        }

        GoldRecord goldRecord = new GoldRecord();
        goldRecord.setBusinessId(target.getId());
        goldRecord.setCustomerId(customer.getId());
        //减少是负数
        goldRecord.setAmount(form.getAmount().multiply(new BigDecimal(-1)));
        goldRecord.setType(TRANSFER);
        goldRecord.setStatus(NORMAL_TYPE_DEFAULT_STATUS);
        goldRecord.setBusinessAccount(target.getBusinessAccount());
        goldRecord.setBusinessName(target.getBusinessName());
        goldRecord.setCustomerPhone(customer.getPhone());
        goldRecord.setCreateBy(CREATE_BY_SERVER);

        goldRecordService.create(goldRecord);

        TransferParams params = new TransferParams(customer.getId(),form.getAmount());

        if(customerService.subtractCustomerGold(params) == 0){
            throw new InvalidRequestException("invalidAmount","insufficient Balance");
        }

        params.setId(target.getId());

        customerService.addBusinessGold(params);

        return new ResponseView();
    }

    //油钻转账
    @RequestMapping(value = "/oilDrillTransfer",method = RequestMethod.POST)
    @Transactional
    @CustomerLoginRequired
    public ResponseView oilDrillTransfer(@Valid @RequestBody CustomerTransferForm form){
        Customer customer = sessionContext.getCustomer();
        if(!customer.getPaymentPassword().equalsIgnoreCase(MD5.encrypt(form.getPaymentPassword() + MD5_SALT))){
            throw new InvalidRequestException("invalidPassword","invalid payment password");
        }

        Customer target = customerService.getById(form.getBusinessId());
        if(Objects.isNull(target)){
            throw new ResourceNotFoundException("customer not found");
        }
        //不能自己给自己添加
        if(target.getId().equals(customer.getId())){
            throw new InvalidRequestException("invalidTarget","can not transfer to yourself");
        }

        if(customer.getCustomerOilDrill().compareTo(form.getAmount()) < 0 ){
            throw new InvalidRequestException("invalidAmount","insufficient Balance");
        }

        OilDrillRecord oilDrillRecord = new OilDrillRecord();
        oilDrillRecord.setBusinessId(target.getId());
        oilDrillRecord.setCustomerId(customer.getId());
        //转账添加负数
        oilDrillRecord.setAmount(form.getAmount().multiply(new BigDecimal(-1)));
        oilDrillRecord.setType(OilDrillConstants.TRANSFER);
        oilDrillRecord.setStatus(OilDrillConstants.NORMAL_TYPE_DEFAULT_STATUS);
        oilDrillRecord.setBusinessAccount(target.getBusinessAccount());
        oilDrillRecord.setBusinessName(target.getBusinessName());
        oilDrillRecord.setCustomerPhone(customer.getPhone());
        oilDrillRecord.setCreateBy(CREATE_BY_SERVER);

        oilDrillRecordService.create(oilDrillRecord);

        TransferParams params = new TransferParams(customer.getId(),form.getAmount());

        if(customerService.subtractCustomerOilDrill(params) == 0){
            throw new InvalidRequestException("invalidAmount","insufficient Balance");
        }

        params.setId(target.getId());

        customerService.addBusinessOilDrill(params);

        return new ResponseView();
    }
}
