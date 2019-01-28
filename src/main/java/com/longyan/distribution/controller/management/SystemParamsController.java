package com.longyan.distribution.controller.management;

import com.longyan.distribution.constants.SystemParamsConstants;
import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.platform.web.rest.exception.InvalidRequestException;
import com.sug.core.rest.view.ResponseView;
import com.sug.core.rest.view.SuccessView;
import com.longyan.distribution.domain.SystemParams;
import com.longyan.distribution.service.SystemParamsService;
import com.longyan.distribution.request.SystemParamsUpdateForm;
import com.longyan.distribution.response.SystemParamsListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Objects;

import static com.longyan.distribution.constants.CommonConstants.*;

@RestController
@RequestMapping(value = "/management/systemParams")
@UserLoginRequired
public class SystemParamsController {

    private static final Logger logger = LoggerFactory.getLogger(SystemParamsController.class);

    @Autowired
    private SystemParamsService systemParamsService;

    @Autowired
    private SessionContext sessionContext;

    @RequestMapping(value = LIST,method = RequestMethod.GET)
    public SystemParamsListView list(){
        return new SystemParamsListView(systemParamsService.selectAll());
    }

    @RequestMapping(value = UPDATE,method = RequestMethod.PUT)
    public ResponseView update(@Valid @RequestBody SystemParamsUpdateForm form){
        SystemParams systemParams = systemParamsService.getById(form.getId());
        if(Objects.isNull(systemParams)){
            throw new ResourceNotFoundException("systemParams not exists");
        }
        if(!SystemParamsConstants.matches(systemParams.getKey(),form.getValue())){
            throw new InvalidRequestException("invalidRegex","invalid regex");
        }
        BeanUtils.copyProperties(form,systemParams);
        systemParams.setUpdateBy(sessionContext.getUser().getId());
        systemParamsService.update(systemParams);
        return new ResponseView();
    }
}
