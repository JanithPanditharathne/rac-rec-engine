package com.zone24x7.ibrac.recengine.rules.executors;

import com.zone24x7.ibrac.recengine.pojo.Product;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.rules.FilteringRulesResult;
import com.zone24x7.ibrac.recengine.pojo.rules.MerchandisingRuleKnowledgeBaseInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for merchandising rule executor.
 */
public interface MerchandisingRuleExecutor {
    /**
     * Method to apply the rules to filter the recommended products
     *
     * @param recommendations the recommendation products to be filtered
     * @param ruleIds         the rule ids to execute
     * @param ccp             the channel context parameters
     * @param recCycleStatus  the recommendation generation cycle status
     * @return the filtering rule result
     */
    FilteringRulesResult getFilteredRecommendations(final List<Product> recommendations,
                                                    final Set<String> ruleIds,
                                                    final Map<String, String> ccp,
                                                    final RecCycleStatus recCycleStatus);

    /**
     * Method to set the knowledge information
     *
     * @param knowledgeBaseInfo the knowledge information to set
     */
    void setKnowledgeBaseInfo(MerchandisingRuleKnowledgeBaseInfo knowledgeBaseInfo);
}
