package com.zone24x7.ibrac.recengine.dao;

import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.Product;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class HBaseAdapter implements DatasourceAdapter {

    @Autowired
    private HBaseDao hBaseDao;

    @Override
    public AlgorithmResult getResult(String algorithmId, Map<String, String> ccp) {
        Result result = hBaseDao.getResult("abc", "trending", "rec", algorithmId);
        byte[] value = result.getValue("rec".getBytes(), algorithmId.getBytes());
        //Curate
        Product p = new Product();
        return new AlgorithmResult();
    }
}
