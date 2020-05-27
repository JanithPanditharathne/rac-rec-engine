package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.dao.DatasourceAdapter;
import com.zone24x7.ibrac.recengine.exceptions.BaseConnectionException;
import com.zone24x7.ibrac.recengine.logging.Log;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.slf4j.Logger;
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

    @Log
    private static Logger logger;

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
        AlgorithmResult result = null;
        try {
            result = datasourceAdapter.getResult(algorithmId, ccp, recCycleStatus);
        } catch (BaseConnectionException e) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + " Error found while calling Hbase adapter", recCycleStatus.getRequestId(), e);
        }
        return result;
    }
}
