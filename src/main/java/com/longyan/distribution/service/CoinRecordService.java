package com.longyan.distribution.service;

import com.longyan.distribution.domain.CoinRecord;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public interface CoinRecordService {
    CoinRecord getById(Integer id);

    CoinRecord select(Map<String, Object> map);

    List<CoinRecord> selectList(Map<String, Object> map);

    List<CoinRecord> selectCoinRecordAndCustomerList(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    int create(CoinRecord coinRecord);

    int update(CoinRecord coinRecord);

    int updateStatus(CoinRecord coinRecord);

    int deleteById(Integer id);
}