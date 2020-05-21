package com.zone24x7.ibrac.recengine.dao;

import com.zone24x7.ibrac.recengine.exceptions.HBaseAdapterException;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;

import java.util.Map;

/**
 * Interface for data source adapter.
 */
public interface DatasourceAdapter {

    /**
     * Method to get the an algorithm result for a given algorithm id and channel context parameters.
     *
     * @param algorithmId the algorithm id
     * @param ccp         the channel context parameters
     * @return the generated algorithm result
     * @throws HBaseAdapterException hbase adapter exception
     */
    AlgorithmResult getResult(String algorithmId, Map<String, String> ccp) throws HBaseAdapterException;
}
