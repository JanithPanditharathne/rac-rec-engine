package com.zone24x7.ibrac.recengine.pojo.rules;

import com.zone24x7.ibrac.recengine.enumeration.RuleType;

/**
 * Class to represent the information about a rule that was executed by the recommendation provider
 * when generating the recommendations
 */
public class EdeExecutedRuleInfo {
    private String ruleId;
    private RuleType ruleType;

    // Constants
    private static final int HASH_CONSTANT = 31;

    /**
     * Getter for the rule id
     *
     * @return the rule id
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * Getter for the rule type
     *
     * @return the rule type
     */
    public RuleType getRuleType() {
        return ruleType;
    }

    /**
     * Setter for the rule id
     *
     * @param ruleId the rule id
     */
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * Setter for the rule type
     *
     * @param ruleType the rule type
     */
    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    /**
     * Check the equality of this object to another EdeExecutedRuleInfo object
     *
     * @param obj the other object to compare against
     * @return true if both objects are equal based on the assigned criteria, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        EdeExecutedRuleInfo that = (EdeExecutedRuleInfo) obj;

        if (ruleId != null ? !ruleId.equals(that.ruleId) : (that.ruleId != null)) {
            return false;
        }

        return ruleType == that.ruleType;
    }

    /**
     * Generated the hash code of this object
     *
     * @return the hash code of this object calculated based on the specified criteria
     */
    @Override
    public int hashCode() {
        int result = ruleId != null ? ruleId.hashCode() : 0;
        result = HASH_CONSTANT * result + (ruleType != null ? ruleType.hashCode() : 0);
        return result;
    }

    /**
     * Overridden toString method
     *
     * @return the toSting output
     */
    @Override
    public String toString() {
        return "{" +
                "ruleId='" + ruleId + '\'' +
                ", ruleType=" + ruleType +
                '}';
    }
}
