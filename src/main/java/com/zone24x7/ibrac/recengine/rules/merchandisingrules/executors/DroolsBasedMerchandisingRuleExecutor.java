package com.zone24x7.ibrac.recengine.rules.merchandisingrules.executors;

import com.zone24x7.ibrac.recengine.pojo.Product;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.rules.FilteringRulesResult;
import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;
import com.zone24x7.ibrac.recengine.pojo.rules.MerchandisingRuleKnowledgeBaseInfo;
import com.zone24x7.ibrac.recengine.pojo.rules.RuleExecutionStatus;
import com.zone24x7.ibrac.recengine.util.ListUtilities;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.apache.commons.collections.CollectionUtils;
import org.drools.core.impl.InternalKnowledgeBase;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Drools based implementation for the merchandising rule executor.
 */
@Component
public class DroolsBasedMerchandisingRuleExecutor implements MerchandisingRuleExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DroolsBasedMerchandisingRuleExecutor.class);

    private Set<String> globalFilteringRuleIds;
    private InternalKnowledgeBase knowledgeBase;

    /**
     * Method to apply the rules to filter the recommended products
     *
     * @param recommendations the recommendation products to be filtered
     * @param ruleIds         the rule ids to execute
     * @param ccp             the channel context parameters
     * @param recCycleStatus  the recommendation generation cycle status
     * @return the filtering rule result
     */
    @Override
    public FilteringRulesResult getFilteredRecommendations(final List<Product> recommendations,
                                                           final Set<String> ruleIds,
                                                           final Map<String, String> ccp,
                                                           final RecCycleStatus recCycleStatus) {
        FilteringRulesResult rulesResult = new FilteringRulesResult();

        RuleExecutionStatus ruleExecutionStatus = new RuleExecutionStatus();

        if (this.knowledgeBase == null) {
            LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + StringConstants.DROOLS_KNOWLEDGE_BASE_NULL_MSG, recCycleStatus.getRequestId());
            rulesResult.setFilteredRecommendedProductsList(recommendations);
            rulesResult.setExecutedFilteringRuleInfoList(new HashSet<>());
            return rulesResult;
        }

        Set<String> rules = new HashSet<>();
        if (ruleIds != null && !ruleIds.isEmpty()) {
            rules.addAll(ruleIds);
        }

        if (CollectionUtils.isNotEmpty(globalFilteringRuleIds)) {
            rules.addAll(globalFilteringRuleIds);
        }

        if (rules.isEmpty()) {
            rulesResult.setFilteredRecommendedProductsList(recommendations);
            rulesResult.setExecutedFilteringRuleInfoList(new HashSet<>());
            return rulesResult;
        }

        List<Product> productList = ListUtilities.removeDuplicates(recommendations);

        KieSession kieSession = knowledgeBase.newKieSession();
        kieSession.insert(productList);
        kieSession.insert(ruleExecutionStatus);

        MatchingCondition matchingCondition = new MatchingCondition();
        matchingCondition.setMatchingMap(ccp);
        kieSession.insert(matchingCondition);

        kieSession.fireAllRules(new CustomAgendaFilter(rules));
        kieSession.dispose();
        rulesResult.setFilteredRecommendedProductsList(productList);
        rulesResult.setExecutedFilteringRuleInfoList(ruleExecutionStatus.getExecutedRuleInfo());

        return rulesResult;
    }

    /**
     * Method to set the knowledge information
     *
     * @param knowledgeBaseInfo the knowledge information to set
     */
    @Override
    public void setKnowledgeBaseInfo(MerchandisingRuleKnowledgeBaseInfo knowledgeBaseInfo) {
        if (knowledgeBaseInfo != null) {
            globalFilteringRuleIds = knowledgeBaseInfo.getGlobalFilteringRuleIds();
            knowledgeBase = knowledgeBaseInfo.getKnowledgeBase();
        }
    }
}
