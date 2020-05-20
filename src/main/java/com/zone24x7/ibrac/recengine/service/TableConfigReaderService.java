package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.pojo.tableconfigs.TableConfigInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Responsible for read table config json and serve table config info objects.
 */
@Component
public class TableConfigReaderService {

    @Autowired
    @Qualifier("tableConfigurationMap")
    private Map<String, TableConfigInfo> tableConfigurationMap = null;


    /**
     * Method to get the TableConfigInfo for given algorithmName.
     *
     * @return the TableConfigInfo
     */
    public TableConfigInfo getTableConfigInfoByAlgorithmName(String algorithmName) {
        return tableConfigurationMap.get(algorithmName);
    }

}
