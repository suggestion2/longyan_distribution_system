package com.longyan.distribution.response;

import com.sug.core.platform.web.pagination.PaginationView;
import org.springframework.beans.factory.annotation.Autowired;
import com.longyan.distribution.domain.Customer;

import java.util.List;

public class CustomerListView extends PaginationView<Customer>{

        @Autowired
        public CustomerListView() {
        }

        public CustomerListView(List<Customer> list,int count) {
            this.setList(list);
            this.setCount(count);
        }

        public CustomerListView(List<Customer> list) {
            this.setList(list);
        }
}
