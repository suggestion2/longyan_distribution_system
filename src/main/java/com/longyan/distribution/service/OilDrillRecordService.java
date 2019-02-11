package com.longyan.distribution.service;

import com.longyan.distribution.domain.OilDrillRecord;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public interface OilDrillRecordService {
    OilDrillRecord getById(Integer id);

    OilDrillRecord select(Map<String, Object> map);

    List<OilDrillRecord> selectList(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    int create(OilDrillRecord oilDrillRecord);

    int update(OilDrillRecord oilDrillRecord);

    int updateStatus(OilDrillRecord oilDrillRecord);

    int deleteById(Integer id);
}