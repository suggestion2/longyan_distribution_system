package com.longyan.distribution.mapper;

import com.longyan.distribution.domain.CoinRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CoinRecordMapper {

    CoinRecord selectById(Integer id);

    CoinRecord select(Map<String, Object> map);

    List<CoinRecord> selectList(Map<String, Object> map);

    List<CoinRecord> selectCoinRecordAndCustomerList(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    int insert(CoinRecord coinRecord);

    int update(CoinRecord coinRecord);

    int updateStatus(CoinRecord coinRecord);

    int deleteById(Integer id);
}