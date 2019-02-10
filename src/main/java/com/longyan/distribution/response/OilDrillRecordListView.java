package com.longyan.distribution.response;

import com.sug.core.platform.web.pagination.PaginationView;
import org.springframework.beans.factory.annotation.Autowired;
import com.longyan.distribution.domain.OilDrillRecord;

import java.util.List;

public class OilDrillRecordListView extends PaginationView<OilDrillRecord>{

        @Autowired
        public OilDrillRecordListView() {
        }

        public OilDrillRecordListView(List<OilDrillRecord> list,int count) {
            this.setList(list);
            this.setCount(count);
        }

        public OilDrillRecordListView(List<OilDrillRecord> list) {
            this.setList(list);
        }
}
