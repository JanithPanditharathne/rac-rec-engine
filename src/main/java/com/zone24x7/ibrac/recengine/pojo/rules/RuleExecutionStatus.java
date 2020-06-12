package com.zone24x7.ibrac.recengine.pojo.rules;

import com.zone24x7.ibrac.recengine.enumeration.RuleType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class keeping track of rule execution
 */
public class RuleExecutionStatus {
    private Set<ExecutedRuleInfo> executedRuleInfo;
    private ConcurrentMap<String, AtomicInteger> hitCountMap;

    /**
     * Constructor to instantiate RuleExecutionStatus.
     */
    public RuleExecutionStatus() {
        this.executedRuleInfo = Collections.synchronizedSet(new HashSet<>());
        this.hitCountMap = new ConcurrentHashMap<>();
    }

    /**
     * Method to add an executed rule id.
     *
     * @param ruleId   the rule id to add
     * @param ruleType the rule type to add
     */
    public void addExecutedRuleId(String ruleId, RuleType ruleType) {
        ExecutedRuleInfo currentExecutedRuleInfo = new ExecutedRuleInfo();
        currentExecutedRuleInfo.setRuleId(ruleId);
        currentExecutedRuleInfo.setRuleType(ruleType);
        this.executedRuleInfo.add(currentExecutedRuleInfo);

        this.hitCountMap.putIfAbsent(ruleId, new AtomicInteger(0));
        this.hitCountMap.get(ruleId).getAndIncrement();
    }

    /**
     * Method to get all executed rule information.
     *
     * @return the list of executed rule info
     */
    public Set<ExecutedRuleInfo> getExecutedRuleInfo() {
        return this.executedRuleInfo;
    }

    /**
     * Method to get the number of hits when a rule is executed.
     *
     * @param ruleId the rule Id of the rule to get the hit count
     * @return the number of hits
     */
    public int getHitCount(String ruleId) {
        int count = 0;

        if (hitCountMap.containsKey(ruleId)) {
            count = this.hitCountMap.get(ruleId).get();
        }

        return count;
    }
}
