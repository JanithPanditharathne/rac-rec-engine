package com.zone24x7.ibrac.recengine.dao;

import com.zone24x7.ibrac.recengine.exceptions.BaseConnectionException;
import com.zone24x7.ibrac.recengine.logging.Log;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.tableconfigs.TableConfigInfo;
import com.zone24x7.ibrac.recengine.service.TableConfigReaderService;
import com.zone24x7.ibrac.recengine.util.ListUtilities;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.*;

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
     * @return the generated list of productIds
     * @throws BaseConnectionException base connection exception
     * @throws UnsupportedOperationException if entries to the specified algorithm id is not configured
     */
    @Override
    public List<String> getResult(String algorithmId, Map<String, String> ccp, RecCycleStatus recCycleStatus) throws BaseConnectionException {
        // Get the tableConfig info by algorithm id.
        TableConfigInfo tableConfigInfo = tableConfigReaderService.getTableConfigInfoByAlgorithmId(algorithmId);

        // Generate the key
        String key = HBaseKeyMaker.generateRowKey(ccp);

        if (tableConfigInfo == null || tableConfigInfo.getTableName() == null || tableConfigInfo.getColumnFamily() == null) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Table info not found for algo Id : {}", recCycleStatus.getRequestId(), algorithmId);
            throw new UnsupportedOperationException(INVALID_ALGORITHM_ID_MSG);
        }

        Result result = hBaseDao.getResult(key, tableConfigInfo.getTableName(), tableConfigInfo.getColumnFamily(), algorithmId, recCycleStatus);
        byte[] value = result.getValue(tableConfigInfo.getColumnFamily().getBytes(), algorithmId.getBytes());

        return generateListOfStrings(value, recCycleStatus);
    }

    /**
     * Generates list of strings of productIds
     *
     * @param value byte value
     * @param recCycleStatus recCycleStatus
     * @return list of product strings trimmed
     */
    private List<String> generateListOfStrings(byte[] value, RecCycleStatus recCycleStatus) {
        if (value == null) {
            return Collections.emptyList();
        }

        String resultString = new String(value, StandardCharsets.UTF_8);

        if (StringUtils.isNotEmpty(resultString)) {
            String[] productIds = resultString.split(",");
            List<String> productIdsCleansed = new LinkedList<>();
            for (String productId : productIds) {
                productIdsCleansed.add(productId.trim());
            }
            recCycleStatus.indicateHBaseReturnedRecs();
            return ListUtilities.removeDuplicates(productIdsCleansed);
        }
        return Collections.emptyList();
    }
}
