package com.zone24x7.ibrac.recengine.pojo.csconfig;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Class to represent recommendation.
 */
public class Rec {
    @NotEmpty
    private String id;

    @NotEmpty
    private String name;
    private String type;
    private String matchingCondition;

    // Until auto optimizer is implemented and test config starts working, regular config is mandatory.
    // In case of auto optimizer enabled, make sure to remove this.
    @Valid @NotNull
    private RegularConfig regularConfig;

    private TestConfig testConfig;

    private static final int HASH_SEED = 31;

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
     * Method to get the matching condition.
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
     * Method to get the regular config.
     *
     * @return the regular config
     */
    public RegularConfig getRegularConfig() {
        return regularConfig;
    }

    /**
     * Method to set the regular config.
     *
     * @param regularConfig the regular config
     */
    public void setRegularConfig(RegularConfig regularConfig) {
        this.regularConfig = regularConfig;
    }

    /**
     * Method to get the test config.
     *
     * @return the test config
     */
    public TestConfig getTestConfig() {
        return testConfig;
    }

    /**
     * Method to set the test config.
     *
     * @param testConfig the test config
     */
    public void setTestConfig(TestConfig testConfig) {
        this.testConfig = testConfig;
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

        Rec rec = (Rec) o;

        if (id != null ? !id.equals(rec.id) : (rec.id != null)) {
            return false;
        }

        if (name != null ? !name.equals(rec.name) : (rec.name != null)) {
            return false;
        }

        if (type != null ? !type.equals(rec.type) : (rec.type != null)) {
            return false;
        }

        if (matchingCondition != null ? !matchingCondition.equals(rec.matchingCondition) : (rec.matchingCondition != null)) {
            return false;
        }

        if (regularConfig != null ? !regularConfig.equals(rec.regularConfig) : (rec.regularConfig != null)) {
            return false;
        }

        return testConfig != null ? testConfig.equals(rec.testConfig) : (rec.testConfig == null);
    }

    /**
     * Overridden hash code method
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = HASH_SEED * result + (name != null ? name.hashCode() : 0);
        result = HASH_SEED * result + (type != null ? type.hashCode() : 0);
        result = HASH_SEED * result + (matchingCondition != null ? matchingCondition.hashCode() : 0);
        result = HASH_SEED * result + (regularConfig != null ? regularConfig.hashCode() : 0);
        result = HASH_SEED * result + (testConfig != null ? testConfig.hashCode() : 0);
        return result;
    }
}
