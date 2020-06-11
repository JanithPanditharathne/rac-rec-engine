package com.zone24x7.ibrac.recengine.recbundle;

import com.zone24x7.ibrac.recengine.pojo.algoparams.AlgoParams;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Implementation class for recommendation algorithm validator.
 */
@Component
public class RecAlgoParamsValidator implements AlgoParamsValidator {

    @Autowired
    @Qualifier("algoParamsMap")
    private Map<String, AlgoParams> algoParamsMap;

    /**
     * Method to validate whether the given algorithm is valid for the incoming context.
     *
     * @param algorithmId the algorithm id
     * @param ccp         the channel context
     * @return true if valid and false if not
     */
    @Override
    public boolean isValidForIncomingContext(String algorithmId, Map<String, String> ccp) {
        if (MapUtils.isEmpty(algoParamsMap)) {
            return false;
        }

        AlgoParams algoParams = algoParamsMap.get(algorithmId);

        if (algoParams == null) {
            return false;
        }

        List<String> mandatoryParams = algoParams.getMandatoryParams();
        List<List<String>> conditionalMandatoryParams = algoParams.getConditionalMandatoryParams();


        if (CollectionUtils.isNotEmpty(mandatoryParams)) {
            // If all mandatory parameters are available return true, else false.
            return checkValidity(mandatoryParams, ccp);
        } else if (CollectionUtils.isNotEmpty(conditionalMandatoryParams)) {
            // Conditional mandatory parameter combinations are checked. If even on combination is valid, returns true.
            for (List<String> conditionalMandatoryParameterSet : conditionalMandatoryParams) {
                if (checkValidity(conditionalMandatoryParameterSet, ccp)) {
                    return true;
                }
            }

            // Returns false if no conditional mandatory combination was valid.
            return false;
        }

        // Optional parameter validations are not require to be done. Therefore sending true.
        return true;
    }

    /**
     * Method to check whether the ccp is valid as per the provided parameters.
     *
     * @param parameters the parameter to be checked. (Can be mandatory or conditional mandatory)
     * @param ccp        the channel context
     * @return true if valid and false if not
     */
    private static boolean checkValidity(List<String> parameters, Map<String, String> ccp) {
        return ccp.keySet().containsAll(parameters);
    }
}
