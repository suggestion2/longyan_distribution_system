package com.longyan.distribution.service.impl;

import com.longyan.distribution.domain.GoodsCategory;
import com.longyan.distribution.service.GoodsCategoryService;
import com.longyan.distribution.mapper.GoodsCategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.longyan.distribution.constants.GoodsCategoryConstants.ENABLE;

@Service
public class GoodsCategoryServiceImpl implements GoodsCategoryService{

    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Override
    public GoodsCategory getById(Integer id){
        return goodsCategoryMapper.selectById(id);
    }
    @Override
    public GoodsCategory select(Map<String, Object> map){
        return goodsCategoryMapper.select(map);
    }

    @Override
    public List<GoodsCategory> selectList(Map<String, Object> map){
        return goodsCategoryMapper.selectList(map);
    }

    @Override
    public List<GoodsCategory> selectAll() {
        return goodsCategoryMapper.selectList(null);
    }

    @Override
    public List<GoodsCategory> selectAllEnable() {
        return goodsCategoryMapper.selectList(Collections.singletonMap("status",ENABLE));
    }

    @Override
    public int selectCount(Map<String, Object> map){
        return goodsCategoryMapper.selectCount(map);
    }

    @Override
    public int create(GoodsCategory goodsCategory){
        return goodsCategoryMapper.insert(goodsCategory);
    }

    @Override
    public int update(GoodsCategory goodsCategory){
        return goodsCategoryMapper.update(goodsCategory);
    }

    @Override
    public int updateStatus(GoodsCategory goodsCategory) {
        return goodsCategoryMapper.updateStatus(goodsCategory);
    }

    @Override
    public int deleteById(Integer id){
        return goodsCategoryMapper.deleteById(id);
    }
}