package com.zone24x7.ibrac.recengine.recrules.executors;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.csconfig.Rec;
import com.zone24x7.ibrac.recengine.pojo.rules.RecRuleKnowledgeBaseInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Rec rule executor interface
 */
public interface RecRuleExecutor {

    /**
     * Get context matched recs from the set of given RecIds
     *
     * @param recIds         recIds to consider
     * @param ccp            channel context params
     * @param recCycleStatus cycle status
     * @return List of matching recs
     */
    List<Rec> getMatchingRecs(final Set<String> recIds,
                              final Map<String, String> ccp,
                              final RecCycleStatus recCycleStatus);

    /**
     * Method to set recRuleKnowledgeBaseInfo
     *
     * @param recRuleKnowledgeBaseInfo recRuleKnowledgeBaseInfo to set
     */
    void setRecRuleKnowledgeBaseInfo(RecRuleKnowledgeBaseInfo recRuleKnowledgeBaseInfo);
}
