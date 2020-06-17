package com.zone24x7.ibrac.recengine.pojo.csconfig;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * Class to represent a rule in rule configs
 */
public class Rule {
    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    @NotEmpty
    @Pattern(regexp = "BOOST|BURY|ONLY_RECOMMEND|DO_NOT_RECOMMEND")
    private String type;

    private boolean isGlobal;
    private String matchingCondition;

    @NotEmpty
    private String actionCondition;

    private static final int HASH_SEED1 = 17;
    private static final int HASH_SEED2 = 37;

    /**
     * Method to get the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Method to set the id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Method to get the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set the name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method to check whether the rule is a global rule
     *
     * @return true if a global rule, else if not
     */
    public boolean isGlobal() {
        return isGlobal;
    }

    /**
     * Method to set whether the rule is a global rule
     *
     * @param global true if global and false is not
     */
    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    /**
     * Method to get the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Method to set the type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Method to get the matching condition
     *
     * @return the matching condition
     */
    public String getMatchingCondition() {
        return matchingCondition;
    }

    /**
     * Method to set the matching condition.
     *
     * @param matchingCondition the matching condition
     */
    public void setMatchingCondition(String matchingCondition) {
        this.matchingCondition = matchingCondition;
    }

    /**
     * Method to get the action condition
     *
     * @return the action condition
     */
    public String getActionCondition() {
        return actionCondition;
    }

    /**
     * Method to set the action condition
     *
     * @param actionCondition the action condition
     */
    public void setActionCondition(String actionCondition) {
        this.actionCondition = actionCondition;
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

        Rule rule = (Rule) o;

        return new EqualsBuilder()
                .append(isGlobal, rule.isGlobal)
                .append(id, rule.id)
                .append(name, rule.name)
                .append(type, rule.type)
                .append(matchingCondition, rule.matchingCondition)
                .append(actionCondition, rule.actionCondition)
                .isEquals();
    }

    /**
     * Overridden hash code method
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(HASH_SEED1, HASH_SEED2)
                .append(id)
                .append(name)
                .append(type)
                .append(isGlobal)
                .append(matchingCondition)
                .append(actionCondition)
                .toHashCode();
    }
}
