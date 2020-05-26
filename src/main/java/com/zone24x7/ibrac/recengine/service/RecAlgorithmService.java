package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.dao.DatasourceAdapter;
import com.zone24x7.ibrac.recengine.exceptions.BaseConnectionException;
import com.zone24x7.ibrac.recengine.logging.Log;
import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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
     * Calls the relevant dao class and will retrieve the recommendations.
     *
     * @param recInputParams input parameters received.
     * @param activeBundle   active bundle object containing details for algo execution.
     * @param recCycleStatus recCycle status.
     * @return result generated from algorithm.
     */
    public AlgorithmResult getAlgorithmResult(RecInputParams recInputParams, ActiveBundle activeBundle, RecCycleStatus recCycleStatus) {
        Map<String, String> map = new HashMap<>();
        AlgorithmResult result = null;
        try {
            result = datasourceAdapter.getResult("100", map,recCycleStatus);
        } catch (BaseConnectionException e) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + " Error found while calling Hbase adapter", recCycleStatus.getRequestId(), e);
        }
        return result;
    }
}
