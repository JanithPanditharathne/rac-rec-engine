package com.zone24x7.ibrac.recengine.util;

import com.zone24x7.ibrac.recengine.dao.HBaseKeyMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Class to initialize static context dependencies.
 */
@Component
public class StaticContextInitializer {

    @Autowired
    @Qualifier("whitelistedCcpKeys")
    private List<String> whitelistedCcpKeys;

    @Value(AppConfigStringConstants.HBASE_KEYMAKER_IGNORED_PARAMETERS)
    private List<String> ignoredCcpKeys;

    @Value(AppConfigStringConstants.HBASE_RECCOMMENDATIONKEY_CCP_MULTI_VALUED_KEYS)
    private List<String> sortedCcpKeys;


    @PostConstruct
    public void init() {
        CcpProcessorUtilities.setWhitelistedCcpKeys(whitelistedCcpKeys);
        HBaseKeyMaker.setIgnoredKeys(ignoredCcpKeys);
        HBaseKeyMaker.setSortedKeys(sortedCcpKeys);
    }
}