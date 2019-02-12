package com.longyan.distribution.controller.api;

import com.longyan.distribution.constants.GoldRecordConstans;
import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.CoinRecord;
import com.longyan.distribution.domain.Customer;
import com.longyan.distribution.domain.GoldRecord;
import com.longyan.distribution.interceptor.BusinessRequired;
import com.longyan.distribution.interceptor.CustomerLoginRequired;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.*;
import com.longyan.distribution.response.GoldRecordHandleView;
import com.longyan.distribution.response.GoldRecordListView;
import com.longyan.distribution.service.CoinRecordService;
import com.longyan.distribution.service.CustomerService;
import com.longyan.distribution.service.GoldRecordService;
import com.longyan.distribution.service.SystemParamsService;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.web.pagination.PaginationForm;
import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.rest.view.ResponseView;
import com.sug.core.rest.view.SuccessView;
import com.sug.core.util.BigDecimalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static com.longyan.distribution.constants.CoinRecordConstants.RECHARGEREWARD;
import static com.longyan.distribution.constants.CommonConstants.*;
import static com.longyan.distribution.constants.CustomerConstants.*;
import static com.longyan.distribution.constants.GoldRecordConstans.*;
import static com.longyan.distribution.constants.OilDrillConstants.REDUCEFAIL;
import static com.longyan.distribution.constants.SystemParamsConstants.*;

@RestController("goldRecordApiController")
@RequestMapping(value = "/api/goldRecord")
@CustomerLoginRequired
public class GoldRecordController {

    private static final Logger logger = LoggerFactory.getLogger(GoldRecordController.class);

    @Autowired
    private GoldRecordService goldRecordService;


    @Autowired
    private SessionContext sessionContext;

    @RequestMapping(value = "/goldRecordList",method = RequestMethod.POST)
    public GoldRecordListView goldRecordList(@Valid @RequestBody PaginationForm form){
        Map<String,Object> query = form.getQueryMap();
        query.put("customerId",sessionContext.getCustomerId());
        return new GoldRecordListView(goldRecordService.selectList(query),goldRecordService.selectCount(query));
    }
}
