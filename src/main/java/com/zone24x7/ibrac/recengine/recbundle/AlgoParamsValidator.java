package com.zone24x7.ibrac.recengine.recbundle;

import java.util.Map;

/**
 * Interface for validating algorithm parameters.
 */
public interface AlgoParamsValidator {
    /**
     * Method to validate whether the given algorithm is valid for the incoming context.
     *
     * @param algorithmId the algorithm id
     * @param ccp         the channel context
     * @return true if valid and false if not
     */
    boolean isValidForIncomingContext(String algorithmId, Map<String, String> ccp);
}
