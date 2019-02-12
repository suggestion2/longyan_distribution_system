package com.longyan.distribution.controller.api;

import com.longyan.distribution.constants.SystemParamsConstants;
import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.SystemParams;
import com.longyan.distribution.interceptor.CustomerLoginRequired;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.SystemParamsUpdateForm;
import com.longyan.distribution.response.SystemParamsListView;
import com.longyan.distribution.service.SystemParamsService;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.rest.view.ResponseView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

import static com.longyan.distribution.constants.CommonConstants.LIST;
import static com.longyan.distribution.constants.CommonConstants.UPDATE;

@RestController("SystemParamsApiController")
@RequestMapping(value = "/api/systemParams")
@CustomerLoginRequired
public class SystemParamsController {

    private static final Logger logger = LoggerFactory.getLogger(SystemParamsController.class);

    @Autowired
    private SystemParamsService systemParamsService;


    @RequestMapping(value = LIST,method = RequestMethod.GET)
    public SystemParamsListView list(){
        return new SystemParamsListView(systemParamsService.selectAll());
    }
}
