package com.longyan.distribution.controller.api;

import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.domain.Order;
import com.longyan.distribution.domain.OrderItem;
import com.longyan.distribution.domain.User;
import com.longyan.distribution.interceptor.CustomerLoginRequired;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.*;
import com.longyan.distribution.response.OrderCreateView;
import com.longyan.distribution.response.OrderDetailView;
import com.longyan.distribution.response.OrderListView;
import com.longyan.distribution.service.GoodsService;
import com.longyan.distribution.service.OrderItemService;
import com.longyan.distribution.service.OrderService;
import com.sug.core.platform.exception.ResourceNotFoundException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.longyan.distribution.constants.CommonConstants.CREATE;
import static com.longyan.distribution.constants.CommonConstants.DETAIL;
import static com.longyan.distribution.constants.CommonConstants.LIST;
import static com.longyan.distribution.constants.GoodsConstants.ENABLE;
import static com.longyan.distribution.constants.GoodsConstants.RECHARGE_GOODSNAME;
import static com.longyan.distribution.constants.OrderConstants.PAID;

@RestController("orderApiController")
@RequestMapping(value = "/api/order")
@CustomerLoginRequired
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private SessionContext sessionContext;

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
    public OrderCreateView create(@Valid @RequestBody OrderCreateForm form) {
        Customer customer = sessionContext.getCustomer();

        Order order = new Order();
        BeanUtils.copyProperties(form, order);
        order.setCustomerId(customer.getId());
        order.setCustomerRealName(customer.getRealName());
        order.setNumber(SequenceNumUtils.generateNum());
        order.setCreateBy(0);
        if (Objects.isNull(form.getList()) || form.getList().size() == 0) {
            order.setCount(0);
            order.setGoodsNames(RECHARGE_GOODSNAME);
            orderService.create(order);
            return new OrderCreateView(order.getId());
        }

        order.setCount(form.getList().stream().mapToInt(OrderItemCreateForm::getCount).sum());
        order.setGoodsNames(form.getList().get(0).getGoodsName());
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
        return new OrderCreateView(order.getId());
    }

    @RequestMapping(value = "/paid", method = RequestMethod.PUT)
    public ResponseView paid(@Valid @RequestBody OrderPaidForm form) {
        Customer customer = sessionContext.getCustomer();

        Order order = orderService.getById(form.getId());
        if(Objects.isNull(order) || !order.getCustomerId().equals(customer.getId())){
            throw new ResourceNotFoundException("order not found");
        }

        order.setStatus(PAID);
        order.setUpdateBy(0);
        orderService.updateStatus(order);

        return new ResponseView();
    }
}