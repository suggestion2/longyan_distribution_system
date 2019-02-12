package com.longyan.distribution.controller.management;

import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.GoodsStatusForm;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.rest.view.ResponseView;
import com.sug.core.rest.view.SuccessView;
import com.longyan.distribution.domain.Goods;
import com.longyan.distribution.service.GoodsService;
import com.longyan.distribution.request.GoodsCreateForm;
import com.longyan.distribution.request.GoodsUpdateForm;
import com.longyan.distribution.request.GoodsListForm;
import com.longyan.distribution.response.GoodsListView;
import com.sun.deploy.net.URLEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import static com.longyan.distribution.constants.CommonConstants.*;

@RestController
@RequestMapping(value = "/management/goods")
@UserLoginRequired
public class GoodsController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SessionContext sessionContext;

    @Value("${path.image}")
    private String imagePath;

    @RequestMapping(value = LIST,method = RequestMethod.POST)
    public GoodsListView list(@Valid @RequestBody GoodsListForm form){
        return new GoodsListView(goodsService.selectList(form.getQueryMap()),goodsService.selectCount(form.getQueryMap()));
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseView studentInfoSave(@RequestParam("file") MultipartFile file) throws IOException {
        String filename = URLEncoder.encode(file.getOriginalFilename(), "utf-8");
        if(!file.isEmpty()) {
            file.transferTo(new java.io.File(imagePath + filename));
        }
        return new ResponseView();
    }


    @RequestMapping(value = DETAIL,method = RequestMethod.GET)
    public Goods detail(@PathVariable Integer id){
        Goods goods = goodsService.getById(id);
        if(Objects.isNull(goods)){
            throw new ResourceNotFoundException("goods not found");
        }
        return goods;
    }

    @RequestMapping(value = CREATE,method = RequestMethod.POST)
    public ResponseView create(@Valid @RequestBody GoodsCreateForm form){
        Goods goods = new Goods();
        BeanUtils.copyProperties(form,goods);
        goods.setCreateBy(sessionContext.getUser().getId());
        goodsService.create(goods);
        return new ResponseView();
    }

    @RequestMapping(value = UPDATE,method = RequestMethod.PUT)
    public ResponseView update(@Valid @RequestBody GoodsUpdateForm form){
        Goods goods = goodsService.getById(form.getId());
        if(Objects.isNull(goods)){
            throw new ResourceNotFoundException("goods not exists");
        }
        BeanUtils.copyProperties(form,goods);
        goods.setUpdateBy(sessionContext.getUser().getId());
        goodsService.update(goods);
        return new ResponseView();
    }

    @RequestMapping(value = "/resetStatus",method = RequestMethod.PUT)
    public ResponseView resetStatus(@Valid @RequestBody GoodsStatusForm form){
        Goods goods = goodsService.getById(form.getId());
        if(Objects.isNull(goods)){
            throw new ResourceNotFoundException("goods not exists");
        }
        BeanUtils.copyProperties(form,goods);
        goods.setUpdateBy(sessionContext.getUser().getId());
        goodsService.updateStatus(goods);
        return new ResponseView();
    }

    @RequestMapping(value = DELETE_BY_ID,method = RequestMethod.DELETE)
    public ResponseView deleteById(@PathVariable Integer id){
        Goods goods = goodsService.getById(id);
        if(Objects.isNull(goods)){
            throw new ResourceNotFoundException("goods not exists");
        }
        goodsService.deleteById(id);
        return new ResponseView();
    }
}
