package com.longyan.distribution.controller.api;

import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.GoodsCategory;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.GoodsCategoryCreateForm;
import com.longyan.distribution.request.GoodsCategoryStatusForm;
import com.longyan.distribution.request.GoodsCategoryUpdateForm;
import com.longyan.distribution.response.GoodsCategoryListView;
import com.longyan.distribution.service.GoodsCategoryService;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.rest.view.ResponseView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

import static com.longyan.distribution.constants.CommonConstants.*;
import static com.longyan.distribution.constants.GoodsCategoryConstants.LEVEL_1;
import static com.longyan.distribution.constants.GoodsCategoryConstants.LEVEL_1_PARENT_ID;

@RestController(value = "goodsCategoryApiController")
@RequestMapping(value = "/api/goodsCategory")
public class GoodsCategoryController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsCategoryController.class);

    @Autowired
    private GoodsCategoryService goodsCategoryService;

    @RequestMapping(value = LIST,method = RequestMethod.GET)
    public GoodsCategoryListView list(){
        return new GoodsCategoryListView(goodsCategoryService.selectAllEnable());
    }
}
