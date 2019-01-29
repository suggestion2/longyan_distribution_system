package com.longyan.distribution.response;


import com.longyan.distribution.domain.Customer;
import com.sug.core.platform.web.pagination.PaginationView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CustomerListShortView extends PaginationView<CustomerShortView> {
    @Autowired
    public CustomerListShortView() {
    }

    public CustomerListShortView(List<CustomerShortView> list, int count) {
        this.setList(list);
        this.setCount(count);
    }

    public CustomerListShortView(List<CustomerShortView> list) {
        this.setList(list);
    }
}
