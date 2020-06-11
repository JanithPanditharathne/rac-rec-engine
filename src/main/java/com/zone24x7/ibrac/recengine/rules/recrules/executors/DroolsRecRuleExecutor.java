package com.zone24x7.ibrac.recengine.rules.recrules.executors;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.csconfig.Rec;
import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;
import com.zone24x7.ibrac.recengine.pojo.rules.RecRuleKnowledgeBaseInfo;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.executors.CustomAgendaFilter;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.apache.commons.collections.CollectionUtils;
import org.drools.core.impl.InternalKnowledgeBase;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Drools based rule executor for matching context
 */
@Component
public class DroolsRecRuleExecutor implements RecRuleExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DroolsRecRuleExecutor.class);

    private RecRuleKnowledgeBaseInfo recRuleKnowledgeBaseInfo;

    /**
     * Get context matched recs from the set of given RecIds
     *
     * @param recIds         recIds to consider
     * @param ccp            channel context params
     * @param recCycleStatus cycle status
     * @return List of matching recs
     */
    public List<Rec> getMatchingRecs(final Set<String> recIds,
                                     final Map<String, String> ccp,
                                     final RecCycleStatus recCycleStatus) {

        List<Rec> resultRecs = new LinkedList<>();

        if (this.recRuleKnowledgeBaseInfo == null || this.recRuleKnowledgeBaseInfo.getKnowledgeBase() == null) {
            LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + StringConstants.DROOLS_KNOWLEDGE_BASE_NULL_MSG, recCycleStatus.getRequestId());
            return resultRecs;
        }

        Set<String> incomingRecIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(recIds)) {
            incomingRecIds.addAll(recIds);
        } else {
            return resultRecs;
        }

        InternalKnowledgeBase knowledgeBase = this.recRuleKnowledgeBaseInfo.getKnowledgeBase();

        KieSession kieSession = knowledgeBase.newKieSession();
        //Setting recRuleKnowledgeBaseInfo for drools to use recIdToRecMap
        kieSession.insert(recRuleKnowledgeBaseInfo);
        //Setting resultRecs for drools to insert results
        kieSession.insert(resultRecs);

        MatchingCondition matchingCondition = new MatchingCondition();
        matchingCondition.setMatchingMap(ccp);
        //Setting matchingCondition for drools to consume ccp values
        kieSession.insert(matchingCondition);

        kieSession.fireAllRules(new CustomAgendaFilter(incomingRecIds));
        kieSession.dispose();
        return resultRecs;
    }

    /**
     * Method to set recRuleKnowledgeBaseInfo
     *
     * @param recRuleKnowledgeBaseInfo recRuleKnowledgeBaseInfo to set
     */
    public void setRecRuleKnowledgeBaseInfo(RecRuleKnowledgeBaseInfo recRuleKnowledgeBaseInfo) {
        this.recRuleKnowledgeBaseInfo = recRuleKnowledgeBaseInfo;
    }
}