package com.zone24x7.ibrac.recengine.pojo.rules;

import org.apache.commons.collections.CollectionUtils;
import org.drools.core.impl.InternalKnowledgeBase;

import java.util.HashSet;
import java.util.Set;

/**
 * POJO class for merchandising rule knowledge base information.
 */
public class MerchandisingRuleKnowledgeBaseInfo {
    private InternalKnowledgeBase knowledgeBase;
    private Set<String> globalFilteringRuleIds;

    /**
     * Constructor to instantiate MerchandisingRuleKnowledgeBaseInfo
     */
    public MerchandisingRuleKnowledgeBaseInfo() {
        this.globalFilteringRuleIds = new HashSet<>();
    }

    /**
     * Method to get the rule knowledge base
     *
     * @return the rule knowledge base
     */
    public InternalKnowledgeBase getKnowledgeBase() {
        return knowledgeBase;
    }

    /**
     * Method to set the rule knowledge base
     *
     * @param knowledgeBase the rule knowledge base to set
     */
    public void setKnowledgeBase(InternalKnowledgeBase knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    /**
     * Method to get the global filtering rule ids.
     *
     * @return the global filtering rule ids
     */
    public Set<String> getGlobalFilteringRuleIds() {
        return globalFilteringRuleIds;
    }

    /**
     * Method to add the global filtering rule ids.
     *
     * @param globalFilteringRuleIds global filtering rule ids
     */
    public void addGlobalFilteringRuleIds(Set<String> globalFilteringRuleIds) {
        if (CollectionUtils.isNotEmpty(globalFilteringRuleIds)) {
            this.globalFilteringRuleIds.addAll(globalFilteringRuleIds);
        }
    }
}
