package com.longyan.distribution.controller.management;

import com.longyan.distribution.constants.GoldRecordConstans;
import com.longyan.distribution.constants.OilDrillConstants;
import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.CoinRecord;
import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.domain.GoldRecord;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.*;
import com.longyan.distribution.response.CustomerListView;
import com.longyan.distribution.response.GoldRecordHandleView;
import com.longyan.distribution.response.GoldRecordListView;
import com.longyan.distribution.service.CoinRecordService;
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

import static com.longyan.distribution.constants.CoinRecordConstants.INVITEREWARD;
import static com.longyan.distribution.constants.CoinRecordConstants.RECHARGEREWARD;
import static com.longyan.distribution.constants.CommonConstants.*;
import static com.longyan.distribution.constants.CustomerConstants.*;
import static com.longyan.distribution.constants.GoldRecordConstans.*;
import static com.longyan.distribution.constants.OilDrillConstants.REDUCEFAIL;
import static com.longyan.distribution.constants.SystemParamsConstants.*;

@RestController("goldRecordManagementController")
@RequestMapping(value = "/management/goldRecord")
@UserLoginRequired
public class GoldRecordController {

    private static final Logger logger = LoggerFactory.getLogger(GoldRecordController.class);

    @Autowired
    private GoldRecordService goldRecordService;

    @Autowired
    private CoinRecordService coinRecordService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SystemParamsService systemParamsService;

    @Autowired
    private SessionContext sessionContext;

    @RequestMapping(value = "/customerList",method = RequestMethod.POST)
    public GoldRecordListView customerList(@Valid @RequestBody GoldRecordCustomerListForm form){
        Map<String,Object> query = form.getQueryMap();
        query.put("customerId",form.getCustomerId());
        List<GoldRecord> list = goldRecordService.selectList(query);
        for (GoldRecord record:list){
            if(record.getType().equals(WITHDRAW)){
                record.setAmount(BigDecimalUtils.multiply(record.getAmount(),-1));
            }
        }
        return new GoldRecordListView(list,goldRecordService.selectCount(query));
    }

    @RequestMapping(value = "/businessList",method = RequestMethod.POST)
    public GoldRecordListView businessList(@Valid @RequestBody GoldRecordBusinessListForm form){
        Map<String,Object> query = form.getQueryMap();
        query.put("businessId",form.getBusinessId());
        List<GoldRecord> list = goldRecordService.selectList(query);
        for (GoldRecord record:list){
            if(record.getType().equals(OilDrillConstants.TRANSFER)||record.getType().equals(WITHDRAW)){
                record.setAmount(BigDecimalUtils.multiply(record.getAmount(),-1));
            }
        }
        return new GoldRecordListView(list,goldRecordService.selectCount(query));
    }

    @RequestMapping(value = "/cashList",method = RequestMethod.POST)
    public GoldRecordListView cashList(@Valid @RequestBody GoldRecordCheckListForm form){
        Map<String,Object> map = form.getQueryMap();
        map.put("type",WITHDRAW);
        return new GoldRecordListView(goldRecordService.selectGoldRecordAndCustomerList(map),goldRecordService.selectCount(map));
    }

    //打款展示处理
    @RequestMapping(value = "/moneyHandle",method = RequestMethod.POST)
    public GoldRecordHandleView moneyHandle(@Valid @RequestBody GoldRecordMoneyHandleForm form){
        GoldRecord goldRecord = goldRecordService.getById(form.getId());
        if (Objects.isNull(goldRecord)) {
            throw new ResourceNotFoundException("goldRecord not exists");
        }
        Customer customer = customerService.getById(goldRecord.getBusinessId());
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("用户不存在");
        }
        if(!Objects.equals(goldRecord.getType(),WITHDRAW)) {
            throw new InvalidRequestException("invalidStatus", "invalid status");
        }
        //计算手续费，拿出系统参数手续费,减掉手续费
        BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",BUSINESSGOLDCASH)).getValue());
        BigDecimal handleMoney = BigDecimalUtils.multiply(goldRecord.getAmount(),value);
        BigDecimal cash = goldRecord.getAmount().subtract(handleMoney);
        GoldRecordHandleView goldRecordHandleView = new GoldRecordHandleView();
        BeanUtils.copyProperties(customer, goldRecordHandleView);
        goldRecordHandleView.setApplyCount(goldRecord.getAmount());
        goldRecordHandleView.setHandleMoney(handleMoney);
        goldRecordHandleView.setExchangeCash(cash);
        return goldRecordHandleView;
    }


    //改变状态
    @Transactional
    @RequestMapping(value = "/resetStatus", method = RequestMethod.PUT)
    public ResponseView resetStatus(@Valid @RequestBody GoldRecordStatusForm form) {
        GoldRecord goldRecord = goldRecordService.getById(form.getId());
        if (Objects.isNull(goldRecord)) {
            throw new ResourceNotFoundException("goldRecord not exists");
        }
        //审核通过减少用户金币
        if(Objects.equals(form.getStatus(),PASS)&&Objects.equals(goldRecord.getStatus(),WAITCHECK)){
            Customer customer = customerService.getById(goldRecord.getBusinessId());
            if (Objects.isNull(customer)) {
                throw new ResourceNotFoundException("用户不存在");
            }
            //判断要减少的金币会不会大于商户金币
            if(Objects.equals(customer.getBusinessGold().compareTo(goldRecord.getAmount()),-1)){
                throw new InvalidRequestException("reduceError","The amount of gold to be reduced is greater than the user's gold");
            }
            goldRecord.setStatus(PASS);
            goldRecordService.updateStatus(goldRecord);
            customer.setBusinessGold(goldRecord.getAmount());
            int status= customerService.updateReduceBusinessGold(customer);
            if(Objects.equals(status,REDUCEFAIL)){
                throw new InvalidRequestException("reduceError","The amount of gold to be reduced is greater than the user's gold");
            }
        }
        //审核没通过添加拒绝理由
        if(Objects.equals(form.getStatus(),REFUSE)){
            goldRecord.setStatus(form.getStatus());
            goldRecord.setRefuseReason(form.getRefuseReason());
            goldRecord.setUpdateBy(sessionContext.getUser().getId());
            goldRecordService.updateStatus(goldRecord);
        }
        return new ResponseView();
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

//    商户增减金币
    @Transactional
    @RequestMapping(value = "/businessAddGoldRecord",method = RequestMethod.POST)
    public Customer businessAddGoldRecord(@Valid @RequestBody BusinessAddReduceGoldForm form){
        Customer customer = customerService.getById(form.getId());
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("用户不存在");
        }
        if(!Objects.equals(BUSINESS,customer.getBusiness())){
            throw new InvalidRequestException("addError","This account is not a merchant");
        }
        GoldRecord goldRecord = new GoldRecord();
        //amount用于当前用户充值
        BigDecimal amount = new BigDecimal(form.getAmount());
        goldRecord.setCustomerId(NOTCUSTOMER);
        goldRecord.setBusinessId(customer.getId());
        goldRecord.setBusinessName(customer.getBusinessName());
        goldRecord.setBusinessAccount(customer.getBusinessAccount());
        goldRecord.setCustomerPhone(customer.getPhone());
        goldRecord.setCreateBy(sessionContext.getUser().getId());
        goldRecord.setType(form.getType());
        //判断是增加还是减少，
        if(Objects.equals(form.getType(),USERREDUCE)){
            //判断要减少的金币是否大于用户金币
            if(Objects.equals(customer.getBusinessGold().compareTo(amount),-1)){
                throw new InvalidRequestException("reduceError","The amount of gold to be reduced is greater than the user's gold");
            }
            customer.setBusinessGold(amount);
            int status = customerService.updateReduceBusinessGold(customer);
            if(Objects.equals(status,GoldRecordConstans.REDUCEFAIL)){
                throw new InvalidRequestException("reduceError","The amount of gold to be reduced is greater than the user's gold");
            }
            //添加减少金币记录
            goldRecord.setAmount(amount.multiply(new BigDecimal(-1)));
            goldRecordService.create(goldRecord);
            return customerService.getById(form.getId());
        }
        if(Objects.equals(form.getType(),USERADD)){
            customer.setBusinessGold(amount);
            customerService.updateAddBusinessGold(customer);
            //添加增加金币记录
            goldRecord.setAmount(amount);
            goldRecordService.create(goldRecord);
        }
        return customerService.getById(form.getId());
    }
    //用户增减金币
    @Transactional
    @RequestMapping(value = "/customerAddReduceGold",method = RequestMethod.POST)
    public Customer customerAddGoldRecord(@Valid @RequestBody CustomerAddReduceGoldForm form){
        Customer customer = customerService.getById(form.getId());
        int userId= sessionContext.getUser().getId();
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("用户不存在");
        }
        GoldRecord goldRecord = new GoldRecord();
        CoinRecord coinRecord = new CoinRecord();
        //amount用于当前用户充值
        BigDecimal amount = new BigDecimal(form.getAmount());
        goldRecord.setCustomerId(customer.getId());
        goldRecord.setBusinessId(NOTBUSINESS);
        goldRecord.setBusinessName("");
        goldRecord.setBusinessAccount("");
        goldRecord.setCustomerPhone(customer.getPhone());
        goldRecord.setCreateBy(userId);
        goldRecord.setType(form.getType());
        coinRecord.setCreateBy(userId);
        coinRecord.setSourceCustomerId(customer.getId());
        coinRecord.setSourceCustomerLevel(customer.getLevel());
        coinRecord.setSourceCustomerPhone(customer.getPhone());
        //判断是增加还是减少，减少不用分红
        if(Objects.equals(form.getType(),USERREDUCE)){
            //判断要减少的金币是否大于用户金币
            if(Objects.equals(customer.getCustomerGold().compareTo(amount),-1)){
                throw new InvalidRequestException("reduceError","The amount of gold to be reduced is greater than the user's gold");
            }
            customer.setCustomerGold(amount);
            int status = customerService.updateReduceCustomerGold(customer);
            if(Objects.equals(status,GoldRecordConstans.REDUCEFAIL)){
                throw new InvalidRequestException("reduceError","The amount of gold to be reduced is greater than the user's gold");
            }
            //添加减少金币记录
            goldRecord.setAmount(amount.multiply(new BigDecimal(-1)));
            goldRecordService.create(goldRecord);
            Customer currentCustomer = customerService.getById(form.getId());
            return currentCustomer;
        }
        if(Objects.equals(form.getType(),RECHARGE)) {
            //进行当前用户按等级分配折扣充值增加金币
            if(Objects.equals(customer.getLevel(), CUSTOPMERONELEVEL)){
                BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",COMMONGOLDCHARGE)).getValue());
                BigDecimal currentAmount=amount.divide(value, DECIMAL, BigDecimal.ROUND_UP);
                customer.setCustomerGold(currentAmount);
                customerService.updateAddCustomerGold(customer);
                //添加增加金币记录
                goldRecord.setAmount(currentAmount);
                goldRecordService.create(goldRecord);
            }
            if(Objects.equals(customer.getLevel(), CUSTOPMERTWOLEVEL)){
                BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",VIPREGOLDCHARGE)).getValue());
                BigDecimal currentAmount=amount.divide(value, DECIMAL, BigDecimal.ROUND_UP);
                customer.setCustomerGold(currentAmount);
                customerService.updateAddCustomerGold(customer);
                //添加增加金币记录
                goldRecord.setAmount(currentAmount);
                goldRecordService.create(goldRecord);
            }
            if(Objects.equals(customer.getLevel(), CUSTOPMERTHREELEVEL)){
                BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",PARTNEGOLDRRECHARGE)).getValue());
                BigDecimal currentAmount=amount.divide(value, DECIMAL, BigDecimal.ROUND_UP);
                customer.setCustomerGold(currentAmount);
                customerService.updateAddCustomerGold(customer);
                //添加增加金币记录
                goldRecord.setAmount(currentAmount);
                goldRecordService.create(goldRecord);
            }
        }
        //如果是后台手动添加，不是前台充值，就直接返回,不用给其他人分红
        if(Objects.equals(form.getType(), USERADD)){
            customer.setCustomerGold(amount);
            customerService.updateAddCustomerGold(customer);
            goldRecord.setAmount(amount);
            goldRecordService.create(goldRecord);
            Customer currentCustomer = customerService.getById(form.getId());
            return currentCustomer;
        }
        if(Objects.equals(form.getType(),RECHARGE)) {
            //判断当前用户是不是合伙人，判断是否有上级
            if(Objects.equals(customer.getLevel(),CUSTOPMERTHREELEVEL)&&!Objects.equals(customer.getParentId(),NOTPARENT)){
                Customer parentCustomer = customerService.getById(customer.getParentId());
                //判断上级等级是不是合伙人，是，添加同级分红
                if(Objects.equals(parentCustomer.getLevel(), CUSTOPMERTHREELEVEL)){
                    rebate(amount,parentCustomer,coinRecord,CUSTOPMERTHREELEVEL);
                    Customer currentCustomer = customerService.getById(form.getId());
                    return currentCustomer;
                }
            }
            //判断当前用户是不是普通用户，是，判断是否有上级，
            if(Objects.equals(customer.getLevel(),CUSTOPMERONELEVEL)&&!Objects.equals(customer.getParentId(),NOTPARENT)){
                Customer parentCustomer = customerService.getById(customer.getParentId());
                //判断上级等级是不是vip
                if(Objects.equals(parentCustomer.getLevel(), CUSTOPMERTWOLEVEL)){
                    //添加vip分红
                    vipToNormal(amount,parentCustomer,coinRecord);
                    //判断是否有上上级
                    if(!Objects.equals(customer.getSuperParentId(),NOTSUPERPARENT)){
                        Customer superParentCustomer = customerService.getById(customer.getSuperParentId());
                        //判断上上级是不是合伙人
                        if(Objects.equals(superParentCustomer.getLevel(), CUSTOPMERTHREELEVEL)){
                            //拿出拿出合伙人对vip分红百分比，添加分红钢镚
                            partnerToVip(amount,superParentCustomer,coinRecord);
                            Customer currentCustomer = customerService.getById(form.getId());
                            return currentCustomer;
                        }
                    }
                }
                //判断上级等级是不是普通会员
                if(Objects.equals(parentCustomer.getLevel(), CUSTOPMERONELEVEL)){
                    //同级分红
                    rebate(amount,parentCustomer,coinRecord,CUSTOPMERONELEVEL);
                    //判断是否有上上级
                    if(!Objects.equals(customer.getSuperParentId(),NOTSUPERPARENT)) {
                        Customer superParentCustomer = customerService.getById(customer.getSuperParentId());
                        //判断上上级是不是合伙人
                        if(Objects.equals(superParentCustomer.getLevel(), CUSTOPMERTHREELEVEL)){
                            //计算合伙人对普通用户分红百分比，添加分红
                            partnerToNormal(amount,superParentCustomer,coinRecord);
                        }
                        //判断上上级是不是vip
                        if(Objects.equals(superParentCustomer.getLevel(), CUSTOPMERTWOLEVEL)){
                            //计算vip对普通用户分红百分比，添加分红
                            vipToNormal(amount,superParentCustomer,coinRecord);
                        }
                    }
                }

                //判断上级等级是不是合伙人，是合伙人上上级没有分红
                if(Objects.equals(parentCustomer.getLevel(), CUSTOPMERTHREELEVEL)){
                    //计算合伙人对普通用户分红百分比，添加分红
                    partnerToNormal(amount,parentCustomer,coinRecord);
                    Customer currentCustomer = customerService.getById(form.getId());
                    return currentCustomer;
                }
            }
            //判断当前用户是不是vip，是，判断是否有上级
            if(Objects.equals(customer.getLevel(),CUSTOPMERTWOLEVEL)&&!Objects.equals(customer.getParentId(),NOTPARENT)){
                Customer parentCustomer = customerService.getById(customer.getParentId());
                //判断上级等级是不是合伙人
                if(Objects.equals(parentCustomer.getLevel(), CUSTOPMERTHREELEVEL)){
                    //拿出合伙人对vip分红百分比，添加分红
                    partnerToVip(amount,parentCustomer,coinRecord);
                    //是合伙人上上级没有分红
                    Customer currentCustomer = customerService.getById(form.getId());
                    return currentCustomer;
                }
                //判断上级等级是不是普通用户，是，判断是否有上上级
                if(Objects.equals(parentCustomer.getLevel(), CUSTOPMERONELEVEL)&&!Objects.equals(customer.getSuperParentId(),NOTSUPERPARENT)){
                    Customer superParentCustomer = customerService.getById(customer.getSuperParentId());
                    //判断上上级是不是合伙人
                    if(Objects.equals(superParentCustomer.getLevel(),CUSTOPMERTHREELEVEL)){
                        //拿出合伙人对vip分红百分比，添加分红
                        partnerToVip(amount,superParentCustomer,coinRecord);
                    }
                }
                //判断上级等级是不是vip，
                if(Objects.equals(parentCustomer.getLevel(), CUSTOPMERTWOLEVEL)){
                    //同级分红
                    rebate(amount,parentCustomer,coinRecord,CUSTOPMERTWOLEVEL);
                    //判断是否有上上级
                    if(!Objects.equals(customer.getSuperParentId(),NOTSUPERPARENT)){
                        Customer superParentCustomer = customerService.getById(customer.getSuperParentId());
                        //判断上上级是不是合伙人
                        if(Objects.equals(superParentCustomer.getLevel(),CUSTOPMERTHREELEVEL)){
                            //拿出合伙人对vip分红百分比，添加分红
                            partnerToVip(amount,superParentCustomer,coinRecord);
                        }
                    }
                }
            }
        }
        Customer currentCustomer = customerService.getById(form.getId());
        return currentCustomer;
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
    //计算同级分红
    public void rebate(BigDecimal amount,Customer customer,CoinRecord coinRecord,int level){
        BigDecimal value = null;
        if(Objects.equals(level,CUSTOPMERONELEVEL)){
            value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key", COMMON_INVITE_COMMON_RECHARGE_GOLD_COIN)).getValue());
        }
        if(Objects.equals(level,CUSTOPMERTWOLEVEL)){
            value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key", VIP_INVITE_VIP_RECHARGE_GOLD_COIN)).getValue());
        }
        if(Objects.equals(level,CUSTOPMERTHREELEVEL)){
            value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key", PARTNER_INVITE_PARTNER_RECHARGE_GOLD_COIN)).getValue());
        }
        BigDecimal parentAmount = BigDecimalUtils.multiply(value, amount);
        customer.setCustomerCoin(parentAmount);
        customerService.updateAddCustomerCoin(customer);
        //添加分红记录
        coinRecord.setCustomerId(customer.getId());
        coinRecord.setCustomerPhone(customer.getPhone());
        coinRecord.setAmount(parentAmount);
        coinRecord.setType(RECHARGEREWARD);
        coinRecordService.create(coinRecord);
    }

    //计算vip对普通用户分红百分比，添加分红
    public void vipToNormal(BigDecimal amount,Customer customer,CoinRecord coinRecord){
        BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",VIPINVITECOMMONRECHARGEGOLDCOIN)).getValue());
        BigDecimal parentAmount = BigDecimalUtils.multiply(value,amount);
        customer.setCustomerCoin(parentAmount);
        customerService.updateAddCustomerCoin(customer);
        //添加分红记录
        coinRecord.setCustomerId(customer.getId());
        coinRecord.setCustomerPhone(customer.getPhone());
        coinRecord.setAmount(parentAmount);
        coinRecord.setType(RECHARGEREWARD);
        coinRecordService.create(coinRecord);
    }
    //计算合伙人对普通用户分红百分比，添加分红
    public void partnerToVip(BigDecimal amount,Customer customer,CoinRecord coinRecord) {
        BigDecimal partnerRebate =  new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",INVITEVIPRECHARGEGOLDCOIN)).getValue());
        BigDecimal superParentAmount = BigDecimalUtils.multiply(partnerRebate,amount);
        customer.setCustomerCoin(superParentAmount);
        customerService.updateAddCustomerCoin(customer);
        //添加分红记录
        coinRecord.setCustomerId(customer.getId());
        coinRecord.setCustomerPhone(customer.getPhone());
        coinRecord.setAmount(superParentAmount);
        coinRecord.setType(RECHARGEREWARD);
        coinRecordService.create(coinRecord);
    }
    //计算合伙人对普通用户分红百分比，添加分红
    public void partnerToNormal(BigDecimal amount,Customer customer,CoinRecord coinRecord) {
        BigDecimal vipRebate =  new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",INVITEVIPRECHARGEGOLDCOIN)).getValue());
        BigDecimal normalRebate = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",VIPINVITECOMMONRECHARGEGOLDCOIN)).getValue());
        BigDecimal partnerRebate=vipRebate.add(normalRebate);
        BigDecimal superParentAmount = BigDecimalUtils.multiply(partnerRebate,amount);
        customer.setCustomerCoin(superParentAmount);
        customerService.updateAddCustomerCoin(customer);
        //添加分红记录
        coinRecord.setCustomerId(customer.getId());
        coinRecord.setCustomerPhone(customer.getPhone());
        coinRecord.setAmount(superParentAmount);
        coinRecord.setType(RECHARGEREWARD);
        coinRecordService.create(coinRecord);
    }
}
