package com.longyan.distribution.controller.management;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.longyan.distribution.constants.OrderConstants;
import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.domain.GoldRecord;
import com.longyan.distribution.domain.OrderItem;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.OrderStatusForm;
import com.longyan.distribution.response.OrderDetailView;
import com.longyan.distribution.service.*;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.rest.view.ResponseView;
import com.sug.core.rest.view.SuccessView;
import com.longyan.distribution.domain.Order;
import com.longyan.distribution.request.OrderListForm;
import com.longyan.distribution.response.OrderListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.longyan.distribution.constants.CommonConstants.*;
import static com.longyan.distribution.constants.GoldRecordConstans.CONSUMPTION;
import static com.longyan.distribution.constants.GoldRecordConstans.NOTBUSINESS;
import static com.longyan.distribution.constants.OrderConstants.*;

@RestController
@RequestMapping(value = "/management/order")
@UserLoginRequired
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoldRecordService goldRecordService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = LIST,method = RequestMethod.POST)
    public OrderListView list(@Valid @RequestBody OrderListForm form){
        Map<String,Object> query = form.getQueryMap();
        if(Objects.nonNull(form.getStatus()) && form.getStatus().equals(1)){
            query.put("pending",form.getStatus());
            query.remove("status");
        }
        return new OrderListView(orderService.selectList(query),orderService.selectCount(query));
    }

    @RequestMapping(value = DETAIL,method = RequestMethod.GET)
    public OrderDetailView detail(@PathVariable Integer id){
        Order order = orderService.getById(id);
        if(Objects.isNull(order)){
            throw new ResourceNotFoundException("order not found");
        }
        List<OrderItem> list = orderItemService.getListByOrderId(id);
        return new OrderDetailView(order,list);
    }

    @RequestMapping(value = "/resetStatus",method = RequestMethod.PUT)
    public ResponseView resetStatus(@Valid @RequestBody OrderStatusForm form){
        Order order = orderService.getById(form.getId());
        if(Objects.isNull(order)){
            throw new ResourceNotFoundException("order not exists");
        }
        if(!order.getStatus().equals(PAID) && !order.getStatus().equals(CREATED)){
            throw new InvalidRequestException("invalidOrderStatus","invalid order status");
        }
        if(!form.getStatus().equals(CONFIRM) && !form.getStatus().equals(OrderConstants.CANCEL)){
            throw new InvalidRequestException("invalidStatus","invalid status");
        }
        BeanUtils.copyProperties(form,order);
        //如果是普通订单,并且后台确认了
        if(Objects.equals(order.getRecharge(),NORMAL_ORDER)&&Objects.equals(order.getStatus(),CONFIRM)){
            //判断用户金币够不够
            Customer customer = customerService.getById(order.getCustomerId());
            if(Objects.isNull(customer)){
                throw new ResourceNotFoundException("用户没有找到");
            }
            if(Objects.equals(customer.getBusinessGold().compareTo(order.getAmount()),-1)){
                throw new InvalidRequestException("用户金币不足","用户金币不足");
            }
            //减少用户金币
            customer.setCustomerGold(order.getAmount());
            customerService.updateReduceCustomerGold(customer);
            //金币记录
            GoldRecord goldRecord = new GoldRecord();
            goldRecord.setCustomerId(order.getCustomerId());
            //减少是负数
            goldRecord.setBusinessId(NOTBUSINESS);
            goldRecord.setAmount(order.getAmount().multiply(new BigDecimal(-1)));
            goldRecord.setType(CONSUMPTION);
            goldRecord.setBusinessAccount("");
            goldRecord.setBusinessName("");
            goldRecord.setCustomerPhone(customer.getPhone());
            goldRecord.setCreateBy(CREATE_BY_SERVER);
            goldRecordService.create(goldRecord);
            order.setRemark(form.getRemarks());
            order.setUpdateBy(sessionContext.getUser().getId());

            if(Objects.equals(orderService.updateStatus(order),UPDATE_FAIL)){

            }orderService.updateStatus(order);
            return new ResponseView();
        }
        order.setRefuseReason(form.getCancelReason());
        order.setRemark(form.getRemarks());
        order.setUpdateBy(sessionContext.getUser().getId());
        orderService.updateStatus(order);
        return new ResponseView();
    }
}
