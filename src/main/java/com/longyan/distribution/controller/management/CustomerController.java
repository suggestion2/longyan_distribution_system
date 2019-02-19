package com.longyan.distribution.controller.management;

import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.CoinRecord;
import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.domain.Goods;
import com.longyan.distribution.interceptor.CustomerLoginRequired;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.*;
import com.longyan.distribution.response.*;
import com.longyan.distribution.service.CoinRecordService;
import com.longyan.distribution.service.CustomerService;
import com.longyan.distribution.service.GoodsService;
import com.longyan.distribution.service.SystemParamsService;
import com.sug.core.platform.crypto.MD5;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.rest.view.ResponseView;
import com.sug.core.util.BigDecimalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.math.BigDecimal;
import java.util.*;

import static com.longyan.distribution.constants.CoinRecordConstants.INVITEREWARD;
import static com.longyan.distribution.constants.CoinRecordConstants.RECHARGEREWARD;
import static com.longyan.distribution.constants.CommonConstants.DETAIL;
import static com.longyan.distribution.constants.CommonConstants.LIST;
import static com.longyan.distribution.constants.CommonConstants.MD5_SALT;
import static com.longyan.distribution.constants.CustomerConstants.*;
import static com.longyan.distribution.constants.GoodsConstants.VIPCARD;
import static com.longyan.distribution.constants.SystemParamsConstants.*;

@RestController("customerManagementController")
@RequestMapping(value = "/management/customer")
@UserLoginRequired
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SystemParamsService systemParamsService;

    @Autowired
    private CoinRecordService coinRecordService;

    @RequestMapping(value = LIST, method = RequestMethod.POST)
    public CustomerListView list(@Valid @RequestBody CustomerManagementlistForm form) {
        Map<String, Object> query = form.getQueryMap();
        //判断是商户，用户，申请商户状态
        if (Objects.equals(form.getType(), CUSTOMER)) {
            query.put("business", CUSTOMER);
        }
        if (Objects.equals(form.getType(), BUSINESS)) {
            query.put("business", BUSINESS);
        }
        if (Objects.equals(form.getType(), APPLYBUSINESS)) {
            query.put("business", APPLYBUSINESS);
        }
        return new CustomerListView(customerService.selectList(query), customerService.selectCount(query));
    }

    //邀请关系上级
    @RequestMapping(value = "/customerParent/{id}", method = RequestMethod.GET)
    public List<Customer> customerList(@PathVariable Integer id) {
        Customer customer = customerService.getById(id);
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
//        if (Objects.isNull(customer.getParentId()) || customer.getParentId().equals(NOTINVITE)) {
//            return null;
//        }
        List<Customer> list = new ArrayList<>();
//        CustomerParentView customerParentView = new CustomerParentView();
//        customerParentView.setCustomer(customerService.getById(customer.getParentId()));
        list.add(customerService.getById(customer.getParentId()));
        return list;
    }

    //邀请下级表
    @RequestMapping(value = "/customerLowList", method = RequestMethod.POST)
    public CustomerListView customerList(@Valid @RequestBody InviteCustomerListForm form) {
        Customer customer = customerService.getById(form.getId());
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        Map<String, Object> query = Collections.singletonMap("parentId", form.getId());
        return new CustomerListView(customerService.selectList(query), customerService.selectCount(query));
    }

    //启用/禁用 商户
    @RequestMapping(value = "/resetBusinessStatus", method = RequestMethod.PUT)
    public ResponseView resetBusinessStatus(@Valid @RequestBody CustomerBusinessStatusForm form) {
        Customer customer = customerService.getById(form.getId());
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        //先判断是不是商户
        if (Objects.equals(customer.getBusiness(), CUSTOMER)) {
            throw new InvalidRequestException("invalidStatus", "customer not a business");
        }
        if (!form.getBusinessStatus().equals(BUSINESS_ENABLE) && !form.getBusinessStatus().equals(BUSINESS_DISABLE)) {
            throw new InvalidRequestException("invalidStatus", "invalid status");
        }
        if (!customer.getBusinessStatus().equals(form.getBusinessStatus())) {
            customer.setBusinessStatus(form.getBusinessStatus());
            customer.setUpdateBy(sessionContext.getUser().getId());
            customerService.update(customer);
        }
        return new ResponseView();
    }

    //设置等级
    @RequestMapping(value = "/resetLevel", method = RequestMethod.PUT)
    public ResponseView resetLevel(@Valid @RequestBody CustomerManagementLevelForm form) {
        Customer customer = customerService.getById(form.getId());
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        if (!form.getLevel().equals(CUSTOPMERONELEVEL) && !form.getLevel().equals(CUSTOPMERTWOLEVEL) && !form.getLevel().equals(CUSTOPMERTHREELEVEL)) {
            throw new InvalidRequestException("invalidStatus", "invalid status");
        }
        if (!customer.getLevel().equals(form.getLevel())) {
            customer.setLevel(form.getLevel());
            customer.setUpdateBy(sessionContext.getUser().getId());
            customerService.updateLevel(customer);
        }
        return new ResponseView();
    }

    //升级vip
    @Transactional
    @RequestMapping(value = "/becomeVip", method = RequestMethod.PUT)
    public ResponseView becomeVip(@Valid @RequestBody CustomerBecomeVipForm form) {
        Customer customer = customerService.getById(form.getId());
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        if (customer.getLevel().equals(CUSTOPMERTWOLEVEL) || customer.getLevel().equals(CUSTOPMERTHREELEVEL)) {
            throw new InvalidRequestException("invalidStatus", "invalid status");
        }

        customer.setLevel(CUSTOPMERTWOLEVEL);
        customer.setUpdateBy(sessionContext.getUser().getId());
        customerService.updateLevel(customer);
        int userId = sessionContext.getUser().getId();
        CoinRecord coinRecord = new CoinRecord();;
        Customer parentCustomer = customerService.getById(customer.getParentId());
        Customer superParent = customerService.getById(customer.getSuperParentId());
        boolean isParent = false;
        boolean isSuper = false;
        //如果有上级，并判断上级不是普通会员
        if (!Objects.isNull(parentCustomer)) {
            isParent = !Objects.equals(customer.getParentId(), NOTPARENT) && !Objects.equals(parentCustomer.getLevel(),CUSTOPMERONELEVEL);
        }
        //如果有上上级，判断上上级不是普通会员
        if (!Objects.isNull(superParent)) {
            isSuper = !Objects.equals(customer.getSuperParentId(), NOTPARENT) && !Objects.equals(superParent.getLevel(),CUSTOPMERONELEVEL);
        }
        if(isParent||isSuper){
            //赋值
            coinRecord.setCreateBy(userId);
            coinRecord.setSourceCustomerId(customer.getId());
            coinRecord.setSourceCustomerLevel(customer.getLevel());
            coinRecord.setSourceCustomerPhone(customer.getPhone());
        }
        if(isParent && isSuper){
            BigDecimal vipCard = goodsService.getById(Integer.valueOf(systemParamsService.getValueByKey(Collections.singletonMap("key", VIPCARD)).getValue())).getPrice();
            //一级分红
            rebate(vipCard,parentCustomer,coinRecord,ONE_LEVEL);
            //二级分红
            rebate(vipCard,superParent,coinRecord,TWO_LEVEL);
            return new ResponseView();
        }
        if(isParent && !isSuper){
            BigDecimal vipCard = goodsService.getById(Integer.valueOf(systemParamsService.getValueByKey(Collections.singletonMap("key", VIPCARD)).getValue())).getPrice();
            rebate(vipCard,parentCustomer,coinRecord,ONE_LEVEL);
            return new ResponseView();
        }
        if(isSuper && !isParent){
            BigDecimal vipCard = goodsService.getById(Integer.valueOf(systemParamsService.getValueByKey(Collections.singletonMap("key", VIPCARD)).getValue())).getPrice();
            rebate(vipCard,superParent,coinRecord,ONE_LEVEL);
            return new ResponseView();
        }
        return new ResponseView();
    }

    //计算分红
    public void rebate(BigDecimal vipCard,Customer customer,CoinRecord coinRecord,int level){
        BigDecimal value = null;
        if(Objects.equals(level,ONE_LEVEL)){
            value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key", INVITECOMMONBECOMEVIPCOIN)).getValue());
        }
        if(Objects.equals(level,TWO_LEVEL)){
            value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key", INVITEVIPINVITECOMMONCOIN)).getValue());
        }
        BigDecimal parentAmount = BigDecimalUtils.multiply(value, vipCard);
        customer.setCustomerCoin(parentAmount);
        customerService.updateAddCustomerCoin(customer);
        //添加分红记录
        coinRecord.setCustomerId(customer.getId());
        coinRecord.setCustomerPhone(customer.getPhone());
        coinRecord.setAmount(parentAmount);
        coinRecord.setType(INVITEREWARD);
        coinRecordService.create(coinRecord);
    }
//    //计算二级（跳级）分红
//    public void TwoToVip(BigDecimal vipCard,Customer customer,CoinRecord coinRecord){
//        BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key", INVITEVIPINVITECOMMONCOIN)).getValue());
//        BigDecimal parentAmount = BigDecimalUtils.multiply(value, vipCard);
//        customer.setCustomerCoin(parentAmount);
//        customerService.updateAddCustomerCoin(customer);
//        //添加分红记录
//        coinRecord.setCustomerId(customer.getId());
//        coinRecord.setCustomerPhone(customer.getPhone());
//        coinRecord.setAmount(parentAmount);
//        coinRecord.setType(INVITEREWARD);
//        coinRecordService.create(coinRecord);
//    }

    //设为商户
    @RequestMapping(value = "/resetBusiness", method = RequestMethod.PUT)
    public ResponseView resetBusiness(@Valid @RequestBody CustomerBusinessForm form) {
        Customer customer = customerService.getById(form.getId());
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        if (customer.getBusiness().equals(CUSTOMER)||customer.getBusiness().equals(BUSINESSAPPLICATION)) {
            customer.setBusinessName(form.getBusinessName());
            customer.setBusiness(BUSINESS);
            customer.setUpdateBy(sessionContext.getUser().getId());
            customer.setBusinessStatus(BUSINESS_ENABLE);
            customerService.updateBusiness(customer);
        }
        return new ResponseView();
    }

    @RequestMapping(value = DETAIL, method = RequestMethod.GET)
    public Customer detail(@PathVariable Integer id) {
        Customer customer = customerService.getById(id);
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not found");
        }
        return customer;
    }

    //重置登陆密码
    @RequestMapping(value = "/loginPassword", method = RequestMethod.PUT)
    public ResponseView resetLoginPassword(@Valid @RequestBody ResetCustomerPasswordForm form) {
        Customer customer = customerService.getById(form.getId());
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        customer.setLoginPassword(MD5.encrypt(INIT_PASSWORD + MD5_SALT));
        customerService.updateLoginPassword(customer);
        return new ResponseView();
    }

    //重置支付密码
    @RequestMapping(value = "/paymentPassword", method = RequestMethod.PUT)
    public ResponseView resetPaymentPassword(@Valid @RequestBody ResetCustomerPasswordForm form) {
        Customer customer = customerService.getById(form.getId());
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        customer.setPaymentPassword(MD5.encrypt(INIT_PASSWORD + MD5_SALT));
        customerService.updatePaymentPassword(customer);
        return new ResponseView();
    }

    @RequestMapping(value = "/verifyBusiness", method = RequestMethod.PUT)
    public ResponseView verifyBusiness(@Valid @RequestBody VerifyBusinessForm form) {
        Customer customer = customerService.getById(form.getId());
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        if (!customer.getBusiness().equals(BUSINESSAPPLICATION)) {
            throw new InvalidRequestException("invalidBusinessApplication", "not application status");
        }
        if (!form.getStatus().equals(BUSINESS) && !form.getStatus().equals(CUSTOMER)) {
            throw new InvalidRequestException("invalidStatus", "invalid status");
        }

        customer.setBusiness(form.getStatus());
        customer.setBusinessAccount(form.getStatus().equals(BUSINESS) ? String.format("%06d",customer.getId()) : "0");
        customer.setBusinessStatus(form.getStatus().equals(BUSINESS) ? BUSINESS_ENABLE : BUSINESS_DISABLE);
        customer.setUpdateBy(sessionContext.getUser().getId());
        customerService.updateBusinessApplication(customer);

        return new ResponseView();
    }
}
