package com.longyan.distribution.controller.management;

import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.rest.view.SuccessView;
import com.longyan.distribution.domain.Goods;
import com.longyan.distribution.service.GoodsService;
import com.longyan.distribution.request.GoodsCreateForm;
import com.longyan.distribution.request.GoodsUpdateForm;
import com.longyan.distribution.request.GoodsListForm;
import com.longyan.distribution.response.GoodsListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Objects;

import static com.longyan.distribution.constants.CommonConstants.*;

@RestController
@RequestMapping(value = "/goods")
public class GoodsController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = LIST,method = RequestMethod.POST)
    public GoodsListView list(@Valid @RequestBody GoodsListForm form){
        return new GoodsListView(goodsService.selectList(form.getQueryMap()));
    }

    @RequestMapping(value = DETAIL,method = RequestMethod.GET)
    public Goods detail(@PathVariable Integer id){
        return goodsService.getById(id);
    }

    @RequestMapping(value = CREATE,method = RequestMethod.POST)
    public SuccessView create(@Valid @RequestBody GoodsCreateForm form){
        Goods goods = new Goods();
        BeanUtils.copyProperties(form,goods);
        goodsService.create(goods);
        return new SuccessView();
    }

    @RequestMapping(value = UPDATE,method = RequestMethod.PUT)
    public SuccessView update(@Valid @RequestBody GoodsUpdateForm form){
        Goods goods = goodsService.getById(form.getId());
        if(Objects.isNull(goods)){
            throw new ResourceNotFoundException("goods not exists");
        }
        BeanUtils.copyProperties(form,goods);
        goodsService.update(goods);
        return new SuccessView();
    }
}
