package com.longyan.distribution.controller.management;

import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.GoodsCategoryStatusForm;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.rest.view.ResponseView;
import com.sug.core.rest.view.SuccessView;
import com.longyan.distribution.domain.GoodsCategory;
import com.longyan.distribution.service.GoodsCategoryService;
import com.longyan.distribution.request.GoodsCategoryCreateForm;
import com.longyan.distribution.request.GoodsCategoryUpdateForm;
import com.longyan.distribution.response.GoodsCategoryListView;
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

@RestController
@RequestMapping(value = "/management/goodsCategory")
@UserLoginRequired
public class GoodsCategoryController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsCategoryController.class);

    @Autowired
    private GoodsCategoryService goodsCategoryService;

    @Autowired
    private SessionContext sessionContext;

    @RequestMapping(value = LIST,method = RequestMethod.GET)
    public GoodsCategoryListView list(){
        return new GoodsCategoryListView(goodsCategoryService.selectAll());
    }

    @RequestMapping(value = CREATE,method = RequestMethod.POST)
    public ResponseView create(@Valid @RequestBody GoodsCategoryCreateForm form){
        GoodsCategory goodsCategory = new GoodsCategory();
        BeanUtils.copyProperties(form,goodsCategory);
        goodsCategory.setParentId(LEVEL_1_PARENT_ID);
        goodsCategory.setLevel(LEVEL_1);
        goodsCategory.setCreateBy(sessionContext.getUser().getId());
        goodsCategoryService.create(goodsCategory);
        return new ResponseView();
    }

    @RequestMapping(value = UPDATE,method = RequestMethod.PUT)
    public ResponseView update(@Valid @RequestBody GoodsCategoryUpdateForm form){
        GoodsCategory goodsCategory = goodsCategoryService.getById(form.getId());
        if(Objects.isNull(goodsCategory)){
            throw new ResourceNotFoundException("goodsCategory not exists");
        }
        BeanUtils.copyProperties(form,goodsCategory);
        goodsCategory.setParentId(LEVEL_1_PARENT_ID);
        goodsCategory.setLevel(LEVEL_1);
        goodsCategory.setUpdateBy(sessionContext.getUser().getId());
        goodsCategoryService.update(goodsCategory);
        return new ResponseView();
    }

    @RequestMapping(value = "/resetStatus",method = RequestMethod.PUT)
    public ResponseView resetStatus(@Valid @RequestBody GoodsCategoryStatusForm form){
        GoodsCategory goodsCategory = goodsCategoryService.getById(form.getId());
        if(Objects.isNull(goodsCategory)){
            throw new ResourceNotFoundException("goodsCategory not exists");
        }
        BeanUtils.copyProperties(form,goodsCategory);
        goodsCategory.setUpdateBy(sessionContext.getUser().getId());
        goodsCategoryService.updateStatus(goodsCategory);
        return new ResponseView();
    }

    @RequestMapping(value = DELETE_BY_ID,method = RequestMethod.DELETE)
    public ResponseView deleteById(@PathVariable Integer id){
        GoodsCategory goodsCategory = goodsCategoryService.getById(id);
        if(Objects.isNull(goodsCategory)){
            throw new ResourceNotFoundException("goodsCategory not exists");
        }
        goodsCategoryService.deleteById(id);
        return new ResponseView();
    }
}
