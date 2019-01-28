package com.longyan.distribution.response;

import com.sug.core.platform.web.pagination.PaginationView;
import org.springframework.beans.factory.annotation.Autowired;
import com.longyan.distribution.domain.SystemParams;

import java.util.List;

public class SystemParamsListView extends PaginationView<SystemParams> {

    @Autowired
    public SystemParamsListView() {
    }

    public SystemParamsListView(List<SystemParams> list, int count) {
        this.setList(list);
        this.setCount(count);
    }

    public SystemParamsListView(List<SystemParams> list) {
        this.setList(list);
    }
}
