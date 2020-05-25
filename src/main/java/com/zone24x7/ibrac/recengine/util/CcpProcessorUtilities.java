package com.zone24x7.ibrac.recengine.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class providing CCP parameter processing functions
 *
 */
public final class CcpProcessorUtilities {

    private static List<String> whitelistedCcpKeys;

    /**
     * Default constructor.
     */
    private CcpProcessorUtilities() {
    }

    /**
     * Method to filter ccp map by using whitelisted ccp keys.
     *
     * @param ccpMap Map of ccp.
     * @return filtered ccp map.
     */
    public static Map<String, String> filterChannelContextParamMap(Map<String, String> ccpMap) {
        if (MapUtils.isNotEmpty(ccpMap) && CollectionUtils.isNotEmpty(whitelistedCcpKeys)) {
            return ccpMap.entrySet().stream()
                                    .filter(map -> whitelistedCcpKeys.contains(map.getKey()))
                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        return Collections.emptyMap();
    }

    /**
     * Method to set whitelisted ccp keys.
     *
     * @param whitelistedCcpKeys List of allowed ccp keys.
     */
    static void setWhitelistedCcpKeys(List<String> whitelistedCcpKeys) {
        CcpProcessorUtilities.whitelistedCcpKeys = whitelistedCcpKeys;
    }
}
