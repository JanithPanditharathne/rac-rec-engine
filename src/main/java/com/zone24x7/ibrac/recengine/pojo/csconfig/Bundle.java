package com.zone24x7.ibrac.recengine.pojo.csconfig;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * Class to represent a bundle.
 */
public class Bundle {
    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    private String type;

    @NotNull
    private Integer defaultLimit;

    @Valid @NotEmpty
    private List<BundleAlgorithmContainer> algorithms;

    @Valid
    @NotNull
    private AlgoCombineInfo algoCombineInfo;

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
     * Method to get the default limit.
     *
     * @return the default limit
     */
    public Integer getDefaultLimit() {
        return defaultLimit;
    }

    /**
     * Method to set the default limit.
     *
     * @param defaultLimit the default limit
     */
    public void setDefaultLimit(Integer defaultLimit) {
        this.defaultLimit = defaultLimit;
    }

    /**
     * Method to get the algorithms.
     *
     * @return the algorithms
     */
    public List<BundleAlgorithmContainer> getAlgorithms() {
        return algorithms;
    }

    /**
     * Method to set the algorithms.
     *
     * @param algorithms the algorithms
     */
    public void setAlgorithms(List<BundleAlgorithmContainer> algorithms) {
        if (algorithms != null) {
            Collections.sort(algorithms);
        }

        this.algorithms = algorithms;
    }

    /**
     * Method to get the algorithm combine information.
     *
     * @return the algorithm combine information
     */
    public AlgoCombineInfo getAlgoCombineInfo() {
        return algoCombineInfo;
    }

    /**
     * Method to set the algorithm combine information.
     *
     * @param algoCombineInfo the algorithm combine information
     */
    public void setAlgoCombineInfo(AlgoCombineInfo algoCombineInfo) {
        this.algoCombineInfo = algoCombineInfo;
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

        Bundle bundle = (Bundle) o;

        if (id != null ? !id.equals(bundle.id) : bundle.id != null) {
            return false;
        }

        if (name != null ? !name.equals(bundle.name) : bundle.name != null) {
            return false;
        }

        if (type != null ? !type.equals(bundle.type) : bundle.type != null) {
            return false;
        }

        if (defaultLimit != null ? !defaultLimit.equals(bundle.defaultLimit) : bundle.defaultLimit != null) {
            return false;
        }

        if (algorithms != null ? !algorithms.equals(bundle.algorithms) : bundle.algorithms != null) {
            return false;
        }

        return algoCombineInfo != null ? algoCombineInfo.equals(bundle.algoCombineInfo) : bundle.algoCombineInfo == null;
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
        result = HASH_SEED * result + (defaultLimit != null ? defaultLimit.hashCode() : 0);
        result = HASH_SEED * result + (algorithms != null ? algorithms.hashCode() : 0);
        result = HASH_SEED * result + (algoCombineInfo != null ? algoCombineInfo.hashCode() : 0);
        return result;
    }
}
