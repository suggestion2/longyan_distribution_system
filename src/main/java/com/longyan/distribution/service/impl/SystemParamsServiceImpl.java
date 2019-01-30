package com.longyan.distribution.service.impl;

import com.longyan.distribution.domain.SystemParams;
import com.longyan.distribution.service.SystemParamsService;
import com.longyan.distribution.mapper.SystemParamsMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemParamsServiceImpl implements SystemParamsService{

    @Autowired
    private SystemParamsMapper systemParamsMapper;

    @Override
    public SystemParams getById(Integer id){
        return systemParamsMapper.selectById(id);
    }

    @Override
    public SystemParams getValueByKey(Map<String, Object> map){
        return systemParamsMapper.getValueByKey(map);
    }
    @Override
    public SystemParams select(Map<String, Object> map){
        return systemParamsMapper.select(map);
    }

    @Override
    public List<SystemParams> selectList(Map<String, Object> map){
        return systemParamsMapper.selectList(map);
    }

    @Override
    public List<SystemParams> selectAll() {
        return systemParamsMapper.selectList(null);
    }

    @Override
    public int selectCount(Map<String, Object> map){
        return systemParamsMapper.selectCount(map);
    }

    @Override
    public int create(SystemParams systemParams){
        return systemParamsMapper.insert(systemParams);
    }

    @Override
    public int update(SystemParams systemParams){
        return systemParamsMapper.update(systemParams);
    }

    @Override
    public int deleteById(Integer id){
        return systemParamsMapper.deleteById(id);
    }
}