package com.zone24x7.ibrac.recengine.rules.merchandisingrules.executors;

import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;

import java.util.Set;

/**
 * Class for custom agenda filter which is useful when filtering what rules should be executed.
 */
public class CustomAgendaFilter implements AgendaFilter {
    private final Set<String> ruleNamesThatAreAllowedToFire;

    /**
     * Constructor to instantiate an object of CustomAgentFilter.
     *
     * @param ruleNamesThatAreAllowedToFire set of rules to be filtered out and executed.
     */
    public CustomAgendaFilter(Set<String> ruleNamesThatAreAllowedToFire) {
        this.ruleNamesThatAreAllowedToFire = ruleNamesThatAreAllowedToFire;
    }

    /**
     * Method to accept the match
     *
     * @param match the match
     * @return true if matches
     */
    @Override
    public boolean accept(Match match) {
        return ruleNamesThatAreAllowedToFire.contains(match.getRule().getName());
    }
}