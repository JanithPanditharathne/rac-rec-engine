package com.zone24x7.ibrac.recengine.dao;

import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.Product;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Class to represent the HBase adapter implementation of the data source adapter.
 */
public class HBaseAdapter implements DatasourceAdapter {

    @Autowired
    private HBaseDao hBaseDao;

    /**
     * Method to get the an algorithm result for a given algorithm id and channel context parameters.
     *
     * @param algorithmId the algorithm id
     * @param ccp         the channel context parameters
     * @return the generated algorithm result
     */
    @Override
    public AlgorithmResult getResult(String algorithmId, Map<String, String> ccp) {
        Result result = hBaseDao.getResult("abc", "trending", "rec", algorithmId);
        byte[] value = result.getValue("rec".getBytes(), algorithmId.getBytes());
        //Curate
        Product p = new Product();
        return new AlgorithmResult();
    }
}
