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
        //判断是否有上级
        if (!Objects.equals(customer.getParentId(), NOTPARENT)) {
            int userId = sessionContext.getUser().getId();
            CoinRecord coinRecord = new CoinRecord();
            coinRecord.setCreateBy(userId);
            coinRecord.setSourceCustomerId(customer.getId());
            coinRecord.setSourceCustomerLevel(customer.getLevel());
            coinRecord.setSourceCustomerPhone(customer.getPhone());
            Customer parentCustomer = customerService.getById(customer.getParentId());
            //判断上级等级是不是vip
            if (Objects.equals(parentCustomer.getLevel(), CUSTOPMERTWOLEVEL)) {
                //拿出分红比
                BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key", INVITECOMMONBECOMEVIPCOIN)).getValue());
                BigDecimal vipCard = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key", VIPCARD)).getValue());
                BigDecimal parentAmount = BigDecimalUtils.multiply(value, vipCard);
                parentCustomer.setCustomerCoin(parentAmount);
                customerService.updateAddCustomerCoin(parentCustomer);
                //添加分红记录
                coinRecord.setCustomerId(parentCustomer.getId());
                coinRecord.setCustomerPhone(parentCustomer.getPhone());
                coinRecord.setAmount(parentAmount);
                coinRecord.setType(INVITEREWARD);
                coinRecordService.create(coinRecord);
                //判断该vip上级是否有x个用户申请成vip,有的话把该vip上级升为合伙人
                if (customerService.selectSubVipCount(parentCustomer.getId()) >= Integer.parseInt(systemParamsService.getValueByKey(Collections.singletonMap("key", VIPINVITENUM)).getValue())) {
                    parentCustomer.setLevel(CUSTOPMERTHREELEVEL);
                    parentCustomer.setUpdateBy(userId);
                    customerService.updateLevel(parentCustomer);
                }
                //判断是否有上上级
                if (!Objects.equals(customer.getSuperParentId(), NOTSUPERPARENT)) {
                    Customer superParentCustomer = customerService.getById(customer.getSuperParentId());
                    //判断上上级是不是合伙人
                    if (Objects.equals(superParentCustomer.getLevel(), CUSTOPMERTHREELEVEL)) {
                        //拿出分红比
                        BigDecimal superValue = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key", INVITEVIPINVITECOMMONCOIN)).getValue());
                        BigDecimal superParentAmount = BigDecimalUtils.multiply(superValue, vipCard);
                        superParentCustomer.setCustomerCoin(superParentAmount);
                        customerService.updateAddCustomerCoin(superParentCustomer);
                        //添加分红记录
                        coinRecord.setCustomerId(superParentCustomer.getId());
                        coinRecord.setCustomerPhone(superParentCustomer.getPhone());
                        coinRecord.setAmount(superParentAmount);
                        coinRecord.setType(INVITEREWARD);
                        coinRecordService.create(coinRecord);
                    }
                }
            }
            //判断上级等级是不是合伙人，是合伙人上上级没有分红
            if (Objects.equals(parentCustomer.getLevel(), CUSTOPMERTHREELEVEL)) {
                BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key", INVITECOMMONBECOMEVIPCOIN)).getValue());
                BigDecimal vipCard = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key", VIPCARD)).getValue());
                BigDecimal parentAmount = BigDecimalUtils.multiply(value, vipCard);
                parentCustomer.setCustomerCoin(parentAmount);
                customerService.updateAddCustomerCoin(parentCustomer);
                //添加分红记录
                coinRecord.setCustomerId(parentCustomer.getId());
                coinRecord.setCustomerPhone(parentCustomer.getPhone());
                coinRecord.setAmount(parentAmount);
                coinRecord.setType(INVITEREWARD);
                coinRecordService.create(coinRecord);
            }
        }

        return new ResponseView();
    }

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
        customer.setBusinessStatus(form.getStatus().equals(BUSINESS) ? BUSINESS_ENABLE : BUSINESS_DISABLE);
        customer.setUpdateBy(sessionContext.getUser().getId());
        customerService.updateBusinessApplication(customer);

        return new ResponseView();
    }
}
