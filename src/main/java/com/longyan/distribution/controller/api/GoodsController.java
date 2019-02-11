package com.longyan.distribution.controller.api;

import com.longyan.distribution.context.SessionContext;
import com.longyan.distribution.domain.Goods;
import com.longyan.distribution.interceptor.UserLoginRequired;
import com.longyan.distribution.request.GoodsCreateForm;
import com.longyan.distribution.request.GoodsListForm;
import com.longyan.distribution.request.GoodsStatusForm;
import com.longyan.distribution.request.GoodsUpdateForm;
import com.longyan.distribution.response.GoodsListView;
import com.longyan.distribution.service.GoodsService;
import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.rest.view.ResponseView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.ServletContextResource;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

import static com.longyan.distribution.constants.CommonConstants.*;
import static com.longyan.distribution.constants.GoodsConstants.DISABLE;
import static com.longyan.distribution.constants.GoodsConstants.ENABLE;

@RestController("goodsApiController")
@RequestMapping(value = "/api/goods")
public class GoodsController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Value("${path.image}")
    private String imagePath;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = LIST,method = RequestMethod.POST)
    public GoodsListView list(@Valid @RequestBody GoodsListForm form){
        Map<String,Object> query = form.getQueryMap();
        query.put("status",ENABLE);
        return new GoodsListView(goodsService.selectList(query),goodsService.selectCount(query));
    }

    @RequestMapping(value = DETAIL,method = RequestMethod.GET)
    public Goods detail(@PathVariable Integer id){
        Goods goods = goodsService.getById(id);
        if(Objects.isNull(goods) || goods.getStatus().equals(DISABLE)){
            throw new ResourceNotFoundException("goods not found");
        }
        return goods;
    }

    @RequestMapping(value = "/image/{fileName}",method = RequestMethod.GET)
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) throws FileNotFoundException {
        File file = new File(imagePath + fileName);
        if(!file.exists()){
            throw new ResourceNotFoundException("image not found");
        }

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("image/jpeg;image/png"))
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(new FileInputStream(file)));

    }
}
