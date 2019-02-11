package com.longyan.distribution.response;

import com.sug.core.platform.web.pagination.PaginationView;
import org.springframework.beans.factory.annotation.Autowired;
import com.longyan.distribution.domain.GoldRecord;

import java.util.List;

public class GoldRecordListView extends PaginationView<GoldRecord> {

    @Autowired
    public GoldRecordListView() {
    }

    public GoldRecordListView(List<GoldRecord> list, int count) {
        this.setList(list);
        this.setCount(count);
    }

    public GoldRecordListView(List<GoldRecord> list) {
        this.setList(list);
    }
}
