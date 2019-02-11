package com.longyan.distribution.service.impl;

import com.longyan.distribution.domain.CoinRecord;
import com.longyan.distribution.service.CoinRecordService;
import com.longyan.distribution.mapper.CoinRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CoinRecordServiceImpl implements CoinRecordService{

    @Autowired
    private CoinRecordMapper coinRecordMapper;

    @Override
    public CoinRecord getById(Integer id){
        return coinRecordMapper.selectById(id);
    }
    @Override
    public CoinRecord select(Map<String, Object> map){
        return coinRecordMapper.select(map);
    }

    @Override
    public List<CoinRecord> selectList(Map<String, Object> map){
        return coinRecordMapper.selectList(map);
    }

    @Override
    public List<CoinRecord> selectCoinRecordAndCustomerList(Map<String, Object> map){
        return coinRecordMapper.selectCoinRecordAndCustomerList(map);
    }

    @Override
    public int selectCount(Map<String, Object> map){
        return coinRecordMapper.selectCount(map);
    }

    @Override
    public int create(CoinRecord coinRecord){
        return coinRecordMapper.insert(coinRecord);
    }

    @Override
    public int update(CoinRecord coinRecord){
        return coinRecordMapper.update(coinRecord);
    }
    @Override
    public int updateStatus(CoinRecord coinRecord){
        return coinRecordMapper.updateStatus(coinRecord);
    }

    @Override
    public int deleteById(Integer id){
        return coinRecordMapper.deleteById(id);
    }
}