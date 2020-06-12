package com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.handlers;

import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.MultipleAlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import com.zone24x7.ibrac.recengine.pojo.RecStatusParams;
import com.zone24x7.ibrac.recengine.pojo.rules.FilteringRulesResult;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.executors.MerchandisingRuleExecutor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Class for calling the rules executor and updating the result to RecStatus Param
 */
@Component
@Qualifier("filteringRulesHandler")
public class FilteringRulesHandler implements RecUnitHandler {

    @Autowired
    private MerchandisingRuleExecutor merchandisingRuleExecutor;

    /**
     * Call the Drools rules executor and applies the filtering rule
     *
     * @param recInputParams  input params carrying object
     * @param recStatusParams status carrying object
     * @param activeBundle    active bundle details object
     */
    @Override
    public void handleTask(RecInputParams recInputParams, RecStatusParams recStatusParams, ActiveBundle activeBundle) {

        MultipleAlgorithmResult multipleAlgorithmResult = recStatusParams.getMultipleAlgorithmResult();
        Set<String> placementFilteringRuleIds = activeBundle.getPlacementFilteringRuleIds();

        if (multipleAlgorithmResult != null &&
                CollectionUtils.isNotEmpty(multipleAlgorithmResult.getRecProducts()) &&
                CollectionUtils.isNotEmpty(placementFilteringRuleIds)) {

            FilteringRulesResult filteringRulesResult = merchandisingRuleExecutor.getFilteredRecommendations(multipleAlgorithmResult.getRecProducts(),
                                                                                                             placementFilteringRuleIds,
                                                                                                             recInputParams.getCcp(),
                                                                                                             recStatusParams.getRecCycleStatus());
            recStatusParams.setFilteringRulesResult(filteringRulesResult);
        }
    }
}
