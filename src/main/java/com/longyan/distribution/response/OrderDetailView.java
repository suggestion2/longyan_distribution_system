package com.longyan.distribution.response;

import com.longyan.distribution.domain.Order;
import com.longyan.distribution.domain.OrderItem;

import java.util.List;

public class OrderDetailView {
    private Order order;

    private List<OrderItem> list;

    public OrderDetailView(Order order, List<OrderItem> list) {
        this.order = order;
        this.list = list;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getList() {
        return list;
    }

    public void setList(List<OrderItem> list) {
        this.list = list;
    }
}
