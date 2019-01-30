package com.longyan.distribution.mapper;

import com.longyan.distribution.domain.SystemParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SystemParamsMapper {

    SystemParams selectById(Integer id);

    SystemParams getValueByKey(Map<String, Object> map);

    SystemParams select(Map<String, Object> map);

    List<SystemParams> selectList(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    int insert(SystemParams systemParams);

    int update(SystemParams systemParams);

    int deleteById(Integer id);
}