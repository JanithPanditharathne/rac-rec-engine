package com.zone24x7.ibrac.recengine.dao;

import com.zone24x7.ibrac.recengine.exceptions.BaseConnectionException;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;

import java.util.List;
import java.util.Map;

/**
 * Interface for data source adapter.
 */
public interface DatasourceAdapter {

    /**
     * Method to get the an list of product Ids for a given algorithm id and channel context parameters.
     *
     * @param algorithmId the algorithm id
     * @param ccp         the channel context parameters
     * @return the generated list of productIds
     * @throws BaseConnectionException base connection exception
     */
    List<String> getResult(String algorithmId, Map<String, String> ccp, RecCycleStatus recCycleStatus) throws BaseConnectionException;
}
