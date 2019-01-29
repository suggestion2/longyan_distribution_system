package com.longyan.distribution.response;

import com.longyan.distribution.domain.Customer;
import com.sug.core.platform.web.pagination.PaginationView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class BusinessListView extends PaginationView<BusinessView> {
    @Autowired
    public BusinessListView() {
    }

    public BusinessListView(List<BusinessView> list, int count) {
        this.setList(list);
        this.setCount(count);
    }

    public BusinessListView(List<BusinessView> list) {
        this.setList(list);
    }
}
