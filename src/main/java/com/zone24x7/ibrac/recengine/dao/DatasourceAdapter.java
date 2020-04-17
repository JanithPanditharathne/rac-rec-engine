package com.zone24x7.ibrac.recengine.dao;

import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;

import java.util.Map;

public interface DatasourceAdapter {
    AlgorithmResult getResult(String algorithmId, Map<String, String> ccp);
}
