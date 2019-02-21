package com.longyan.distribution.controller.api;

import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.*;
import com.longyan.distribution.interceptor.CustomerLoginRequired;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.*;
import com.longyan.distribution.response.OrderCreateView;
import com.longyan.distribution.response.OrderDetailView;
import com.longyan.distribution.response.OrderListView;
import com.longyan.distribution.service.*;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.rest.view.ResponseView;
import com.sug.core.util.BigDecimalUtils;
import com.sug.core.util.SequenceNumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

import static com.longyan.distribution.constants.CommonConstants.*;
import static com.longyan.distribution.constants.GoldRecordConstans.*;
import static com.longyan.distribution.constants.GoodsConstants.*;
import static com.longyan.distribution.constants.OrderConstants.*;
import static com.longyan.distribution.constants.SystemParamsConstants.COMMONGOLDCHARGE;

@RestController("orderApiController")
@RequestMapping(value = "/api/order")
@CustomerLoginRequired
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoldRecordService goldRecordService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private SystemParamsService systemParamsService;

    @RequestMapping(value = LIST, method = RequestMethod.POST)
    public OrderListView list(@Valid @RequestBody OrderListForm form) {
        Map<String,Object> query = form.getQueryMap();
        query.put("customerId", sessionContext.getCustomerId());
        return new OrderListView(orderService.selectList(query), orderService.selectCount(query));
    }

    @RequestMapping(value = DETAIL, method = RequestMethod.GET)
    public OrderDetailView detail(@PathVariable Integer id) {
        Order order = orderService.getById(id);
        if (Objects.isNull(order) || !order.getCustomerId().equals(sessionContext.getCustomerId())) {
            throw new ResourceNotFoundException("order not found");
        }
        List<OrderItem> list = orderItemService.getListByOrderId(id);
        return new OrderDetailView(order, list);
    }

    @RequestMapping(value = CREATE, method = RequestMethod.POST)
    @Transactional
    public OrderDetailView create(@Valid @RequestBody OrderCreateForm form) {
//        //不是vip卡不能下单
//        if(form.getList().size()>0&&Objects.equals(form.getRecharge(),NORMAL_GOODSNAME)){
//            for(OrderItemCreateForm list:form.getList()){
//                if(!Objects.equals(list.getGoodsId(), Integer.valueOf(systemParamsService.getValueByKey(Collections.singletonMap("key", VIPCARD)).getValue()))){
//                    throw new InvalidRequestException("不是会员vip升级卡不能下单");
//                }
//            }
//        }
        Customer customer = sessionContext.getCustomer();
        if(Objects.isNull(customer.getAddress()) && form.getRecharge().equals(NORMAL_ORDER)){
            throw new InvalidRequestException("请先填写收货地址");
        }
        Order order = new Order();
        BeanUtils.copyProperties(form, order);
        order.setCustomerId(customer.getId());
        order.setCustomerRealName(customer.getRealName());
        order.setNumber(SequenceNumUtils.generateNum());
        order.setCreateBy(0);
        if (form.getRecharge().equals(RECHARGE_ORDER)) {
            order.setCount(0);
            order.setGoodsNames(RECHARGE_GOODSNAME);
            orderService.create(order);
            return new OrderDetailView(order,new ArrayList<>());
        }
        //判断用户拥有的金币是否足够购买商品
//        BigDecimal amount = BigDecimalUtils.multiply(form.getList().get(0).getPrice(),form.getList().stream().mapToInt(OrderItemCreateForm::getCount).sum());
        if(Objects.equals(customer.getCustomerGold().compareTo(form.getAmount()),-1)&&Objects.equals(order.getRecharge(),NORMAL_ORDER)){
            throw new InvalidRequestException("用户金币不足不能下单");
        }
        order.setCount(form.getList().stream().mapToInt(OrderItemCreateForm::getCount).sum());
        order.setGoodsNames(form.getList().get(0).getGoodsName());
            order.setAddress(customer.getAddress());
        orderService.create(order);
        List<OrderItem> itemList = new ArrayList<>();
        for (OrderItemCreateForm itemCreateForm : form.getList()) {
            OrderItem orderItem = new OrderItem();
            BeanUtils.copyProperties(itemCreateForm, orderItem);
            orderItem.setAmount(BigDecimalUtils.multiply(orderItem.getPrice(), orderItem.getCount()));
            orderItem.setOrderId(order.getId());
            orderItem.setCreateBy(0);
            itemList.add(orderItem);
        }

        orderItemService.batchCreate(itemList);
        return new OrderDetailView(order,itemList);
    }

    @RequestMapping(value = "/paid", method = RequestMethod.PUT)
    public ResponseView paid(@Valid @RequestBody OrderPaidForm form) {
        Customer customer = sessionContext.getCustomer();

        Order order = orderService.getById(form.getId());
        if(Objects.isNull(order) || !order.getCustomerId().equals(customer.getId())){
            throw new ResourceNotFoundException("order not found");
        }
        if(!order.getStatus().equals(CREATED)){
            throw new InvalidRequestException("invalidStatus","invalid status");
        }

        order.setStatus(PAID);
        order.setUpdateBy(0);
        orderService.updateStatus(order);

        return new ResponseView();
    }
}
