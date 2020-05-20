package com.zone24x7.ibrac.recengine.dao;

import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.Product;
import com.zone24x7.ibrac.recengine.pojo.tableconfigs.TableConfigInfo;
import com.zone24x7.ibrac.recengine.service.TableConfigReaderService;
import org.apache.hadoop.hbase.client.Result;
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

    /**
     * Method to get the an algorithm result for a given algorithm id and channel context parameters.
     *
     * @param algorithmId the algorithm id
     * @param ccp         the channel context parameters
     * @return the generated algorithm result
     */
    @Override
    public AlgorithmResult getResult(String algorithmId, Map<String, String> ccp) {

        // Generate the key
        String key = HBaseKeyMaker.generateRowKey(ccp);


        // Get the tableConfig info by algorithm id.
        TableConfigInfo tableConfigInfo = tableConfigReaderService.getTableConfigInfoByAlgorithmName(algorithmId);

        // call
        Result result = hBaseDao.getResult(key, tableConfigInfo.getTableName(), tableConfigInfo.getColumnFamily(), algorithmId);
        byte[] value = result.getValue("rec".getBytes(), algorithmId.getBytes());
        //Curate
        Product p = new Product();
        return new AlgorithmResult();
    }
}
