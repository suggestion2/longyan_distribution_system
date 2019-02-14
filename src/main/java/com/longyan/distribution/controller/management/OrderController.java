package com.longyan.distribution.controller.management;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.longyan.distribution.constants.OrderConstants;
import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.OrderItem;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.OrderStatusForm;
import com.longyan.distribution.response.OrderDetailView;
import com.longyan.distribution.service.OrderItemService;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.rest.view.ResponseView;
import com.sug.core.rest.view.SuccessView;
import com.longyan.distribution.domain.Order;
import com.longyan.distribution.service.OrderService;
import com.longyan.distribution.request.OrderListForm;
import com.longyan.distribution.response.OrderListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.longyan.distribution.constants.CommonConstants.*;
import static com.longyan.distribution.constants.OrderConstants.CONFIRM;
import static com.longyan.distribution.constants.OrderConstants.CREATED;
import static com.longyan.distribution.constants.OrderConstants.PAID;

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
        order.setRefuseReason(form.getCancelReason());
        order.setRemark(form.getRemarks());
        order.setUpdateBy(sessionContext.getUser().getId());
        orderService.updateStatus(order);
        return new ResponseView();
    }
}
