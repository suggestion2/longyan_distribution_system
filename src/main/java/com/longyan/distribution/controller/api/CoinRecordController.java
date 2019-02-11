package com.longyan.distribution.controller.api;

import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.rest.view.SuccessView;
import com.longyan.distribution.domain.CoinRecord;
import com.longyan.distribution.service.CoinRecordService;
import com.longyan.distribution.request.CoinRecordCreateForm;
import com.longyan.distribution.request.CoinRecordUpdateForm;
import com.longyan.distribution.request.CoinRecordListForm;
import com.longyan.distribution.response.CoinRecordListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Objects;

import static com.longyan.distribution.constants.CommonConstants.*;

@RestController("CoinRecordApiController")
@RequestMapping(value = "/coinRecord")
public class CoinRecordController {

    private static final Logger logger = LoggerFactory.getLogger(CoinRecordController.class);

    @Autowired
    private CoinRecordService coinRecordService;

    @RequestMapping(value = LIST,method = RequestMethod.POST)
    public CoinRecordListView list(@Valid @RequestBody CoinRecordListForm form){
        return new CoinRecordListView(coinRecordService.selectList(form.getQueryMap()));
    }

    @RequestMapping(value = DETAIL,method = RequestMethod.GET)
    public CoinRecord detail(@PathVariable Integer id){
        return coinRecordService.getById(id);
    }

    @RequestMapping(value = CREATE,method = RequestMethod.POST)
    public SuccessView create(@Valid @RequestBody CoinRecordCreateForm form){
        CoinRecord coinRecord = new CoinRecord();
        BeanUtils.copyProperties(form,coinRecord);
        coinRecordService.create(coinRecord);
        return new SuccessView();
    }

    @RequestMapping(value = UPDATE,method = RequestMethod.PUT)
    public SuccessView update(@Valid @RequestBody CoinRecordUpdateForm form){
        CoinRecord coinRecord = coinRecordService.getById(form.getId());
        if(Objects.isNull(coinRecord)){
            throw new ResourceNotFoundException("coinRecord not exists");
        }
        BeanUtils.copyProperties(form,coinRecord);
        coinRecordService.update(coinRecord);
        return new SuccessView();
    }
}
