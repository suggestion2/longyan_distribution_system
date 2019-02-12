package com.longyan.distribution.mapper;

import com.longyan.distribution.domain.OilDrillRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OilDrillRecordMapper {

    OilDrillRecord selectById(Integer id);

    OilDrillRecord select(Map<String, Object> map);

    List<OilDrillRecord> selectList(Map<String, Object> map);

    List<OilDrillRecord> selectOilRecordAndCustomerList(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    int updateStatus(OilDrillRecord oilDrillRecord);

    int insert(OilDrillRecord oilDrillRecord);

    int update(OilDrillRecord oilDrillRecord);

    int deleteById(Integer id);
}