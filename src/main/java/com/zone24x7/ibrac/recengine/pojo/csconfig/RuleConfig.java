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
}
