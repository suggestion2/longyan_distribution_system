package com.longyan.distribution.mapper;

import com.longyan.distribution.domain.GoldRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GoldRecordMapper {

    GoldRecord selectById(Integer id);

    GoldRecord select(Map<String, Object> map);

    List<GoldRecord> selectList(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    int insert(GoldRecord goldRecord);

    int update(GoldRecord goldRecord);

    int updateStatus(GoldRecord goldRecord);

    int deleteById(Integer id);
}