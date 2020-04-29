package com.zone24x7.ibrac.recengine.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @PostConstruct
    public void init() {
        CcpProcessorUtilities.setWhitelistedCcpKeys(whitelistedCcpKeys);
    }
}
