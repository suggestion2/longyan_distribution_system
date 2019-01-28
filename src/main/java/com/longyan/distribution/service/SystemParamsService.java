package com.longyan.distribution.service;

import com.longyan.distribution.domain.SystemParams;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public interface SystemParamsService {
    SystemParams getById(Integer id);

    SystemParams select(Map<String, Object> map);

    List<SystemParams> selectList(Map<String, Object> map);

    List<SystemParams> selectAll();

    int selectCount(Map<String, Object> map);

    int create(SystemParams systemParams);

    int update(SystemParams systemParams);

    int deleteById(Integer id);
}