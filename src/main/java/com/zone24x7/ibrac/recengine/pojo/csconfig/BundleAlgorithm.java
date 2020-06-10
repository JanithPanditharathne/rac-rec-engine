package com.zone24x7.ibrac.recengine.pojo.csconfig;

import javax.validation.constraints.NotEmpty;

/**
 * Class to represent bundle algorithm.
 */
public class BundleAlgorithm {
    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    private String type;

    @NotEmpty
    private String defaultDisplayText;

    private String customDisplayText;

    private static final int HASH_SEED = 31;

    /**
     * Method to get the algorithm id.
     *
     * @return the algorithm id
     */
    public String getId() {
        return id;
    }

    /**
     * Method to set the algorithm id.
     *
     * @param id the algorithm id
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
     * Method to get the default display text.
     *
     * @return the default display text
     */
    public String getDefaultDisplayText() {
        return defaultDisplayText;
    }

    /**
     * Method to set the default display text.
     *
     * @param defaultDisplayText the default display text
     */
    public void setDefaultDisplayText(String defaultDisplayText) {
        this.defaultDisplayText = defaultDisplayText;
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
     * Method to get the custom display text.
     *
     * @return the custom display text
     */
    public String getCustomDisplayText() {
        return customDisplayText;
    }

    /**
     * Method to set the custom display text.
     *
     * @param customDisplayText the custom display text
     */
    public void setCustomDisplayText(String customDisplayText) {
        this.customDisplayText = customDisplayText;
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

        BundleAlgorithm that = (BundleAlgorithm) o;

        if (id != null ? !id.equals(that.id) : (that.id != null)) {
            return false;
        }

        if (name != null ? !name.equals(that.name) : (that.name != null)) {
            return false;
        }

        if (type != null ? !type.equals(that.type) : (that.type != null)) {
            return false;
        }

        if (defaultDisplayText != null ? !defaultDisplayText.equals(that.defaultDisplayText) : (that.defaultDisplayText != null)) {
            return false;
        }

        return customDisplayText != null ? customDisplayText.equals(that.customDisplayText) : (that.customDisplayText == null);
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
        result = HASH_SEED * result + (defaultDisplayText != null ? defaultDisplayText.hashCode() : 0);
        result = HASH_SEED * result + (customDisplayText != null ? customDisplayText.hashCode() : 0);
        return result;
    }
}
