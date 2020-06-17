package com.zone24x7.ibrac.recengine.pojo.csconfig;

import javax.validation.Valid;
import java.util.List;

/**
 * Class to represent rule configs.
 */
public class RuleConfig {
    @Valid
    private List<Rule> rules;

    /**
     * Method to get the rules.
     *
     * @return the rules
     */
    public List<Rule> getRules() {
        return rules;
    }

    /**
     * Method to set the rules.
     *
     * @param rules the rules
     */
    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    /**
     * Overridden equals method
     *
     * @param o object to compare
     * @return true if equal and false if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RuleConfig that = (RuleConfig) o;

        return rules != null ? rules.equals(that.rules) : (that.rules == null);
    }

    /**
     * Overridden hash code method
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return rules != null ? rules.hashCode() : 0;
    }
}
