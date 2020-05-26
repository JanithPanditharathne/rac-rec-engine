package com.zone24x7.ibrac.recengine.dao;

import com.zone24x7.ibrac.recengine.exceptions.BaseConnectionException;
import com.zone24x7.ibrac.recengine.logging.Log;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.Product;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.tableconfigs.TableConfigInfo;
import com.zone24x7.ibrac.recengine.service.TableConfigReaderService;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.apache.hadoop.hbase.client.Result;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Class to represent the HBase adapter implementation of the data source adapter.
 */
public class HBaseAdapter implements DatasourceAdapter {

    @Autowired
    private HBaseDao hBaseDao;

    @Autowired
    private TableConfigReaderService tableConfigReaderService;

    @Log
    private static Logger logger;

    private static final String INVALID_ALGORITHM_ID_MSG = "Incorrect Algorithm ID provided, No tableConfigInfo found";

    /**
     * Method to get the an algorithm result for a given algorithm id and channel context parameters.
     *
     * @param algorithmId the algorithm id
     * @param ccp         the channel context parameters
     * @return the generated algorithm result
     */
    @Override
    public AlgorithmResult getResult(String algorithmId, Map<String, String> ccp, RecCycleStatus recCycleStatus) throws BaseConnectionException {

        // Generate the key
        String key = HBaseKeyMaker.generateRowKey(ccp);

        // Get the tableConfig info by algorithm id.
        TableConfigInfo tableConfigInfo = tableConfigReaderService.getTableConfigInfoByAlgorithmId(algorithmId);

        // Throw BaseConnectionException if the table config info is null.
        if (tableConfigInfo == null) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + " Table info not found for algo Id : {}", recCycleStatus.getRequestId(), algorithmId);
            throw new BaseConnectionException(INVALID_ALGORITHM_ID_MSG);
        }

        // call
        Result result = hBaseDao.getResult(key, tableConfigInfo.getTableName(), tableConfigInfo.getColumnFamily(), algorithmId, recCycleStatus);
        byte[] value = result.getValue("rec".getBytes(), algorithmId.getBytes());
        //Curate
        Product p = new Product();
        return new AlgorithmResult();
    }
}
