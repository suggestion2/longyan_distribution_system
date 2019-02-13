package com.longyan.distribution.controller.management;

import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.CoinRecord;
import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.domain.GoldRecord;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.*;
import com.longyan.distribution.response.GoldRecordHandleView;
import com.longyan.distribution.response.OilDrillRecordHandleView;
import com.longyan.distribution.service.CoinRecordService;
import com.longyan.distribution.service.CustomerService;
import com.longyan.distribution.service.SystemParamsService;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.rest.view.ResponseView;
import com.sug.core.rest.view.SuccessView;
import com.longyan.distribution.domain.OilDrillRecord;
import com.longyan.distribution.service.OilDrillRecordService;
import com.longyan.distribution.response.OilDrillRecordListView;
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
import java.util.Map;
import java.util.Objects;

import static com.longyan.distribution.constants.CoinRecordConstants.RECHARGEREWARD;
import static com.longyan.distribution.constants.CommonConstants.*;
import static com.longyan.distribution.constants.CustomerConstants.*;
import static com.longyan.distribution.constants.CustomerConstants.CUSTOPMERTHREELEVEL;
import static com.longyan.distribution.constants.OilDrillConstants.*;
import static com.longyan.distribution.constants.OilDrillConstants.NOTCUSTOMER;
import static com.longyan.distribution.constants.OilDrillConstants.RECHARGE;
import static com.longyan.distribution.constants.OilDrillConstants.USERADD;
import static com.longyan.distribution.constants.OilDrillConstants.USERREDUCE;
import static com.longyan.distribution.constants.SystemParamsConstants.*;
import static com.longyan.distribution.constants.SystemParamsConstants.VIPINVITECOMMONRECHARGEGOLDCOIN;

@RestController("oilDrillRecordManagementController")
@RequestMapping(value = "/management/oilDrillRecord")
@UserLoginRequired
public class OilDrillRecordController {

    private static final Logger logger = LoggerFactory.getLogger(OilDrillRecordController.class);

    @Autowired
    private OilDrillRecordService oilDrillRecordService;

    @Autowired
    private CoinRecordService coinRecordService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SystemParamsService systemParamsService;

    @Autowired
    private SessionContext sessionContext;

    @RequestMapping(value = LIST,method = RequestMethod.POST)
    public OilDrillRecordListView list(@Valid @RequestBody OilDrillRecordListForm form){
        return new OilDrillRecordListView(oilDrillRecordService.selectList(form.getQueryMap()),oilDrillRecordService.selectCount(form.getQueryMap()));
    }

    @RequestMapping(value = DETAIL,method = RequestMethod.GET)
    public OilDrillRecord detail(@PathVariable Integer id){
        return oilDrillRecordService.getById(id);
    }

    @RequestMapping(value = CREATE,method = RequestMethod.POST)
    public SuccessView create(@Valid @RequestBody OilDrillRecordCreateForm form){
        OilDrillRecord oilDrillRecord = new OilDrillRecord();
        BeanUtils.copyProperties(form,oilDrillRecord);
        oilDrillRecordService.create(oilDrillRecord);
        return new SuccessView();
    }

    //提现列表
    @RequestMapping(value = "cashList",method = RequestMethod.POST)
    public OilDrillRecordListView cashList(@Valid @RequestBody OilDrillCashListForm form){
        Map<String,Object> map = form.getQueryMap();
        map.put("type",WITHDRAW);
        return new OilDrillRecordListView(oilDrillRecordService.selectOilRecordAndCustomerList(map),oilDrillRecordService.selectCount(map));
    }

    //打款展示处理
    @RequestMapping(value = "moneyHandle",method = RequestMethod.POST)
    public OilDrillRecordHandleView moneyHandle(@Valid @RequestBody GoldRecordMoneyHandleForm form){
        OilDrillRecord oilDrillRecord = oilDrillRecordService.getById(form.getId());
        if (Objects.isNull(oilDrillRecord)) {
            throw new ResourceNotFoundException("goldRecord not exists");
        }
        Customer customer = customerService.getById(oilDrillRecord.getCustomerId());
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        if(!Objects.equals(oilDrillRecord.getType(),WITHDRAW)) {
            throw new InvalidRequestException("invalidStatus", "invalid status");
        }
        //计算手续费，拿出系统参数手续费,减掉手续费
        BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",BUSINESSOILDRILLCASH)).getValue());
        BigDecimal handleMoney = BigDecimalUtils.multiply(oilDrillRecord.getAmount(),value);
        BigDecimal cash = oilDrillRecord.getAmount().subtract(handleMoney);
        OilDrillRecordHandleView oilDrillRecordHandleView = new OilDrillRecordHandleView();
        BeanUtils.copyProperties(customer, oilDrillRecordHandleView);
        oilDrillRecordHandleView.setApplyCount(oilDrillRecord.getAmount());
        oilDrillRecordHandleView.setHandleMoney(handleMoney);
        oilDrillRecordHandleView.setExchangeCash(cash);
        return oilDrillRecordHandleView;
    }
    //改变状态
    @Transactional
    @RequestMapping(value = "/resetStatus", method = RequestMethod.PUT)
    public ResponseView resetStatus(@Valid @RequestBody OilDrillRecordStatusForm form) {
        OilDrillRecord oilDrillRecord = oilDrillRecordService.getById(form.getId());
        if (Objects.isNull(oilDrillRecord)) {
            throw new ResourceNotFoundException("oilDrill not exists");
        }
        //审核通过减少用户油钻
        if(Objects.equals(form.getStatus(),PASS)&&Objects.equals(oilDrillRecord.getStatus(),WAITCHECK)){
            Customer customer = customerService.getById(oilDrillRecord.getCustomerId());
            if (Objects.isNull(customer)) {
                throw new ResourceNotFoundException("customer not exists");
            }
           //判断要减少的油钻会不会大于用户油钻
            if(Objects.equals(customer.getBusinessOilDrill().compareTo(form.getApplyCount()),-1)){
                throw new InvalidRequestException("reduceError","The amount of oilDrill to be reduced is greater than the user's oilDrill");
            }
            oilDrillRecord.setStatus(PASS);
            oilDrillRecordService.updateStatus(oilDrillRecord);
            customer.setBusinessOilDrill(form.getApplyCount());
            int status= customerService.updateReduceBusinessOilDrill(customer);
            if(Objects.equals(status,REDUCEFAIL)){
                throw new InvalidRequestException("reduceError","The amount of oilDrill to be reduced is greater than the user's oilDrill");
            }
        }
        //审核没通过添加拒绝理由
        if(Objects.equals(form.getStatus(),REFUSE)){
            oilDrillRecord.setStatus(form.getStatus());
            oilDrillRecord.setRefuseReason(form.getRefuseReason());
            oilDrillRecord.setUpdateBy(sessionContext.getUser().getId());
            oilDrillRecordService.updateStatus(oilDrillRecord);
        }
        return new ResponseView();
    }
   // 商户增减油钻
    @Transactional
    @RequestMapping(value = "/businessAddGoldOil",method = RequestMethod.POST)
    public ResponseView businessAddGoldRecord(@Valid @RequestBody BusinessAddReduceGoldForm form){
        Customer customer = customerService.getById(form.getId());
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        if(!Objects.equals(BUSINESS,customer.getBusiness())){
            throw new InvalidRequestException("addError","This account is not a merchant");
        }
        OilDrillRecord oilDrillRecord = new OilDrillRecord();
        //amount用于当前用户充值
        BigDecimal amount = new BigDecimal(form.getAmount());
        oilDrillRecord.setCustomerId(NOTCUSTOMER);
        oilDrillRecord.setBusinessId(customer.getId());
        oilDrillRecord.setBusinessName(customer.getBusinessName());
        oilDrillRecord.setBusinessAccount(customer.getBusinessAccount());
        oilDrillRecord.setCustomerPhone(customer.getPhone());
        oilDrillRecord.setCreateBy(sessionContext.getUser().getId());
        oilDrillRecord.setType(form.getType());
        //判断是增加还是减少
        if(Objects.equals(form.getType(),USERREDUCE)){
            //判断要减少的油钻是否大于用户油钻
            if(Objects.equals(customer.getCustomerOilDrill().compareTo(amount),-1)){
                throw new InvalidRequestException("reduceError","The amount of oilDrill to be reduced is greater than the user's oilDrill");
            }
            customer.setCustomerOilDrill(amount);
            customerService.updateReduceBusinessOilDrill(customer);
            //添加减少油钻记录
            oilDrillRecord.setAmount(amount.multiply(new BigDecimal(-1)));
            oilDrillRecordService.create(oilDrillRecord);
            return new ResponseView();
        }
        if(Objects.equals(form.getType(),USERADD)){
            customer.setCustomerOilDrill(amount);
            customerService.updateAddBusinessOilDrill(customer);
            //添加增加油钻记录
            oilDrillRecord.setAmount(amount);
            oilDrillRecordService.create(oilDrillRecord);
        }
        return new ResponseView();
    }

    //用户增减油钻
    @Transactional
    @RequestMapping(value = "/customerAddReduceOil",method = RequestMethod.POST)
    public Customer customerAddGoldRecord(@Valid @RequestBody CustomerAddReduceGoldForm form){
        Customer customer = customerService.getById(form.getId());
        int userId= sessionContext.getUser().getId();
        if (Objects.isNull(customer)) {
            throw new ResourceNotFoundException("customer not exists");
        }
        OilDrillRecord oilDrillRecord = new OilDrillRecord();
        CoinRecord coinRecord = new CoinRecord();
        //amount用于当前用户充值
        BigDecimal amount = new BigDecimal(form.getAmount());
        oilDrillRecord.setCustomerId(customer.getId());
        oilDrillRecord.setBusinessId(NOTBUSINESS);
        oilDrillRecord.setBusinessName("");
        oilDrillRecord.setBusinessAccount("");
        oilDrillRecord.setCustomerPhone(customer.getPhone());
        oilDrillRecord.setCreateBy(userId);
        oilDrillRecord.setType(form.getType());
        coinRecord.setCreateBy(userId);
        coinRecord.setSourceCustomerId(customer.getId());
        coinRecord.setSourceCustomerLevel(customer.getLevel());
        coinRecord.setSourceCustomerPhone(customer.getPhone());
        //判断是增加还是减少，减少不用分红
        if(Objects.equals(form.getType(),USERREDUCE)){
            //判断要减少的油钻是否大于用户油钻
            if(Objects.equals(customer.getCustomerOilDrill().compareTo(amount),-1)){
                throw new InvalidRequestException("reduceError","The amount of oilDrill to be reduced is greater than the user's oilDrill");
            }
            customer.setCustomerOilDrill(amount);
            int status = customerService.updateReduceCustomerOilDrill(customer);
            if(Objects.equals(status,REDUCEFAIL)){
                throw new InvalidRequestException("reduceError","The amount of oilDrill to be reduced is greater than the user's oilDrill");
            }
            //添加减少油钻记录
            oilDrillRecord.setAmount(amount.multiply(new BigDecimal(-1)));
            oilDrillRecordService.create(oilDrillRecord);
            Customer currentCustomer = customerService.getById(form.getId());
            return currentCustomer;
        }
        if(Objects.equals(form.getType(),RECHARGE)) {
            //进行当前用户按等级分配折扣充值增加油钻
            if(Objects.equals(customer.getLevel(), CUSTOPMERONELEVEL)){
                BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",COMMONOILCHARGE)).getValue());
                BigDecimal currentAmount=amount.divide(value, DECIMAL, BigDecimal.ROUND_UP);
                customer.setCustomerOilDrill(currentAmount);
                customerService.updateAddCustomerOilDrill(customer);
                //添加增加油钻记录
                oilDrillRecord.setAmount(currentAmount);
                oilDrillRecordService.create(oilDrillRecord);
            }
            if(Objects.equals(customer.getLevel(), CUSTOPMERTWOLEVEL)){
                BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",VIPREOILCHARGE)).getValue());
                BigDecimal currentAmount=amount.divide(value, DECIMAL, BigDecimal.ROUND_UP);
                customer.setCustomerOilDrill(currentAmount);
                customerService.updateAddCustomerOilDrill(customer);
                //添加增加油钻记录
                oilDrillRecord.setAmount(currentAmount);
                oilDrillRecordService.create(oilDrillRecord);
            }
            //如果当前用户会员等级是最高，充值就不用给其他人分红;
            if(Objects.equals(customer.getLevel(), CUSTOPMERTHREELEVEL)){
                BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",PARTNEOILRRECHARGE)).getValue());
                BigDecimal currentAmount=amount.divide(value, DECIMAL, BigDecimal.ROUND_UP);
                customer.setCustomerOilDrill(currentAmount);
                customerService.updateAddCustomerOilDrill(customer);
                //添加增加油钻记录
                oilDrillRecord.setAmount(currentAmount);
                oilDrillRecordService.create(oilDrillRecord);
                Customer currentCustomer = customerService.getById(form.getId());
                return currentCustomer;
            }
        }
        //如果是后台手动添加，不是前台充值，就直接返回,不用给其他人分红
        if(Objects.equals(form.getType(), USERADD)){
            customer.setCustomerGold(amount);
            customerService.updateAddCustomerOilDrill(customer);
            oilDrillRecord.setAmount(amount);
            oilDrillRecordService.create(oilDrillRecord);
            Customer currentCustomer = customerService.getById(form.getId());
            return currentCustomer;
        }
        if(Objects.equals(form.getType(),RECHARGE)) {
            //判断当前用户是不是普通用户
            if(Objects.equals(customer.getLevel(),CUSTOPMERONELEVEL)){
                //判断是否有上级
                if(!Objects.equals(customer.getParentId(),NOTPARENT)){
                    Customer parentCustomer = customerService.getById(customer.getParentId());
                    //判断上级等级是不是vip
                    if(Objects.equals(parentCustomer.getLevel(), CUSTOPMERTWOLEVEL)){
                        //添加vip分红
                        BigDecimal value = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",VIPINVITECOMMONRECHARGEOILCOIN)).getValue());
                        BigDecimal parentAmount = BigDecimalUtils.multiply(value,amount);
                        parentCustomer.setCustomerCoin(parentAmount);
                        customerService.updateAddCustomerCoin(parentCustomer);
                        //添加分红记录
                        coinRecord.setCustomerId(parentCustomer.getId());
                        coinRecord.setCustomerPhone(parentCustomer.getPhone());
                        coinRecord.setAmount(parentAmount);
                        coinRecord.setType(RECHARGEREWARD);
                        coinRecordService.create(coinRecord);
                        //判断是否有上上级
                        if(!Objects.equals(customer.getSuperParentId(),NOTSUPERPARENT)){
                            Customer superParentCustomer = customerService.getById(customer.getSuperParentId());
                            //判断上上级是不是合伙人
                            if(Objects.equals(superParentCustomer.getLevel(), CUSTOPMERTHREELEVEL)){
                                //拿出拿出合伙人对vip分红百分比，添加分红
                                BigDecimal partnerRebate =  new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",INVITEVIPRECHARGEOILCOIN)).getValue());
                                BigDecimal superParentAmount = BigDecimalUtils.multiply(partnerRebate,amount);
                                superParentCustomer.setCustomerCoin(superParentAmount);
                                customerService.updateAddCustomerCoin(superParentCustomer);
                                //添加分红记录
                                coinRecord.setCustomerId(superParentCustomer.getId());
                                coinRecord.setCustomerPhone(superParentCustomer.getPhone());
                                coinRecord.setAmount(superParentAmount);
                                coinRecord.setType(RECHARGEREWARD);
                                coinRecordService.create(coinRecord);
                            }
                        }
                    }
                    //判断上级等级是不是合伙人，是合伙人上上级没有分红
                    if(Objects.equals(parentCustomer.getLevel(), CUSTOPMERTHREELEVEL)){
                        //计算合伙人对普通用户分红百分比，添加分红
                        BigDecimal vipRebate =  new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",INVITEVIPRECHARGEOILCOIN)).getValue());
                        BigDecimal normalRebate = new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",VIPINVITECOMMONRECHARGEOILCOIN)).getValue());
                        BigDecimal partnerRebate=vipRebate.add(normalRebate);
                        BigDecimal superParentAmount = BigDecimalUtils.multiply(partnerRebate,amount);
                        parentCustomer.setCustomerCoin(superParentAmount);
                        customerService.updateAddCustomerCoin(parentCustomer);
                        //添加分红记录
                        coinRecord.setCustomerId(parentCustomer.getId());
                        coinRecord.setCustomerPhone(parentCustomer.getPhone());
                        coinRecord.setAmount(superParentAmount);
                        coinRecord.setType(RECHARGEREWARD);
                        coinRecordService.create(coinRecord);
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
                        BigDecimal partnerRebate =  new BigDecimal(systemParamsService.getValueByKey(Collections.singletonMap("key",INVITEVIPRECHARGEOILCOIN)).getValue());
                        BigDecimal parentAmount = BigDecimalUtils.multiply(partnerRebate,amount);
                        parentCustomer.setCustomerCoin(parentAmount);
                        customerService.updateAddCustomerCoin(parentCustomer);
                        //添加分红记录
                        coinRecord.setCustomerId(parentCustomer.getId());
                        coinRecord.setCustomerPhone(parentCustomer.getPhone());
                        coinRecord.setAmount(parentAmount);
                        coinRecord.setType(RECHARGEREWARD);
                        coinRecordService.create(coinRecord);
                    }
                }
            }
        }
        Customer currentCustomer = customerService.getById(form.getId());
        return currentCustomer;
    }

    @RequestMapping(value = UPDATE,method = RequestMethod.PUT)
    public SuccessView update(@Valid @RequestBody OilDrillRecordUpdateForm form){
        OilDrillRecord oilDrillRecord = oilDrillRecordService.getById(form.getId());
        if(Objects.isNull(oilDrillRecord)){
            throw new ResourceNotFoundException("oilDrillRecord not exists");
        }
        BeanUtils.copyProperties(form,oilDrillRecord);
        oilDrillRecordService.update(oilDrillRecord);
        return new SuccessView();
    }
}
