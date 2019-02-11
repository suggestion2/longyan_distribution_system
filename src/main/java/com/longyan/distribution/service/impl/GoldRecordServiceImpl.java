package com.longyan.distribution.service.impl;

import com.longyan.distribution.domain.GoldRecord;
import com.longyan.distribution.service.GoldRecordService;
import com.longyan.distribution.mapper.GoldRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoldRecordServiceImpl implements GoldRecordService{

    @Autowired
    private GoldRecordMapper goldRecordMapper;

    @Override
    public GoldRecord getById(Integer id){
        return goldRecordMapper.selectById(id);
    }
    @Override
    public GoldRecord select(Map<String, Object> map){
        return goldRecordMapper.select(map);
    }

    @Override
    public List<GoldRecord> selectList(Map<String, Object> map){
        return goldRecordMapper.selectList(map);
    }

    @Override
    public int selectCount(Map<String, Object> map){
        return goldRecordMapper.selectCount(map);
    }

    @Override
    public int create(GoldRecord goldRecord){
        return goldRecordMapper.insert(goldRecord);
    }

    @Override
    public int update(GoldRecord goldRecord){
        return goldRecordMapper.update(goldRecord);
    }

    @Override
    public int updateStatus(GoldRecord goldRecord){
        return goldRecordMapper.updateStatus(goldRecord);
    }

    @Override
    public int deleteById(Integer id){
        return goldRecordMapper.deleteById(id);
    }
}