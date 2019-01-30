package com.longyan.distribution.service;

import com.longyan.distribution.domain.GoldRecord;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public interface GoldRecordService {
    GoldRecord getById(Integer id);

    GoldRecord select(Map<String, Object> map);

    List<GoldRecord> selectList(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    int create(GoldRecord goldRecord);

    int update(GoldRecord goldRecord);

    int deleteById(Integer id);
}