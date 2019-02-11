package com.longyan.distribution.response;

import com.sug.core.platform.web.pagination.PaginationView;
import org.springframework.beans.factory.annotation.Autowired;
import com.longyan.distribution.domain.CoinRecord;

import java.util.List;

public class CoinRecordListView extends PaginationView<CoinRecord>{

    @Autowired
    public CoinRecordListView() {
    }

    public CoinRecordListView(List<CoinRecord> list,int count) {
        this.setList(list);
        this.setCount(count);
    }

    public CoinRecordListView(List<CoinRecord> list) {
        this.setList(list);
    }
}
