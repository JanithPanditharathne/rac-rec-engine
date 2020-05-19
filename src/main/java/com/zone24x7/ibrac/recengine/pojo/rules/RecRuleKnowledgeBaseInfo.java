package com.zone24x7.ibrac.recengine.pojo.rules;

import com.zone24x7.ibrac.recengine.pojo.csconfig.Rec;
import org.apache.commons.collections.MapUtils;
import org.drools.core.impl.InternalKnowledgeBase;

import java.util.HashMap;
import java.util.Map;

public class RecRuleKnowledgeBaseInfo {
    private InternalKnowledgeBase knowledgeBase;
    private Map<String, Rec> recIdToRecMap = new HashMap<>();

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
     * Method to return recId to rec map
     *
     * @return return recId to rec map
     */
    public Map<String, Rec> getRecIdToRecMap() {
        return recIdToRecMap;
    }

    /**
     * Method to set recId to rec map
     *
     * @param recIdToRecMap recId to rec map
     */
    public void setRecIdToRecMap(Map<String, Rec> recIdToRecMap) {
        if (MapUtils.isNotEmpty(recIdToRecMap)){
            this.recIdToRecMap.putAll(recIdToRecMap);
        }
    }
}
