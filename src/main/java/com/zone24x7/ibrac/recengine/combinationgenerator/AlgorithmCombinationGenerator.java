package com.zone24x7.ibrac.recengine.combinationgenerator;

import com.zone24x7.ibrac.recengine.logging.Log;
import com.zone24x7.ibrac.recengine.pojo.algoparams.AlgoParams;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Class to to generate  CCP parameter combinations to a given algorithm.
 */
@Component
public class AlgorithmCombinationGenerator {
    private static final String ALGO_PARAMS_NULL_MSG = "Algo params to generate algorithm combinations is null.";
    private static final String EMPTY_CCP_MAP_TO_GENERATE_ALGO_COMBINATIONS_MSG = "Empty ccp map to generate algorithm combinations. Algo Id: {}";
    private static final String MANDATORY_PARAMS_NOT_FOUND_MSG = "Mandatory params not found in ccp map to generate algorithm combinations. AlgoId: {} Ccp Map: {} Required Mandatory Params: {}";
    private static final String CONDITIONAL_MANDATORY_PARAMS_NOT_FOUND_MSG = "Conditional mandatory params not found in ccp map to generate algorithm combinations. AlgoId: {} Ccp Map: {} Required Mandatory Params: {}";
    private static final String NO_COMBINATIONS_GENERATED_MSG = "No algorithm combinations generated. Algo Id: {} Ccp Map: {}";

    @Autowired
    private AlgoCombinationInputFilter algoCombinationInputFilter;

    @Log
    private static Logger logger;

    /**
     * Method to generate combinations based on algorithm params and input ccp params.
     *
     * @param algoParams algorithm params to consider.
     * @param ccp        input ccp params.
     * @param requestId  id of the request.
     * @return List of valid combinations.
     */
    List<List<String>> generateAlgoCombinations(AlgoParams algoParams, Map<String, String> ccp, String requestId) {
        // if algo params is null, return an empty list.
        if (algoParams == null) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + ALGO_PARAMS_NULL_MSG, requestId);
            return Collections.emptyList();
        }

        // if the algorithm is a generic algo which doesnt required ccp,  return.
        if (isGenericAlgorithm(algoParams)) {
            return new LinkedList<>(Collections.emptyList());
        }

        if (MapUtils.isEmpty(ccp)) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + EMPTY_CCP_MAP_TO_GENERATE_ALGO_COMBINATIONS_MSG, requestId, algoParams.getAlgoId());
            return Collections.emptyList();
        }

        // filter input ccp params using the algo params to generate combinations.
        List<String> mandatoryParams = algoCombinationInputFilter.getMandatoryParams(algoParams, ccp);
        List<List<String>> conditionalMandatoryParams = algoCombinationInputFilter.getConditionalMandatoryParams(algoParams, ccp);
        List<String> optionalParams = algoCombinationInputFilter.getOptionalParams(algoParams, ccp);
        boolean optionalCombinationEnabled = algoParams.isOptionalCombEnabled();

        // if algo params has mandatory params, get mandatory param based combinations.
        if (CollectionUtils.isNotEmpty(algoParams.getMandatoryParams())) {
            // return an empty list, since no mandatory params found.
            if (CollectionUtils.isEmpty(mandatoryParams)) {
                logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + MANDATORY_PARAMS_NOT_FOUND_MSG, requestId, algoParams.getAlgoId(), ccp, algoParams.getMandatoryParams());
                return Collections.emptyList();
            } else {
                return generateMandatoryCombinations(algoParams, mandatoryParams, conditionalMandatoryParams, optionalParams);
            }
        }

        // Move to conditional mandatory param combinations
        if (CollectionUtils.isNotEmpty(algoParams.getConditionalMandatoryParams())) {
            // return an empty list, if conditional mandatory params are empty.
            if (CollectionUtils.isEmpty(conditionalMandatoryParams)) {
                logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + CONDITIONAL_MANDATORY_PARAMS_NOT_FOUND_MSG, requestId, algoParams.getAlgoId(), ccp, algoParams.getConditionalMandatoryParams());
                return Collections.emptyList();
            }

            if (CollectionUtils.isNotEmpty(optionalParams)) {
                return getConditionalMandatoryAndOptionalParams(conditionalMandatoryParams, optionalParams, optionalCombinationEnabled);
            } else {
                // conditional mandatory only combinations
                return conditionalMandatoryParams;
            }
        }

        // Get optional only combinations.
        if (CollectionUtils.isNotEmpty(optionalParams)) {
            return getOptionalCombinations(optionalParams, optionalCombinationEnabled);
        }

        logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + NO_COMBINATIONS_GENERATED_MSG, requestId, algoParams.getAlgoId(), ccp);
        return Collections.emptyList();
    }

    /**
     * Method to get optional param combinations.
     *
     * @param optionalParams             list of optional params.
     * @param optionalCombinationEnabled if optional combinations is enabled.
     * @return list of optional combinations.
     */
    private List<List<String>> getOptionalCombinations(List<String> optionalParams, boolean optionalCombinationEnabled) {
        // if combinations are enabled, combine param lists.
        if (optionalCombinationEnabled) {
            return getOptionalParamSubsets(optionalParams);
        } else {
            List<List<String>> returnList = new LinkedList<>();
            optionalParams.forEach(optionalParam -> {
                List<String> params = new LinkedList<>();
                params.add(optionalParam);
                returnList.add(params);
            });

            return returnList;
        }
    }

    /**
     * Method to generate mandatory params based combinations.
     *
     * @param algoParams                 algorithm based algo params.
     * @param mandatoryParams            filtered mandatory params
     * @param conditionalMandatoryParams filtered conditional mandatory params
     * @param optionalParams             filtered optional params.
     * @return List of combinations.
     */
    private List<List<String>> generateMandatoryCombinations(AlgoParams algoParams, List<String> mandatoryParams, List<List<String>> conditionalMandatoryParams, List<String> optionalParams) {
        // mandatory + conditionally mandatory params combinations
        if (CollectionUtils.isNotEmpty(conditionalMandatoryParams)) {
            List<List<String>> combinationList = new LinkedList<>(Collections.singletonList(mandatoryParams));
            combinationList.addAll(conditionalMandatoryParams);
            return combinationList;
        }

        // mandatory + optional params combinations
        if (CollectionUtils.isNotEmpty(optionalParams)) {
            return getMandatoryAndOptionalCombinations(mandatoryParams, optionalParams, algoParams.isOptionalCombEnabled());
        }

        // mandatory only combination
        return Collections.singletonList(mandatoryParams);
    }

    /**
     * Method to generate mandatory and optional combinations.
     * <p>
     * Input: Mandatory[M1, M2] optional [Op1, Op2]
     * Output : [[M1, M2, Op1, Op2], [M1, M2, Op1], [M1, M2, Op2], [M1, M2]]
     *
     * @param mandatoryParams     mandatory params.
     * @param optionalParams      optional params.
     * @param optionalCombEnabled enable optional combinations. If this is false, combinations will not be generated.
     * @return list of combinations.
     */
    private List<List<String>> getMandatoryAndOptionalCombinations(List<String> mandatoryParams, List<String> optionalParams, boolean optionalCombEnabled) {
        // if combinations are enabled, combine param lists.
        if (optionalCombEnabled) {
            List<List<String>> optionalParamSubset = getOptionalParamSubsets(optionalParams);
            return generateOptionalParamsBasedCombinations(Collections.singletonList(mandatoryParams), optionalParamSubset);
        } else {
            // if combinations are disabled, return original lists.
            List<List<String>> returnList = new LinkedList<>();
            optionalParams.forEach(optionalParam -> {
                List<String> params = new LinkedList<>(mandatoryParams);
                params.add(optionalParam);
                returnList.add(params);
            });

            return returnList;
        }
    }

    /**
     * Method to generate conditional mandatory and optional combinations.
     * Permutations of optional combinations will be combined with conditional mandatory param combination.
     *
     * @param conMandatoryParams         conditional mandatory params.
     * @param optionalParams             optional params.
     * @param optionalCombinationEnabled enable optional combinations. If this is false, combinations will not be generated.
     * @return list of combinations.
     */
    private List<List<String>> getConditionalMandatoryAndOptionalParams(List<List<String>> conMandatoryParams, List<String> optionalParams, boolean optionalCombinationEnabled) {
        // if combinations are enabled, combine param lists.
        if (optionalCombinationEnabled) {
            List<List<String>> optionalParamSubset = getOptionalParamSubsets(optionalParams);
            return generateOptionalParamsBasedCombinations(conMandatoryParams, optionalParamSubset);
        } else {
            List<List<String>> returnList = new LinkedList<>();
            for (List<String> conditionalMandatoryList : conMandatoryParams) {
                optionalParams.forEach(optionalParam -> {
                    List<String> params = new LinkedList<>(conditionalMandatoryList);
                    params.add(optionalParam);
                    returnList.add(params);
                });
            }

            return returnList;
        }
    }

    /**
     * Generates optional params based combinations.
     *
     * @param listToCombineWith list to combine optional params with.
     * @param optionalParams    list of optional params.
     * @return optional and main combination lists.
     */
    private List<List<String>> generateOptionalParamsBasedCombinations(List<List<String>> listToCombineWith, List<List<String>> optionalParams) {
        List<List<String>> finalList = new LinkedList<>();
        // for each mandatory/conditional mandatory params, combine with optional params.
        for (List<String> mainList : listToCombineWith) {
            // iterate optional params and combine with each main list.
            for (List<String> optionalList : optionalParams) {
                List<String> combinedList = new LinkedList<>(mainList);
                combinedList.addAll(optionalList);
                finalList.add(combinedList);
            }

            finalList.add(mainList);
        }

        return finalList;
    }

    /**
     * Method to get subsets of a given list.
     *
     * @param list list to generate subsets from.
     * @return List generated subsets.
     */
    private List<List<String>> getOptionalParamSubsets(List<String> list) {
        List<List<String>> finalList = new LinkedList<>();
        //create subsets for each optional param
        for (int i = list.size(); i > 0; i--) {
            List<List<String>> listOfSubSets = generateSubSets(list, i);
            finalList.addAll(listOfSubSets);
        }

        return finalList;
    }

    /**
     * Method to get subsets of a list.
     *
     * @param list of params.
     * @param size index to get the subsets from.
     * @return subsets.
     */
    private List<List<String>> generateSubSets(List<String> list, int size) {
        List<List<String>> out = new ArrayList<>();
        for (int i = 0; i < list.size() - size + 1; i++) {
            List<String> subset = new ArrayList<>();
            for (int j = i; j < i + size - 1; j++) {
                subset.add(list.get(j));
            }

            if (!(size == 1 && i > 0)) {
                for (int j = i + size - 1; j < list.size(); j++) {
                    List<String> sub = new ArrayList<>(subset);
                    sub.add(list.get(j));
                    out.add(sub);
                }
            }
        }
        return out;
    }

    /**
     * Checks whether the given algo is a generic algorithm.
     *
     * @param algoParams algorithm params.
     * @return true if algo is a generic algorithm.
     */
    private boolean isGenericAlgorithm(AlgoParams algoParams) {
        return (CollectionUtils.isEmpty(algoParams.getMandatoryParams()) &&
                CollectionUtils.isEmpty(algoParams.getConditionalMandatoryParams()) &&
                CollectionUtils.isEmpty(algoParams.getOptionalParams()));
    }
}
