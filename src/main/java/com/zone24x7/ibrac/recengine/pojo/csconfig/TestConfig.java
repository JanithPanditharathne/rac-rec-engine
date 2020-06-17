package com.zone24x7.ibrac.recengine.pojo.csconfig;

/**
 * Class to represent test configuration.
 *
 */
public class TestConfig {
    private String id;

    // Constructor
    public TestConfig() {
        // Empty test config until auto optimizer is implemented.
    }

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

        TestConfig that = (TestConfig) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    /**
     * Overridden hash code method
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
