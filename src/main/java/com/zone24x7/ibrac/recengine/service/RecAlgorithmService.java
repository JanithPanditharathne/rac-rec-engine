package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.dao.DatasourceAdapter;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Responsible for executing a single recommendation algorithm.
 */
@Component
public class RecAlgorithmService implements AlgorithmService {

    @Autowired
    private DatasourceAdapter datasourceAdapter;

    /**
     * Calls ccp iterators and with the given ccp calls relevant dao class and retrieves the recommendations
     *
     * @param algorithmId    algorithm Id
     * @param ccp            channel context parameter
     * @param recCycleStatus recCycle status.
     * @return result generated from algorithm.
     */
    public AlgorithmResult getAlgorithmResult(String algorithmId, Map<String, String> ccp, RecCycleStatus recCycleStatus) {
        //TODO: Call algorithm combination iterator
        //TODO: Support multiple other ways
        AlgorithmResult result = datasourceAdapter.getResult(algorithmId, ccp);
        return result;
    }
}
