package com.longyan.distribution.service.impl;

import com.longyan.distribution.domain.OilDrillRecord;
import com.longyan.distribution.service.OilDrillRecordService;
import com.longyan.distribution.mapper.OilDrillRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OilDrillRecordServiceImpl implements OilDrillRecordService{

    @Autowired
    private OilDrillRecordMapper oilDrillRecordMapper;

    @Override
    public OilDrillRecord getById(Integer id){
        return oilDrillRecordMapper.selectById(id);
    }
    @Override
    public OilDrillRecord select(Map<String, Object> map){
        return oilDrillRecordMapper.select(map);
    }

    @Override
    public List<OilDrillRecord> selectList(Map<String, Object> map){
        return oilDrillRecordMapper.selectList(map);
    }

    @Override
    public List<OilDrillRecord> selectOilRecordAndCustomerList(Map<String, Object> map){
        return oilDrillRecordMapper.selectOilRecordAndCustomerList(map);
    }

    @Override
    public int selectCount(Map<String, Object> map){
        return oilDrillRecordMapper.selectCount(map);
    }

    @Override
    public int create(OilDrillRecord oilDrillRecord){
        return oilDrillRecordMapper.insert(oilDrillRecord);
    }

    @Override
    public int update(OilDrillRecord oilDrillRecord){
        return oilDrillRecordMapper.update(oilDrillRecord);
    }

    @Override
    public int updateStatus(OilDrillRecord oilDrillRecord){
        return oilDrillRecordMapper.updateStatus(oilDrillRecord);
    }

    @Override
    public int deleteById(Integer id){
        return oilDrillRecordMapper.deleteById(id);
    }
}